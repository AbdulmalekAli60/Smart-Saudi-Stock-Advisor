import psycopg2
import logging
from config.databaseConnInfo import DATABASE, HOST, PASSWORD, PORT, USER

logger = logging.getLogger('database_connection')
logger.setLevel(logging.INFO)

file_handler = logging.FileHandler("connect_to_db.log", mode="w")
file_handler.setLevel(logging.INFO)

formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

if not logger.handlers:
    logger.addHandler(file_handler)

def connect_to_database() -> psycopg2.extensions.connection | False:
        try:
            conn:psycopg2.extensions.connection = psycopg2.connect(
                database=DATABASE,
                user=USER,
                host=HOST,
                password=PASSWORD,
                port=PORT
            )
            logger.info("Successfully connected to database")
            return conn
        except Exception as e:
            logger.exception(f"Failed to connect to database: {str(e)}")
            return False