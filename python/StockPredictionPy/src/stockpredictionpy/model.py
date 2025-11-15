import pandas as pd
import numpy as np
import logging
from xgboost import XGBRegressor
from stock_indicators import StockIndicators
from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score
from sklearn.model_selection import GridSearchCV, TimeSeriesSplit
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
        self.last_rows:dict = {}
        
        self.preprocessing_data()

    def preprocessing_data(self) -> None:
        self.dataframe['tomorrow_close'] = self.dataframe.groupby('company_id')['close'].shift(-1)
        self.dataframe = self.dataframe.dropna(subset=['tomorrow_close'])
        self.dataframe = self.dataframe[self.dataframe['volume'] > 0]
        self.dataframe = self.dataframe.sort_values(['company_id', 'data_date']).reset_index(drop=True)

    def data_split(self, train_percentage:float = 0.70, validation_percentage:float = 0.15, test_percentage:float = 0.15) -> dict[int, dict[str,pd.DataFrame]]:
        companies_sets:dict = {}
        for company_id in self.dataframe['company_id'].unique():
            filtered_company_data:pd.DataFrame = self.dataframe[self.dataframe['company_id'] == company_id].copy()
            filtered_company_data:pd.DataFrame  = filtered_company_data.sort_values('data_date').reset_index(drop=True)
            filtered_company_data['data_date'] = range(len(filtered_company_data))
            total_data:int = len(filtered_company_data)
            
            train_end:int = int(total_data * train_percentage)
            validation_end:int = int(total_data * (train_percentage + validation_percentage))

            train_set:pd.DataFrame = filtered_company_data[:train_end]
            validation_set:pd.DataFrame = filtered_company_data[train_end:validation_end]
            test_set:pd.DataFrame = filtered_company_data[validation_end:]

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
           
    def xgboost_model(self) -> dict[int, dict[str, bool | float]]:
        companies_sets_dict:dict[int, dict[str,pd.DataFrame]] = self.data_split()
        reg = None
        predictions_dict:dict[int, dict[str, bool | float]]  = {}

        for company_id, company_set in companies_sets_dict.items():
            # if company_id != 4:
            #     continue
            train_data:pd.DataFrame = company_set['train']
            validation_data:pd.DataFrame = company_set['validation']
            test_data:pd.DataFrame = company_set['test']

            x_train, y_train = train_data.iloc[:, :-1], train_data.iloc[:, -1]
            x_validation, y_validation = validation_data.iloc[:, :-1], validation_data.iloc[:, -1]
            x_test, y_test = test_data.iloc[:, :-1], test_data.iloc[:, -1]
            
            logger.info(f"Training for company: {company_id} Training with: {x_train.shape[1]} features and with {x_train.shape[0]} samples")
            logger.info(f"Company: {company_id}")

            reg = XGBRegressor(    
                n_estimators=1000,    
                subsample=0.6,         
                colsample_bytree=0.6,   
                early_stopping_rounds=10,
                random_state=42,
                enable_categorical=True
            )
            
            grid_params = {
                'max_depth': [2, 3],        
                'learning_rate': [0.05, 0.1],  
                'reg_alpha': [0, 10, 50],     
                'reg_lambda': [25, 100, 200],  
                'min_child_weight': [5, 10, 15] 
            }
            
            tscv = TimeSeriesSplit(n_splits=3)

            fit_params = {
                'eval_set': [(x_validation, y_validation)]
            }

            gsc = GridSearchCV(estimator=reg, param_grid=grid_params, verbose=1, n_jobs=-1, cv=tscv, scoring='neg_mean_squared_error')
            logger.info("Training XGBoost model...")
            
            # reg.fit(
            #     x_train, y_train,
            #     eval_set=[(x_validation, y_validation)]
            # )

            gsc.fit(x_train, y_train, **fit_params)

            logger.info(f"GridSearchCV complete for company: {company_id}")
            logger.info(f"Best parameters found: {gsc.best_params_}")
            logger.info(f"Best CV score (neg_mean_squared_error): {gsc.best_score_:.4f}")

            best_model = gsc.best_estimator_

            train_pred:np.ndarray = best_model.predict(x_train)
            val_pred:np.ndarray = best_model.predict(x_validation)
            test_pred:np.ndarray = best_model.predict(x_test)

            train_rmse:float = np.sqrt(mean_squared_error(y_train, train_pred))
            train_mae:float = mean_absolute_error(y_train, train_pred)
            train_r2:float = r2_score(y_train, train_pred)

            val_rmse:float = np.sqrt(mean_squared_error(y_validation, val_pred))
            val_mae:float = mean_absolute_error(y_validation, val_pred)
            val_r2:float = r2_score(y_validation, val_pred)

            test_rmse:float = np.sqrt(mean_squared_error(y_test, test_pred))
            test_mae:float = mean_absolute_error(y_test, test_pred)
            test_r2:float = r2_score(y_test, test_pred)
        
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

            # importance_df = pd.DataFrame({
            #     'features_name': best_model.feature_names_in_,
            #     'importance': best_model.feature_importances_
            # })

            # logger.info(importance_df)
            self.last_rows[company_id] = self.get_last_row(company_id=company_id)
            prediction_row:pd.Series = self.get_prediction_row(company_id=company_id, x_train_columns=x_train.columns)
            tomorrow_prediction:float = best_model.predict(prediction_row)[0]

            current_price:float = self.last_rows[company_id]['close'].iloc[0]
            direction:bool = tomorrow_prediction > current_price
    
            predictions_dict [company_id] = {
                'direction': direction,
                'prediction' : tomorrow_prediction
            }
                   
            logger.info(f"XGBoost model training completed for company: {company_id}")
            logger.info("=" * 50)

        return predictions_dict 
            
    def get_last_row(self, company_id:int) -> pd.DataFrame:
        company_data:pd.DataFrame = self.original_dataframe[self.original_dataframe['company_id'] == company_id]
        last_row:pd.DataFrame = company_data.iloc[-1:, :]
        logger.info(f"Last row for database operations: {last_row.shape}")
        return last_row
    
    def get_prediction_row(self, company_id: int, x_train_columns:pd.Index) -> pd.DataFrame:
        company_data:pd.DataFrame = self.original_dataframe[self.original_dataframe['company_id'] == company_id]
        last_row:pd.DataFrame = company_data.iloc[-1:, :].copy()
        last_row['data_date'] = 9999  
        prediction_row:pd.DataFrame = last_row[x_train_columns]
        logger.info(f"Prediction row columns: {list(prediction_row.columns)}")
        return prediction_row
        
    def save_sets_to_excel(self) ->None:
        companies_sets_dict = self.data_split()
        
        for company_id, company_set in companies_sets_dict.items():
            if company_id != 2:
                continue
            train_data = company_set['train']
            validation_data = company_set['validation']
            test_data = company_set['test']
            
            train_data.to_excel(f'train_company_{company_id}.xlsx', index=False)
            validation_data.to_excel(f'validation_company_{company_id}.xlsx', index=False)
            test_data.to_excel(f'test company_{company_id}.xlsx', index=False)

            logger.info(f"Train, validation, test, Train for company: {company_id} Excel file saved")
            print(f"Train, validation, test, Excel file saved for company: {company_id}")  

if __name__ == "__main__":
    model_obj = XgBoostModel()
    pred = model_obj.xgboost_model()
    # model_obj.save_sets_to_excel()

    # db_saver = SavePredictions()
    # db_saver.insert_prediction(predictions_dict = pred, last_rows= model_obj.last_rows)