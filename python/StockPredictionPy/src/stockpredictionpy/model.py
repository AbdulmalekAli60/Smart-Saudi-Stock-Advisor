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
        self.dataframe['tomorrow_close'] = self.dataframe.groupby('company_id')['close'].shift(-1)
        self.dataframe = self.dataframe.dropna(subset=['tomorrow_close'])
        self.dataframe = self.dataframe[self.dataframe['volume'] > 0]
        self.dataframe = self.dataframe.sort_values(['company_id', 'data_date']).reset_index(drop=True)

        self.conn = connect_to_database()
        self.cur = None
        if self.conn:
            try:
                self.cur = self.conn.cursor()
                logger.info("Database connected and cursor created successfully")
            except Exception as e:
                logger.error(f"Failed to create cursor: {str(e)}")
                self.conn = None
        else:
            logger.error("Failed to establish database connection")

    def data_split(self, train_percentage = 0.70, valiadtion_percentage = 0.15, test_percentage = 0.15):

        companies_sets = {}
        for compnay_id in self.dataframe['company_id'].unique():
            
            filtered_compnay_data = self.dataframe[self.dataframe['company_id'] == compnay_id].copy()

            filtered_compnay_data = filtered_compnay_data.sort_values('data_date').reset_index(drop=True)
            
            # filtered_compnay_data['data_date'] = filtered_compnay_data['data_date'].astype('int64')
            filtered_compnay_data['data_date'] = range(len(filtered_compnay_data))
            total_data = len(filtered_compnay_data)
            
            train_end = int(total_data * train_percentage) # trrain start from the beginging

            valiadtion_end = int(total_data * (train_percentage + valiadtion_percentage))

            train_set = filtered_compnay_data[:train_end]

            validation_set = filtered_compnay_data[train_end:valiadtion_end]
        
            test_set = filtered_compnay_data[valiadtion_end:]

            companies_sets[compnay_id] = {
                "train":train_set,
                "validation": validation_set,
                "test":test_set
            }

            logger.info("=" * 50)
            logger.info(f"Spliting Compnay: {compnay_id}.")
            logger.info(f"The length of Train set is: {len(train_set)}.")
            logger.info(f"The length of validation set is: {len(validation_set)}.")
            logger.info(f"The length of Test set is: {len(test_set)}.")
            logger.info("=" * 50)

            
        return companies_sets
        

    def correct_directional_accuracy(self, y_actual, y_predicted, actual_prices):
        """
        Calculate correct directional accuracy for stock prediction
        
        Parameters:
        - y_actual: Tomorrow's actual closing prices (your target variable)
        - y_predicted: Model's predictions for tomorrow's prices  
        - actual_prices: Today's actual closing prices (needed for calculating changes)
        
        Returns:
        - direction_accuracy_1: Accuracy based on price change directions
        - direction_accuracy_2: Accuracy based on above/below average
        """
        
        # Method 1: Direction of predicted vs actual price changes
        # Actual price changes (tomorrow_actual - today_actual)
        actual_changes = y_actual - actual_prices 
        
        # Predicted price changes (predicted_tomorrow - today_actual)
        predicted_changes = y_predicted - actual_prices
        
        # Compare directions (up=1, down=-1, flat=0)
        actual_directions = np.sign(actual_changes)
        predicted_directions = np.sign(predicted_changes)
        
        # Calculate accuracy
        direction_accuracy_1 = np.mean(actual_directions == predicted_directions)
        
        # Method 2: Simple up/down prediction (above/below average)
        avg_price = np.mean(y_actual)
        actual_direction = y_actual > avg_price
        predicted_direction = y_predicted > avg_price
        
        direction_accuracy_2 = np.mean(actual_direction == predicted_direction)
        
        print(f"Directional Accuracy (price changes): {direction_accuracy_1:.1%}")
        print(f"Directional Accuracy (above/below avg): {direction_accuracy_2:.1%}")
        
        # Additional debugging info
        print(f"Sample actual changes: {actual_changes[:5]}")
        print(f"Sample predicted changes: {predicted_changes[:5]}")
        print(f"Up predictions: {np.sum(predicted_directions > 0)}/{len(predicted_directions)}")
        print(f"Down predictions: {np.sum(predicted_directions < 0)}/{len(predicted_directions)}")
        
        return direction_accuracy_1, direction_accuracy_2
    
    def xgboost_model(self):
        companies_sets_dict = self.data_split()
        reg = None
        predictions_dict  = {}
        for company_id, company_set in companies_sets_dict.items():
            # if company_id != 17:
            #     continue

            train_data = company_set['train']
            validation_data = company_set['validation']
            test_data = company_set['test']
            
            x_train, y_train = train_data.iloc[:, :-1], train_data.iloc[:, -1]
            x_validation, y_validation = validation_data.iloc[:, :-1], validation_data.iloc[:, -1]
            x_test, y_test = test_data.iloc[:, :-1], test_data.iloc[:, -1]
            
            logger.info(f"Training for company: {company_id} Training with: {x_train.shape[1]} features and with {x_train.shape[0]} samples")

            reg = XGBRegressor(    
                n_estimators=50, 
                learning_rate=0.1,     
                max_depth=3,            
                min_child_weight=20,    
                reg_alpha=10.0,         
                reg_lambda=20.0,        
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

            last_row = self.get_last_row(test_set=test_data)
            tomorrow_prediction = reg.predict([last_row])[0]

            current_price = last_row['close']
            direction = tomorrow_prediction > current_price
         
            predictions_dict [company_id] = {
                'direction': direction,
                'prediction' : tomorrow_prediction
            }
            
           
            print(f"\nModel Performance:")
            print(f"Train RMSE: {np.sqrt(mean_squared_error(y_train, train_pred)):.4f}")
            print(f"Train MAE: {mean_absolute_error(y_train, train_pred):.4f}")
            print(f"Train R²: {r2_score(y_train, train_pred):.4f}")
            
            print(f"Validation RMSE: {np.sqrt(mean_squared_error(y_validation, val_pred)):.4f}")
            print(f"Validation MAE: {mean_absolute_error(y_validation, val_pred):.4f}")
            print(f"Validation R²: {r2_score(y_validation, val_pred):.4f}")
            
            print(f"Test RMSE: {np.sqrt(mean_squared_error(y_test, test_pred)):.4f}")
            print(f"Test MAE: {mean_absolute_error(y_test, test_pred):.4f}")
            print(f"Test R²: {r2_score(y_test, test_pred):.4f}")
            
            
            print(f"\nPrediction Analysis:")
            print(f"Actual target range: {y_test.min():.4f} to {y_test.max():.4f}")
            print(f"Predicted range: {test_pred.min():.4f} to {test_pred.max():.4f}")
            print(f"Actual target std: {y_test.std():.4f}")
            print(f"Predicted std: {test_pred.std():.4f}")
            
            # Feature importance
            # self.feature_importance_plot(reg=reg, train_cols=x_train)
            
            print(f"\nDirectional Accuracy Analysis:")
            
            actual_prices_today = test_data['close'].values  
            
          
            dir_acc_1, dir_acc_2 = self.correct_directional_accuracy(
                y_test.values,      
                test_pred,          
                actual_prices_today
            )
            
            logger.info(f"XGBoost model training completed for company: {company_id}")
            # self.save_sets_to_excel()
            
        return predictions_dict 
            
    def get_last_row(self, test_set):
        last_row = test_set.iloc[-1, :-1]
        return last_row
    
    def feature_importance_plot(self, reg, train_cols):
        feature_importance = pd.DataFrame({
            'feature': train_cols.columns,
            'importance': reg.feature_importances_
            }).sort_values('importance', ascending=False)
            
        plt.plot(feature_importance['feature'], feature_importance['importance'])
        plt.xlabel("Features")
        plt.ylabel("Improtance")
        plt.title("Feature Imortance")
        plt.legend()
        plt.show()

    # this method will insert the prediction to the prediciton table in database
    def insert_prediction(self, predictions_dict):
        successfully_inserted_predictions = []
        failed_inserted_predictions = []
    
        today = pd.Timestamp.today().normalize()
        today_date = today.strftime('%Y-%m-%d')
        tomorrow_date = (today + pd.Timedelta(days=1)).strftime('%Y-%m-%d')
    
        try:
            for company_id, prediction_set in predictions_dict.items():
                insert_query = """
                    INSERT INTO prediction 
                    (actual_result, direction, expiration_date, prediction_date, company_id, prediction)
                    VALUES (%s, %s, %s, %s, %s, %s);
                    """
            
                values = (
                    None,
                    bool(prediction_set['direction']),
                    tomorrow_date,
                    today_date,
                    int(company_id),
                    float(prediction_set['prediction'])
                )
            
                try:
                    self.cur.execute(insert_query, values)
                    successfully_inserted_predictions.append(company_id)
                except Exception as e:
                    failed_inserted_predictions.append(company_id)
                    logger.error(f'Failed to insert prediction. Company id: {company_id} Error: {str(e)}')
        
            self.conn.commit()
        
        except Exception as e:
            logger.error(f"Transaction failed: {str(e)}")
            self.conn.rollback()
        finally:
            if self.cur is not None:
                self.cur.close()
                logger.info("Cursor is closed")
            if self.conn is not None:
                self.conn.close()
                logger.info("Connection is closed")
    
        logger.info(f"Successfully inserted predictions: {len(successfully_inserted_predictions)}")
        logger.info(f"Failed to insert predictions: {len(failed_inserted_predictions)}")



    def save_sets_to_excel(self):
        companies_sets_dict = self.data_split()
        
        for company_id, company_set in companies_sets_dict.items():
            train_data = company_set['train']
            validation_data = company_set['validation']
            test_data = company_set['test']
            
            train_data.to_excel(f'train_company_{company_id}.xlsx', index=False)
            validation_data.to_excel(f'validation_compnay_{company_id}.xlsx', index=False)
            test_data.to_excel(f'test company_{company_id}.xlsx', index=False)

            logger.info(f"Train, validation, test, Train for company: {company_id} Excel file saved")
            print(f"Train, validation, test, Excel file saved for company: {company_id}")  
            break

if __name__ == "__main__":
    obj = XgBoostModel()
    obj.data_split()
    pred = obj.xgboost_model()
    obj.insert_prediction(predictions_dict=pred)
    # obj.save_sets_to_excel()
   