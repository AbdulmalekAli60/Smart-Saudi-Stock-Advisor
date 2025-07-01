import yfinance as yf
import psycopg2 
import pandas as pd
from datetime import datetime
import logging
from config.databaseConnInfo import USER, DATABASE, HOST, PASSWORD, PORT
from config.companies_list import companies
logging.basicConfig(level=logging.INFO, filename="saving_data_from_yf.log", filemode="w", format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)


class SavingDataFromYf:
    def __init__(self):
        self.conn = None
        self.cur = None
        self.dataFrame = pd.DataFrame()
        self.companies = {
            2: "7203.SR", 
            3: "2223.SR", 
            4: "7010.SR", 
            5: "1180.SR", 
            6: "2020.SR", 
            7: "2280.SR", 
            8: "1120.SR", 
            9: "1010.SR", 
            10: "2082.SR", 
            11: "4280.SR", 
            12: "1111.SR", 
            13: "4013.SR", 
            14: "1211.SR", 
            15: "8210.SR", 
            16: "8010.SR"
        }
        
    def connect_to_database(self):
        try:
            self.conn = psycopg2.connect(
                database=DATABASE,
                user=USER,
                host=HOST,
                password=PASSWORD,
                port=PORT
            )
            self.cur = self.conn.cursor()
            logger.info("Database connected successfully")
            return True
        except Exception as e:
            logger.error(f"Failed to connect: {str(e)}")
            return False
                
    def saving_yf_data_in_db(self):
        if not self.connect_to_database():
            return 
        
        # ticker = "8010.SR" #insurrance
        # company_id = 16
        
        today_date = datetime.today().strftime('%Y-%m-%d')
        logger.info(f"Fetching data today's data:  {today_date}")
        inserted_tickers = []
        falied_inserted_tickers = []
        try:
            logger.info(f"Processing ticker: {ticker}")
            
            for company_id, ticker in self.companies.items():
                stock_data = yf.download(
                    tickers=ticker,
                    interval="1d",
                    start=today_date, # jsut today data the new data
                    # end=today_date, 
                    progress=False 
                )
                
                self.dataFrame = stock_data
            
                if stock_data.empty:
                    logger.warning(f"No data found for ticker: {ticker}")
                    falied_inserted_tickers.append(ticker)
                    continue
            
               
                records_to_insert = []
                for index, row in self.dataFrame.iterrows():
                
                    record = (
                        float(row["Close"].iloc[0]) if hasattr(row["Close"], 'iloc') else float(row["Close"]),
                        index.strftime('%Y-%m-%d'),
                        float(row["High"].iloc[0]) if hasattr(row["High"], 'iloc') else float(row["High"]),
                        float(row["Low"].iloc[0]) if hasattr(row["Low"], 'iloc') else float(row["Low"]),
                        float(row["Open"].iloc[0]) if hasattr(row["Open"], 'iloc') else float(row["Open"]),
                        int(row["Volume"].iloc[0]) if hasattr(row["Volume"], 'iloc') else int(row["Volume"]),
                        company_id
                    )
                    records_to_insert.append(record)
                
            
                query = """
                    INSERT INTO historical_data (close, data_date, high, low, open, volume, company_id)
                    VALUES (%s, %s, %s, %s, %s, %s, %s)
                    """
                    
                self.cur.executemany(query, records_to_insert)
                self.conn.commit()
                inserted_tickers.append(ticker)
                logger.info(f"Successfully inserted ticker: {ticker}")
                    
        except Exception as e:
            logger.exception(f"Error processing ticker {ticker}: {str(e)}")
            falied_inserted_tickers.append(ticker)
            if self.conn:
                self.conn.rollback()
        
        logger.info(f"Completed sucssfully: {len(inserted_tickers)}, failed: {len(falied_inserted_tickers)}")
                    
        try:
            if self.cur is not None:
                self.cur.close()
            if self.conn is not None:
                self.conn.close()
            logger.info("Database connections closed")
        except Exception as e:
            logger.exception(f"Error closing connections: {str(e)}")

if __name__ == "__main__":
    saver = SavingDataFromYf()
    saver.saving_yf_data_in_db()