import psycopg2
import logging
# from config.databaseConnInfo import DATABASE, HOST, PASSWORD, PORT, USER
import os
logger = logging.getLogger('database_connection')
logger.setLevel(logging.INFO)

file_handler = logging.FileHandler("connect_to_db.log", mode="w")
file_handler.setLevel(logging.INFO)

formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

if not logger.handlers:
    logger.addHandler(file_handler)

def connect_to_database() -> psycopg2.extensions.connection | None:
        
        db_host = os.getenv('DB_HOST', 'localhost')
        db_port = os.getenv('DB_PORT', '5432')
        db_name = os.getenv('DB_NAME', 'SmartSaudiStockAdvisor')
        db_user = os.getenv('DB_USER', 'postgres')
        db_password = os.getenv('DB_PASSWORD', 'Afsd1423')

        try:
            conn = psycopg2.connect(
                host=db_host,
                port=db_port,
                dbname=db_name,
                user=db_user,
                password=db_password
            )
            logger.info("Successfully connected to database")
            return conn
        except Exception as e:
            logger.exception(f"Failed to connect to database: {str(e)}")
            return None