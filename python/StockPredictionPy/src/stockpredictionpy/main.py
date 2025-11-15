from saving_data_from_yf import SavingDataFromYf 
from model import XgBoostModel
from save_predictions import SavePredictions

import logging

logger = logging.getLogger('main module')
logger.setLevel(logging.INFO)

file_handler = logging.FileHandler("main.log", mode="w")

formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

if not logger.handlers:
    logger.addHandler(file_handler)

class Mian: 
    def __init__(self):
        try:
            logger.info("=" * 50)
            logger.info("Step 1:  fetching data from yf")
            logger.info("=" * 50)
            data_saver = SavingDataFromYf()
            data_saver.saving_yf_data_in_db()

            logger.info("=" * 50)
            logger.info("STEP 1: Finished data fetching.")
            logger.info("=" * 50)

            logger.info("=" * 50)
            logger.info("Step 2: call model for training")
            logger.info("=" * 50)

            model = XgBoostModel()
            predictions_dict = model.xgboost_model()

            logger.info("=" * 50)
            logger.info("Step 2: Finished trainig")
            logger.info("=" * 50)

            logger.info("=" * 50)
            logger.info("Step 3: Save new predictions in db")
            logger.info("=" * 50)

            db_saver = SavePredictions()
            db_saver.insert_prediction(
                predictions_dict = predictions_dict,
                last_rows= model.last_rows
            )

            logger.info("=" * 50)
            logger.info("Step 3: Finished saving in db")
            logger.info("=" * 50)

            logger.info("==== Process completed successfuly ===")
            print("==== Process completed successfuly ===")
            
        except Exception as e:
            logger.exception(f"Error: Check (main.log). Error: {str(e)}")
            print(f"Error: Check (main.log). Error: {str(e)}")

if __name__ ==  "__main__":
    s = Mian() 
        