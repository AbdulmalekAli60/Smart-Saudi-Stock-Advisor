import pandas as pd
import read_data_from_db
import logging
logging.basicConfig(level=logging.INFO, filename="stock_idicators.log", filemode="w", format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)
class StockIndicators:
    def __init__(self):
        
        data_reader = read_data_from_db.ReadDataFromDB()
        self.dataframe = data_reader.read_data()
        self.companies_id = [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]

    # trend indicators
    def sma(self):
        time_periods = [5, 10, 20, 50]
        
        for time_period in time_periods:
            sma_col_names = f"sma_{time_period}"
            self.dataframe[sma_col_names] = self.dataframe.groupby('company_id')['close'].rolling(window=time_period).mean().reset_index(0, drop=True)

        logger.info("sma done")
        print("SMA  DONE")
        print(self.dataframe.head(51))
        return self.dataframe

    # Exponential Moving Average (EMA) 
    # https://www.strike.money/technical-analysis/ema
    def ema(self):
        time_periods = [5, 10, 20, 50]
        
        self.dataframe = self.dataframe.sort_values(['company_id', 'data_date']).reset_index(drop=True)
        
        for time_period in time_periods:
            
            self.dataframe[f'ema_{time_period}'] = float('nan')
        
            for company_id in self.dataframe['company_id'].unique():
                
                company_mask = self.dataframe['company_id'] == company_id
                
                company_ema = self.dataframe.loc[company_mask, 'close'].ewm(span=time_period, adjust=False).mean()
                
                self.dataframe.loc[company_mask, f'ema_{time_period}'] = company_ema.values
        
        logger.info("EMA done")
        print(self.dataframe.head(51))
        return self.dataframe
    

    #Moving Average Convergence Divergence (MACD) 
    def macd(self):
        pass




if __name__ == "__main__":
    excuter = StockIndicators()
    excuter.sma()
    excuter.ema()
    