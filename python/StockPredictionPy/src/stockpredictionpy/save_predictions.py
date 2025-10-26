import logging
import psycopg2
from config.connect_to_database import connect_to_database
from config.companies_list import companies
from utills.get_date import get_date

logger = logging.getLogger("save predictions module")
logger.setLevel(logging.INFO)

file_handler = logging.FileHandler('save_predictions.log', mode="w")

formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

if not logger.handlers:
    logger.addHandler(file_handler) 


class SavePredictions:
    def __init__(self):
        self.conn = connect_to_database()
        self.cur: psycopg2.extensions.cursor = None
        self.connect_to_db()

    def connect_to_db(self) -> None:
        if self.conn:
            try:
                self.cur = self.conn.cursor()
                logger.info("Database connected and cursor created successfully")
            except Exception as e:
                logger.error(f"Failed to create cursor: {str(e)}")
                self.conn = None
        else:
            logger.error("Failed to establish database connection")
    
    def insert_prediction(self, predictions_dict:dict[int, dict[str, bool | float]], last_rows:dict) -> None:
        if not predictions_dict:
            logger.info("There is no prediction dict")
            return
        
        successfully_inserted_predictions:list[int] = []
        failed_inserted_predictions:list[int] = []
        successfully_updated_predictions:list[int] = []
        failed_updates_predictions:list[int] = []
        
        try:
            for company_id, prediction_set in predictions_dict.items():
                insert_success = self.handle_insert(company_id, prediction_set)
                if insert_success:
                    successfully_inserted_predictions.append(company_id)
                else:
                    failed_inserted_predictions.append(company_id)
                
                update_success = self.handle_update(company_id, last_rows)
                if update_success:
                    successfully_updated_predictions.append(company_id)
                else:
                    failed_updates_predictions.append(company_id)
            
            self.handle_db_commit()
            
        except Exception as e:
            logger.exception(f"Transaction failed: {str(e)}")
            self.conn.rollback()
        finally:
            self.close_db()

        logger.info(f"Successfully inserted predictions: {len(successfully_inserted_predictions)}")
        logger.info(f"Failed to insert predictions: {len(failed_inserted_predictions)}")
        logger.info(f"Successfully updated predictions: {len(successfully_updated_predictions)}")
        logger.info(f"Failed to update predictions: {len(failed_updates_predictions)}")

    def handle_insert(self, company_id:int, pred_data:dict[str, bool | float]) -> bool:
        today_date, _, tomorrow_date, _, is_thursday, _, sunday_date = get_date()
        insert_query:str = """
                INSERT INTO prediction (
                actual_result, direction, expiration_date, 
                prediction_date, company_id, prediction
            ) VALUES (%s, %s, %s, %s, %s, %s);
            """
        
        insert_values:tuple[None, bool, str, str, int, float] = (
            None,
            bool(pred_data['direction']),
            sunday_date if is_thursday else tomorrow_date,
            today_date,
            int(company_id),
            float(pred_data['prediction'])
        )
        
        try:
            self.cur.execute(insert_query, insert_values)
            logger.info(f"Successfully inserted prediction for company {company_id}")
            return True
        except Exception as e:
            logger.exception(f'Failed to insert prediction. Company id: {company_id} Error: {str(e)}')
            return False

    def handle_update(self, company_id:int, last_rows: dict) -> bool:
        today_date, yesterday_date, _, thursday_date, _, is_sunday, _ = get_date()
        update_query:str = """
                UPDATE prediction
                SET actual_result = %s
                WHERE prediction_date = %s 
                AND company_id = %s
                AND actual_result IS NULL
                AND expiration_date = %s
            """
        
        update_values:tuple[float, str, int, str] = (
            float(last_rows[company_id]['close'].iloc[0]),
            thursday_date if is_sunday else yesterday_date,
            int(company_id),
            today_date
        )
        
        try:
            self.cur.execute(update_query, update_values)
            if self.cur.rowcount > 0:
                logger.info(f"Successfully updated prediction for company {company_id}")
                return True
            else:
                logger.info(f"No previous prediction found to update for company {company_id}")
                return True
        except Exception as e:
            logger.exception(f"Failed to update previous prediction for company: {company_id}")
            return False

    def close_db(self) -> None:
        if self.cur is not None:
            self.cur.close()
            logger.info("Cursor is closed")
        if self.conn is not None:
            self.conn.close()
            logger.info("Connection is closed")

    def handle_db_commit(self) -> bool:
        try:
            self.conn.commit()
            logger.info("Transaction Done")
            return True
        except Exception as e:
            logger.exception(f"Transaction Failed: {str(e)}")
            self.conn.rollback()
            return False