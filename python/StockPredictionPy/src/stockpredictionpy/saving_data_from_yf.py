import yfinance as yf
import psycopg2 
import pandas as pd
from datetime import datetime
import logging
from config.databaseConnInfo import USER, DATABASE, HOST, PASSWORD, PORT
from config.companies_list import companies

logging.basicConfig(
    level=logging.INFO, 
    filename="saving_data_from_yf.log", 
    filemode="w", 
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

class SavingDataFromYf:
    def __init__(self):
        self.conn = None
        self.cur = None
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
            logger.error(f"Failed to connect to database: {str(e)}")
            return False
                
    def saving_yf_data_in_db(self):
        logger.info("Starting data fetching process...")
        
        if not self.connect_to_database():
            logger.error("Could not connect to database. Exiting.")
            return 
        
        today_date = datetime.today().strftime('%Y-%m-%d')
        tomorrow_date = (datetime.today() + pd.Timedelta(days=1)).strftime('%Y-%m-%d')
        logger.info(f"Fetching today's data for date: {today_date}")
        
        inserted_tickers = []
        failed_inserted_tickers = []
        
        try:
            for company_id, ticker in self.companies.items():
                logger.info(f"Processing company_id: {company_id}, ticker: {ticker}")
                
                try:
                    stock_data = yf.download(
                        tickers=ticker,
                        interval="1d",
                        start=today_date,
                        end=tomorrow_date,
                        progress=False 
                    )
                    
                    if stock_data.empty:
                        logger.warning(f"No data found for ticker: {ticker}")
                        failed_inserted_tickers.append(ticker)
                        continue
                    
                    logger.info(f"Found {len(stock_data)} records for {ticker}")
                    
                    records_to_insert = []
                    for date_index, row in stock_data.iterrows():
                        
                        record = (
                            float(row["Close"]),
                            date_index.strftime('%Y-%m-%d'),
                            float(row["High"]),
                            float(row["Low"]),
                            float(row["Open"]),
                            int(row["Volume"]),
                            company_id
                        )
                        records_to_insert.append(record)
                    
                    if records_to_insert:
                        query = """
                            INSERT INTO historical_data (close, data_date, high, low, open, volume, company_id)
                            VALUES (%s, %s, %s, %s, %s, %s, %s)

                        """
                        
                        self.cur.executemany(query, records_to_insert)
                        self.conn.commit()
                        inserted_tickers.append(ticker)
                        logger.info(f"Successfully inserted {len(records_to_insert)} records for ticker: {ticker}")
                    else:
                        logger.warning(f"No records to insert for ticker: {ticker}")
                        failed_inserted_tickers.append(ticker)
                        
                except Exception as e:
                    logger.exception(f"Error processing ticker {ticker}: {str(e)}")
                    failed_inserted_tickers.append(ticker)
                    if self.conn:
                        self.conn.rollback()

        except Exception as e:
            logger.exception(f"Unexpected error in main loop: {str(e)}")
            
        finally:
            try:
                if self.cur is not None:
                    self.cur.close()
                if self.conn is not None:
                    self.conn.close()
                logger.info("Database connections closed")
            except Exception as e:
                logger.exception(f"Error closing connections: {str(e)}")
        
        logger.info(f"Process completed - Successfully processed: {len(inserted_tickers)}, Failed: {len(failed_inserted_tickers)}")
        if failed_inserted_tickers:
            logger.info(f"Failed tickers: {failed_inserted_tickers}")

if __name__ == "__main__":
    try:
        logger.info("=" * 50)
        logger.info("Starting  yFinance data fetching")
        logger.info("=" * 50)
        
        saver = SavingDataFromYf()
        saver.saving_yf_data_in_db()
        
        logger.info("=" * 50)
        logger.info("Process finished")
        logger.info("=" * 50)
        
    except Exception as e:
        logger.exception(f" error in main execution: {str(e)}")
        print(f" error: {str(e)}") 