import pandas as pd
import read_data_from_db
import logging
import openpyxl
logging.basicConfig(level=logging.INFO, filename="stock_idicators.log", filemode="w", format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)
class StockIndicators:
    def __init__(self):
        
        data_reader = read_data_from_db.ReadDataFromDB()
        self.dataframe = data_reader.read_data()

    # trend indicators
    def sma(self):
        time_periods = [10, 12, 20, 26, 50]
        
        for time_period in time_periods:
            
            for company_id in self.dataframe['company_id'].unique():
                company_mask = self.dataframe['company_id'] == company_id
                company_sma = self.dataframe.loc[company_mask, 'close'].rolling(window=time_period).mean()
                self.dataframe.loc[company_mask, f'sma_{time_period}'] = company_sma.values

        logger.info("sma done")
        print("SMA  DONE")
        return self.dataframe

    # Exponential Moving Average (EMA) 
    def ema(self):
    
        time_periods = [5, 12, 26, 50]
        
        self.dataframe = self.dataframe.sort_values(['company_id', 'data_date']).reset_index(drop=True)
        
        for time_period in time_periods:
            
            self.dataframe[f'ema_{time_period}'] = float('nan')
        
            for company_id in self.dataframe['company_id'].unique():
                
                company_mask = self.dataframe['company_id'] == company_id
                
                company_ema = self.dataframe.loc[company_mask, 'close'].ewm(span=time_period, adjust=False).mean()
                
                self.dataframe.loc[company_mask, f'ema_{time_period}'] = company_ema.values
        
        logger.info("EMA done")
        print("EMA done")
        return self.dataframe
    

    #Moving Average Convergence Divergence (MACD) 
    def macd(self):
        self.dataframe = self.dataframe.sort_values(['company_id', 'data_date']).reset_index(drop=True)
        self.dataframe['signal_line'] = float('nan')
        self.dataframe['macd_line'] = self.dataframe['ema_12'] - self.dataframe['ema_26']

        for company_id in self.dataframe['company_id'].unique():
            company_mask = self.dataframe['company_id'] == company_id
            company_signal = self.dataframe.loc[company_mask, 'macd_line'].ewm(span=9, adjust=False).mean()
            self.dataframe.loc[company_mask, 'signal_line'] = company_signal.values
        
        self.dataframe['macd'] = self.dataframe['macd_line'] - self.dataframe['signal_line']
        self.dataframe.drop('signal_line', axis=1, inplace= True)
        self.dataframe.drop('macd_line', axis=1, inplace=True)
    
        logger.info("MACD Done")
        print("MACD Done")
        return self.dataframe

    # Price Rate of Change (ROC) 
    def roc(self):
        time_periods = [5, 10]
        self.dataframe = self.dataframe.sort_values(['company_id', 'data_date']).reset_index(drop=True)
    
        for time_period in time_periods:
            self.dataframe[f'roc_{time_period}'] = float("nan")
        
            for company_id in self.dataframe['company_id'].unique():
                company_mask = self.dataframe['company_id'] == company_id
                company_indices = self.dataframe.index[company_mask].tolist()
            
                for i, current_idx in enumerate(company_indices):
                    if i - time_period < 0:
                        continue
                    
                    historical_idx = company_indices[i - time_period]
                
                    current_close = self.dataframe.at[current_idx, 'close']
                    historical_close = self.dataframe.at[historical_idx, 'close']
                
                    roc = (current_close - historical_close) / historical_close * 100
                    self.dataframe.at[current_idx, f'roc_{time_period}'] = roc
    
        logger.info("ROC Done")
        print("ROC DOne")
        print(self.dataframe.head(51))
        return self.dataframe

    # Momentum Indicators (RSI)
    def res(self):
        pass
        
    # Save dataframe to excel file
    def save_df_to_excel(self):
        self.dataframe.to_excel('df.xlsx')
        logger.info("Excel file saved")

if __name__ == "__main__":
    excuter = StockIndicators()
    excuter.sma()
    excuter.ema()
    excuter.macd()
    excuter.roc()
    excuter.save_df_to_excel()    