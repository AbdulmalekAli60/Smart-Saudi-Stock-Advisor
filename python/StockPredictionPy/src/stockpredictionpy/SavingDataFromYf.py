import yfinance as yf
import psycopg2 
from config.databaseConnInfo import DATABASE, HOST, PASSWORD, PORT, USER
import pandas as pd

class SavingDataFromYf:
    def __init__(self):
        self.conn = None
        self.cur = None
        self.dataFrame = pd.DataFrame()
        
    
    def saving_yf_data_in_db(self):
        try:
            self.conn = psycopg2.connect(
                database = DATABASE,
                user = USER,
                host = HOST,
                password = PASSWORD,
                port = PORT
            )
            self.cur = self.conn.cursor()
            
            for index, row in self.dataFrame.iterrows():
                query = """
                        insert into historical_data (close, data_date, high, low, open, volume, company_id)
                        VALUES
                        (%s, %s, %s, %s, %s, %s, %s)
                    """
                values = ()
        except Exception as e:
            print("Error cocurred" , str(e))
            self.conn.rollback()
        finally:
            if self.conn is not None:
                self.conn.close()
            if self.cur is not None:
                self.cur.close()

       
        




    