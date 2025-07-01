import psycopg2
import pandas as pd
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
            logger.info("Successfully read data and stored in data frame")
            print(self.dataframe.head())
        
        except Exception as e:
            logger.exception(f": failed to get data fro database {str(e)}")
            return pd.DataFrame()
        finally:
            if self.conn is not None:
                self.conn.close()
    
        return self.dataframe
    
if __name__ == "__main__":
    reader = ReadDataFromDB()
    reader.read_data()

