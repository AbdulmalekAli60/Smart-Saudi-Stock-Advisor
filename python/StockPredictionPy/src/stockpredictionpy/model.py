import pandas as pd
import numpy as np
import logging
from sklearn.model_selection import TimeSeriesSplit
import xgboost as xgb
import matplotlib.pyplot as plt
from stock_indicators import StockIndicators

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
        
        self.dataframe['target_value'] = self.dataframe.groupby('company_id')['close'].shift(-1)
        self.dataframe = self.dataframe.dropna(subset=['target_value'])
        self.dataframe.sort_values(['company_id', 'data_date']).reset_index(drop=True)

    def data_split(self, train = 0.70, valiadtion = 0.15, test = 0.15):
        unique_dates = sorted(self.dataframe['data_date'].unique())
        total_dates = len(unique_dates)

        train_end = int(total_dates * train) # trrain start from the beginging
        valiadtion_end = int(total_dates * (train + valiadtion))

        train_end_date = unique_dates[train_end - 1]
        valiadtion_end_date = unique_dates[valiadtion_end - 1]

        train_set = self.dataframe[self.dataframe['data_date'] <= train_end_date].copy()

        validation_set = self.dataframe[
            (self.dataframe['data_date'] > train_end_date) &
            (self.dataframe['data_date'] <= valiadtion_end_date)
            ].copy()
        
        test_set = self.dataframe[self.dataframe['data_date'] > valiadtion_end_date].copy()

        print("Data spliting done")
        logger.info("Data splitting done")
        logger.info(f"Train period: {train_set['data_date'].min()} to {train_set['data_date'].max()}")
        logger.info(f"Validation period: {validation_set['data_date'].min()} to {validation_set['data_date'].max()}")
        logger.info(f"Test period: {test_set['data_date']} to {test_set['data_date'].max()}")
        logger.info(f"Train: {len(train_set)}, Validation: {len(validation_set)}, Test: {len(test_set)}")

        # print(train_set, validation_set, test_set)
        return train_set, validation_set, test_set
        
    def xgboost_model(self):
        train,validation, test = self.data_split()
        

if __name__ == "__main__":
    xgboostMoedl = XgBoostModel()
    xgboostMoedl.data_split()
    xgboostMoedl.xgboost_model()
   