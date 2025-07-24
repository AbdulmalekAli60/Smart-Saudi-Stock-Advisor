import yfinance as yf
import pandas as pd
from curl_cffi import requests
from datetime import  timedelta
import logging
from config.companies_list import companies
from config.connect_to_database import connect_to_database

logger = logging.getLogger('yfinance_saver module')
logger.setLevel(logging.INFO)

file_handler = logging.FileHandler("saving_data_from_yf.log", mode="w")

formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

if not logger.handlers:
    logger.addHandler(file_handler)

class SavingDataFromYf:
    def __init__(self):
        self.conn = connect_to_database()
        self.cur = None

        if self.conn:
            try:
                self.cur = self.conn.cursor()
                logger.info("Database connected and cursor created successfully")
            except Exception as e:
                logger.error(f"Failed to create cursor: {str(e)}")
                self.conn = None
        else:
            logger.error("Failed to establish database connection")
                        
    def saving_yf_data_in_db(self):
        logger.info("Starting data fetching process...")
        
        if not self.conn:
            logger.error("Could not connect to database. Exiting.")
            return 
        
        session = requests.Session(impersonate="chrome")
        
        today = pd.Timestamp.today().normalize()
        today_date = today.strftime('%Y-%m-%d')
        yesterday_date = (today - timedelta(days=1)).strftime('%Y-%m-%d')
        tomorrow_date = (today + pd.Timedelta(days=1)).strftime('%Y-%m-%d')
        
        logger.info(f"Fetching today's data for date: {today_date}")
        logger.info(f"Fetching today's data for date: {today_date} tomowro: {tomorrow_date}")
        
        inserted_tickers = []
        failed_inserted_tickers = []
        
        try:
            
            for company_id, ticker in companies.items():
               
                try:
                    stock_data = yf.download(
                        tickers=ticker,
                        interval="1d",
                        start= today,
                        end= tomorrow_date,
                        progress=False,
                        session=session,
                        auto_adjust=True
                    )
                    
                    if stock_data.empty:
                        logger.warning(f"No data found for ticker: {ticker}")
                        failed_inserted_tickers.append(ticker)
                        continue
                    
                    logger.info(f"Found {len(stock_data)} records for {ticker}")
                    
                    records_to_insert = []
                    for date_index, row in stock_data.iterrows():
                        
                        record = (
                            row["Close"].item() if hasattr(row["Close"], 'item') else float(row["Close"]),
                            date_index.strftime('%Y-%m-%d'),
                            row["High"].item() if hasattr(row["High"], 'item') else float(row["High"]),
                            row["Low"].item() if hasattr(row["Low"], 'item') else float(row["Low"]),
                            row["Open"].item() if hasattr(row["Open"], 'item') else float(row["Open"]),
                            int(row["Volume"].item()) if hasattr(row["Volume"], 'item') else int(row["Volume"]),
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
                if session:
                    session.close()
                    logger.info("Session Closed")
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
        print("Process finished")
        logger.info("=" * 50)
        
    except Exception as e:
        logger.exception(f" error in main execution: {str(e)}")
        print(f" error: {str(e)}") 