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
    def sma_5(self):
        
        self.dataframe['sma_5'] = self.dataframe.groupby('company_id')['close'].rolling(window=5).mean().reset_index(0, drop=True)
        logger.info("sma 5 done")
        print("SMA 5 DONE")
        print(self.dataframe.head(15))
        return self.dataframe
    
    def sma_10(self):
        
        self.dataframe['sma_10'] = self.dataframe.groupby('company_id')['close'].rolling(window=10).mean().reset_index(0, drop=True)
        logger.info("sma 10 done")
        print("SMA 10 DONE")
        print(self.dataframe.head(15))
        return self.dataframe
    
    def sma_20(self):
        
        self.dataframe['sma_20'] = self.dataframe.groupby('company_id')['close'].rolling(window=20).mean().reset_index(0, drop=True)
        logger.info("sma 20 done")
        print("SMA 20 DONE")
        print(self.dataframe.head(20))
        return self.dataframe
    
    def sma_50(self):
        
        self.dataframe['sma_50'] = self.dataframe.groupby('company_id')['close'].rolling(window=50).mean().reset_index(0, drop=True)
        logger.info("sma 5 done")
        print("SMA 50 DONE")
        print(self.dataframe.head(51))
        return self.dataframe

    # Exponential Moving Average (EMA) 
    def ema(self):
        pass

if __name__ == "__main__":
    excuter = StockIndicators()
    excuter.sma_5()
    excuter.sma_10()
    excuter.sma_20()
    excuter.sma_50()