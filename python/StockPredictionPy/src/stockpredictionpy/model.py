import pandas as pd
import numpy as np
import logging
from xgboost import XGBRegressor
from config.companies_list import companies
import matplotlib.pyplot as plt
from stock_indicators import StockIndicators
from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score
from datetime import  timedelta
from config.connect_to_database import connect_to_database

logger = logging.getLogger("Xgboost model module")
logger.setLevel(logging.INFO)

file_handler = logging.FileHandler('Xgboost_Model.log', mode="w")

formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

if not logger.handlers:
    logger.addHandler(file_handler)

class XgBoostModel:

    def __init__(self):
        stock_indicators = StockIndicators()
        data = stock_indicators.calculate_all_indicators()
        self.dataframe = data
        self.original_dataframe = data.copy()
        self.last_rows = {}
        
        self.conn = connect_to_database()
        self.cur = None
        self.connect_to_db()
        
        
        self.preprocessing_data()

    def preprocessing_data(self):
        self.dataframe['tomorrow_close'] = self.dataframe.groupby('company_id')['close'].shift(-1)
        self.dataframe = self.dataframe.dropna(subset=['tomorrow_close'])
        # self.dataframe = self.dataframe[self.dataframe['volume'] > 0]
        self.dataframe = self.dataframe.sort_values(['company_id', 'data_date']).reset_index(drop=True)

    def connect_to_db(self):
        if self.conn:
            try:
                self.cur = self.conn.cursor()
                logger.info("Database connected and cursor created successfully")
            except Exception as e:
                logger.error(f"Failed to create cursor: {str(e)}")
                self.conn = None
        else:
            logger.error("Failed to establish database connection")

    def data_split(self, train_percentage = 0.70, validation_percentage = 0.15, test_percentage = 0.15):
        companies_sets = {}
        for company_id in self.dataframe['company_id'].unique():
            filtered_company_data = self.dataframe[self.dataframe['company_id'] == company_id].copy()
            filtered_company_data = filtered_company_data.sort_values('data_date').reset_index(drop=True)
            filtered_company_data['data_date'] = range(len(filtered_company_data))
            total_data = len(filtered_company_data)
            
            train_end = int(total_data * train_percentage)
            validation_end = int(total_data * (train_percentage + validation_percentage))

            train_set = filtered_company_data[:train_end]
            validation_set = filtered_company_data[train_end:validation_end]
            test_set = filtered_company_data[validation_end:]
            
            companies_sets[company_id] = {
                "train":train_set,
                "validation": validation_set,
                "test":test_set
            }

            logger.info("=" * 50)
            logger.info(f"Splitting Company: {company_id}.")
            logger.info(f"The length of Train set is: {len(train_set)}.")
            logger.info(f"The length of validation set is: {len(validation_set)}.")
            logger.info(f"The length of Test set is: {len(test_set)}.")
            logger.info("=" * 50)
        
        return companies_sets
           
    def xgboost_model(self):
        companies_sets_dict = self.data_split()
        reg = None
        predictions_dict  = {}
        for company_id, company_set in companies_sets_dict.items():
            # if company_id != 4:
            #     continue
            train_data = company_set['train']
            validation_data = company_set['validation']
            test_data = company_set['test']
            
            x_train, y_train = train_data.iloc[:, :-1], train_data.iloc[:, -1]
            x_validation, y_validation = validation_data.iloc[:, :-1], validation_data.iloc[:, -1]
            x_test, y_test = test_data.iloc[:, :-1], test_data.iloc[:, -1]
            
            logger.info(f"Training for company: {company_id} Training with: {x_train.shape[1]} features and with {x_train.shape[0]} samples")
            logger.info(f"Company: {company_id}")
            reg = XGBRegressor(    
                n_estimators=75, 
                learning_rate=0.1,     
                max_depth=4,            
                min_child_weight=20,    
                reg_alpha=20.0,         
                reg_lambda=30.0,        
                subsample=0.6,         
                colsample_bytree=0.6,   
                early_stopping_rounds=5,
                random_state=42,
                enable_categorical=True
            )
            
            logger.info("Training XGBoost model...")
            
            reg.fit(
                x_train, y_train,
                eval_set=[(x_validation, y_validation)]
            )
            
            train_pred = reg.predict(x_train)
            val_pred = reg.predict(x_validation)
            test_pred = reg.predict(x_test)

            train_rmse = np.sqrt(mean_squared_error(y_train, train_pred))
            train_mae = mean_absolute_error(y_train, train_pred)
            train_r2 = r2_score(y_train, train_pred)

            val_rmse = np.sqrt(mean_squared_error(y_validation, val_pred))
            val_mae = mean_absolute_error(y_validation, val_pred)
            val_r2 = r2_score(y_validation, val_pred)

            test_rmse = np.sqrt(mean_squared_error(y_test, test_pred))
            test_mae = mean_absolute_error(y_test, test_pred)
            test_r2 = r2_score(y_test, test_pred)

            logger.info("=" * 50)
            logger.info(f"Model Performance:")
            logger.info(f"Train RMSE: {train_rmse:.4f}")
            logger.info(f"Train MAE: {train_mae:.4f}")
            logger.info(f"Train R²: {train_r2:.4f}")
            logger.info("=" * 50)
            logger.info(f"Validation RMSE: {val_rmse:.4f}")
            logger.info(f"Validation MAE: {val_mae:.4f}")
            logger.info(f"Validation R²: {val_r2:.4f}")
            logger.info("=" * 50)
            logger.info(f"Test RMSE: {test_rmse:.4f}")
            logger.info(f"Test MAE: {test_mae:.4f}")
            logger.info(f"Test R²: {test_r2:.4f}")
            logger.info("=" * 50)

            self.last_rows[company_id] = self.get_last_row(company_id=company_id)
            prediction_row = self.get_prediction_row(company_id=company_id, x_train_columns=x_train.columns)
            tomorrow_prediction = reg.predict(prediction_row)[0]

            current_price = self.last_rows[company_id]['close'].iloc[0]
            direction = tomorrow_prediction > current_price
         
            predictions_dict [company_id] = {
                'direction': direction,
                'prediction' : tomorrow_prediction
            }
                                    
            logger.info(f"XGBoost model training completed for company: {company_id}")
        
        return predictions_dict 
            
    def get_last_row(self, company_id):
        company_data = self.original_dataframe[self.original_dataframe['company_id'] == company_id]
        last_row = company_data.iloc[-1:, :]
        logger.info(f"Last row for database operations: {last_row.shape}")
        return last_row
    
    def get_prediction_row(self, company_id, x_train_columns):
        company_data = self.original_dataframe[self.original_dataframe['company_id'] == company_id]
        last_row = company_data.iloc[-1:, :].copy()
        last_row['data_date'] = 9999  
        prediction_row = last_row[x_train_columns]
        logger.info(f"Prediction row columns: {list(prediction_row.columns)}")
        return prediction_row
    
    def get_date(self):
        today = pd.Timestamp.today().normalize()
        today_date = today.strftime('%Y-%m-%d')
        yesterday_date = (today - timedelta(days=1)).strftime('%Y-%m-%d')
        tomorrow_date = (today + pd.Timedelta(days=1)).strftime('%Y-%m-%d')
        return today_date, yesterday_date, tomorrow_date

    def insert_prediction(self, predictions_dict):
        if not predictions_dict:
            logger.info("There is no prediction dict")
            return
        
        successfully_inserted_predictions = []
        failed_inserted_predictions = []
        updated_predictions = []
        failed_updates = []
        
        try:
            for company_id, prediction_set in predictions_dict.items():
                insert_success = self.handle_insert(company_id, prediction_set)
                if insert_success:
                    successfully_inserted_predictions.append(company_id)
                else:
                    failed_inserted_predictions.append(company_id)
                
                update_success = self.handle_update(company_id)
                if update_success:
                    updated_predictions.append(company_id)
                else:
                    failed_updates.append(company_id)
            
            self.handle_db_commit()
            
        except Exception as e:
            logger.error(f"Transaction failed: {str(e)}")
            self.conn.rollback()
        finally:
            self.close_db()

        logger.info(f"Successfully inserted predictions: {len(successfully_inserted_predictions)}")
        logger.info(f"Failed to insert predictions: {len(failed_inserted_predictions)}")

    def close_db(self):
        if self.cur is not None:
            self.cur.close()
            logger.info("Cursor is closed")
        if self.conn is not None:
            self.conn.close()
            logger.info("Connection is closed")

    def handle_db_commit(self):
        try:
            self.conn.commit()
            logger.info("Transaction Done")
            return True
        except Exception as e:
            logger.error(f"Transaction Failed: {str(e)}")
            self.conn.rollback()
            return False

    def handle_insert(self, company_id, pred_data):
        today_date, yesterday_date, tomorrow_date = self.get_date()
        insert_query = """
            INSERT INTO prediction 
            (actual_result, direction, expiration_date, prediction_date, company_id, prediction)
            VALUES (%s, %s, %s, %s, %s, %s);
            """
        
        insert_values = (
            None,
            bool(pred_data['direction']),
            tomorrow_date,
            today_date,
            int(company_id),
            float(pred_data['prediction'])
        )
        
        try:
            self.cur.execute(insert_query, insert_values)
            logger.info(f"Successfully inserted prediction for company {company_id}")
            return True
        except Exception as e:
            logger.error(f'Failed to insert prediction. Company id: {company_id} Error: {str(e)}')
            return False

    def handle_update(self, company_id):
        today_date, yesterday_date, tomorrow_date = self.get_date()
        update_query = """
            UPDATE prediction
            SET actual_result = %s
            WHERE prediction_date = %s 
            AND company_id = %s
            AND actual_result IS NULL
            AND expiration_date = %s
            """
        
        update_values = (
            float(self.last_rows[company_id]['close'].iloc[0]),
            yesterday_date,
            int(company_id),
            today_date
        )
        
        try:
            self.cur.execute(update_query, update_values)
            if self.cur.rowcount > 0:
                logger.info(f"Successfully updated prediction for company {company_id}")
                return True
            else:
                logger.info(f"No previous prediction found to update for company {company_id}")
                return True
        except Exception as e:
            logger.error(f"Failed to update previous prediction for company: {company_id}")
            return False
    
    def save_sets_to_excel(self):
        companies_sets_dict = self.data_split()
        
        for company_id, company_set in companies_sets_dict.items():
            train_data = company_set['train']
            validation_data = company_set['validation']
            test_data = company_set['test']
            
            train_data.to_excel(f'train_company_{company_id}.xlsx', index=False)
            validation_data.to_excel(f'validation_company_{company_id}.xlsx', index=False)
            test_data.to_excel(f'test company_{company_id}.xlsx', index=False)

            logger.info(f"Train, validation, test, Train for company: {company_id} Excel file saved")
            print(f"Train, validation, test, Excel file saved for company: {company_id}")  

if __name__ == "__main__":
    obj = XgBoostModel()
    pred = obj.xgboost_model()
    obj.insert_prediction(predictions_dict=pred)