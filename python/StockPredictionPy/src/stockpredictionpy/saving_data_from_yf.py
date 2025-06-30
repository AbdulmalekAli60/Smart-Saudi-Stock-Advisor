import yfinance as yf
import psycopg2 
import pandas as pd
from datetime import datetime
import logging
from config.databaseConnInfo import USER, DATABASE, HOST, PASSWORD, PORT

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
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
        
        ticker = "7010.SR"
        company_id = 4
        
        today_date = datetime.today().strftime('%Y-%m-%d')
        logger.info(f"Fetching data from 2020-01-01 to {today_date}")
        
        try:
            logger.info(f"Processing ticker: {ticker}")
                
            stock_data = yf.download(
                tickers=ticker,
                interval="1d",
                start="2020-01-01",
                end=today_date, 
                progress=False 
            )
                
            self.dataFrame = stock_data
            
            if stock_data.empty:
                logger.warning(f"No data found for ticker: {ticker}")
                return
                 
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
                
            if records_to_insert:
                
                existing_dates = set()
                check_query = "SELECT data_date FROM historical_data WHERE company_id = %s"
                self.cur.execute(check_query, (company_id,))
                existing_dates = {row[0].strftime('%Y-%m-%d') for row in self.cur.fetchall()}
                
                new_records = []
                for record in records_to_insert:
                    date_str = record[1]  
                    if date_str not in existing_dates:
                        new_records.append(record)
                
                if new_records:
                    query = """
                        INSERT INTO historical_data (close, data_date, high, low, open, volume, company_id)
                        VALUES (%s, %s, %s, %s, %s, %s, %s)
                    """
                    
                    self.cur.executemany(query, new_records)
                    self.conn.commit()
                    
                    logger.info(f"Successfully inserted {len(new_records)} new records for {ticker}")
                    logger.info(f"Skipped {len(records_to_insert) - len(new_records)} duplicate records")
                else:
                    logger.info(f"All {len(records_to_insert)} records already exist for {ticker}")
                
        except Exception as e:
            logger.error(f"Error processing ticker {ticker}: {str(e)}")
            if self.conn:
                self.conn.rollback()
                    
        try:
            if self.cur is not None:
                self.cur.close()
            if self.conn is not None:
                self.conn.close()
            logger.info("Database connections closed")
        except Exception as e:
            logger.error(f"Error closing connections: {str(e)}")

if __name__ == "__main__":
    saver = SavingDataFromYf()
    saver.saving_yf_data_in_db()