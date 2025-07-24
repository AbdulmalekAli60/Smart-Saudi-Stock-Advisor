import psycopg2
import pandas as pd
import stock_indicators
from config.connect_to_database import connect_to_database
import logging

logger = logging.getLogger('database_reader module')
logger.setLevel(logging.INFO)

file_handler = logging.FileHandler("read_data_from_db.log", mode="w")

formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

if not logger.handlers:
    logger.addHandler(file_handler)


class ReadDataFromDB:
    def __init__(self):
        self.dataframe = pd.DataFrame()
        self.conn = connect_to_database()
            
    def read_data(self):
        if not self.conn:
            return pd.DataFrame()   

        try:
            query = """
                    Select * from historical_data
                    """

            self.dataframe = pd.read_sql(query, self.conn)
            logger.info(f"Successfully read data and stored in data frame")
            # print(f"Data read successfully, shape: {self.dataframe.shape}")
            # print(self.dataframe.head())

        except Exception as e:
            logger.exception(f": failed to get data fro database {str(e)}")
            print(f"Exception occurred: {str(e)}")
            self.dataframe = pd.DataFrame()
            
        finally:
            if self.conn is not None:
                self.conn.close()
                print("Database conncetion closed")

        print(f"Returning dataframe with shape: {self.dataframe.shape}")
        return self.dataframe
    
    # read_data_from_db.py
if __name__ == "__main__":
    print("=== Testing ReadDataFromDB ===")
    reader = ReadDataFromDB()
    df = reader.read_data()
    # print(f"Direct call result shape: {df.shape}")
    print("\n=== Testing StockIndicators ===")
    stock_indicators = stock_indicators.StockIndicators()
    stock_indicators.sma_5()

