from stock_indicators import StockIndicators
from read_data_from_db import ReadDataFromDB
import pandas as pd
class exp:
    def __init__(self):

        indicators = StockIndicators()
        full_data: pd.DataFrame = indicators.calculate_all_indicators()
        # print(full_data.head().reset_index(drop=True))

        db_reader  = ReadDataFromDB()
        row_data = db_reader.read_data()
        print(row_data.head().reset_index(drop=True))

if __name__ == "__main__":
    obj = exp()