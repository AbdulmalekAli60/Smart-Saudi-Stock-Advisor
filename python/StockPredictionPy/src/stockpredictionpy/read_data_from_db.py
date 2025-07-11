import psycopg2
import pandas as pd
import stock_indicators
from config.databaseConnInfo import DATABASE, HOST, PASSWORD, PORT, USER
from config.companies_list import companies
import logging
logging.basicConfig(level=logging.INFO, filename="read_data_from_db.log", filemode="w", format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

class ReadDataFromDB:
    def __init__(self):
        self.dataframe = pd.DataFrame()
        self.conn = None
        
    def connect_to_database(self):
        try:
            self.conn = psycopg2.connect(
                database=DATABASE,
                user=USER,
                host=HOST,
                password=PASSWORD,
                port=PORT
            )
            logger.info("Successfully connected to database")
            return True
        except Exception as e:
            logger.exception(f"Failed to connect to database: {str(e)}")
            return False
    
    def read_data(self):
        if not self.connect_to_database():
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

