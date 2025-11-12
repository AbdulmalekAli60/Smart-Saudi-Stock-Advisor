import read_data_from_db
import logging
import numpy as np
import pandas as pd
logger = logging.getLogger('stock_indicators module')

logger.setLevel(logging.INFO)

file_handler = logging.FileHandler('stock_indicators.log',mode="w")

formatter = logging.Formatter('%(asctime)s - %(levelname)s - %(message)s')
file_handler.setFormatter(formatter)

if not logger.handlers:
    logger.addHandler(file_handler)

class StockIndicators:
    def __init__(self):
        data_reader = read_data_from_db.ReadDataFromDB()
        self.dataframe = data_reader.read_data()
        self.dataframe = self.dataframe.set_index('data_id')
        self.dataframe = self.dataframe.sort_values(['company_id', 'data_date']).reset_index(drop=True)
        logger.info("Data has been sorted")
        print("Data has been sorted")

    # Trend Indicators    
    def sma(self) -> pd.DataFrame:
        #time_periods = [5 ,10, 12, 20, 26, 50] # original
        # time_periods:list[int] = [5 ,15, 30]
        time_periods:list[int] = [20]

        for time_period in time_periods:
            self.dataframe[f'SMA_{time_period}'] = (
            self.dataframe
            .groupby('company_id')['close']
            .rolling(window=time_period, min_periods=time_period)
            .mean()
            .reset_index(level=0, drop=True)
            )

        logger.info("sma done")
        print("SMA  DONE")
        return self.dataframe    
    
    # Exponential Moving Average (EMA)     
    def ema(self) -> pd.DataFrame:
        # time_periods = [5, 12, 20 ,26, 50] # original      
        time_periods:list[int] = [9, 12, 26]      

        for time_period in time_periods:
            self.dataframe[f'EMA_{time_period}'] = (
                self.dataframe.groupby('company_id')['close']
                .ewm(span=time_period, adjust=False)
                .mean()
                .reset_index(level=0, drop=True)
            )

        logger.info("EMA done")
        print("EMA done")
        return self.dataframe
    
    #Moving Average Convergence Divergence (MACD) 
    def macd(self) -> pd.DataFrame:
        self.dataframe['macd_line'] = self.dataframe['EMA_12'] - self.dataframe['EMA_26']

        self.dataframe['signal_line'] = (
            self.dataframe.groupby('company_id')['macd_line']
            .ewm(span=9, adjust=False)
            .mean()
            .reset_index(level=0, drop=True)
        )
        
        self.dataframe['MACD'] = self.dataframe['macd_line'] - self.dataframe['signal_line']
        self.dataframe.drop(['signal_line', 'macd_line'], axis=1, inplace= True)
       
        logger.info("MACD Done")
        print("MACD Done")
        return self.dataframe

    # Price Rate of Change (ROC) 
    def roc(self) -> pd.DataFrame:
        time_periods:list[int] = [5, 10]
        
        for time_period in time_periods:
            self.dataframe[f'ROC_{time_period}'] = (
                self.dataframe.groupby('company_id')['close']
                .pct_change(periods=time_period)
                .reset_index(level=0, drop=True)
            ).multiply(100)
        
        logger.info("ROC Done")
        print("ROC Done")
        return self.dataframe

    # Momentum Indicators (RSI)
    def rsi(self) -> pd.DataFrame:
        self.dataframe['RSI'] = np.nan
        self.dataframe['change'] = self.dataframe.groupby('company_id')['close'].diff()

        time_period:int = 14 

        self.dataframe['gain'] = self.dataframe['change'].where(self.dataframe['change'] > 0, other = 0)
        self.dataframe['loss'] = self.dataframe['change'].where(self.dataframe['change'] < 0, other = 0).abs()

        avg_gain = (
            self.dataframe.groupby('company_id')['gain']
            .rolling(window=time_period, min_periods=time_period)
            .mean()
            .reset_index(level=0, drop=True)
            )
        avg_loss = (
            self.dataframe.groupby('company_id')['loss']
            .rolling(window=time_period, min_periods=time_period)
            .mean()
            .reset_index(level=0, drop=True)
            )
        
        rs = avg_gain / avg_loss.replace(0, np.nan)

        rsi = 100 - (100 / (1 + rs))

        self.dataframe['RSI'] = rsi

        self.dataframe.drop(['change', 'gain', 'loss'], axis=1, inplace=True)
        print("RSI Done")
        logger.info("RSI Done")
        return self.dataframe

    # Momentum Indicators (Stochastic Oscillator)
    def stochastic_oscillator(self) -> pd.DataFrame:
        time_period:int = 14

        self.dataframe['Stochastic_Oscillator_Fast'] = np.nan
        self.dataframe['Stochastic_Oscillator_Slow'] = np.nan
    
        lowest_low = ( 
             self.dataframe
            .groupby('company_id')['low']
            .rolling(window=time_period, min_periods=time_period)
            .min()
            .reset_index(level=0, drop=True)
            )
        
        highest_high = (
            self.dataframe
            .groupby('company_id')['high']
            .rolling(window=time_period, min_periods=time_period)
            .max()
            .reset_index(level=0, drop=True)  
        )
    
        stochastic_k = ((self.dataframe['close'] - lowest_low) / (highest_high - lowest_low)) * 100
        
        stochastic_d = (
            stochastic_k
            .groupby(self.dataframe['company_id'])
            .rolling(window=3, min_periods=3)
            .mean()
            .reset_index(level=0, drop=True)
        )
        
        self.dataframe['Stochastic_Oscillator_Fast'] = stochastic_k.values
        self.dataframe['Stochastic_Oscillator_Slow'] = stochastic_d.values
    
        print("Stochastic Oscillator Done")
        logger.info("Stochastic Oscillator Done")
        return self.dataframe
    
    # Momentum Indicators (Williams %R)
    def williams_R(self) -> pd.DataFrame:
        time_period:int = 14

        highest_high = (
            self.dataframe
            .groupby('company_id')['high']
            .rolling(window=time_period, min_periods=time_period)
            .max()
            .reset_index(level=0, drop=True)
         )

        lowest_low = (
            self.dataframe
            .groupby('company_id')['low']
            .rolling(window=time_period, min_periods=time_period)
            .min()
            .reset_index(level=0, drop=True))
        
        williams_r = ((highest_high - self.dataframe['close']) / (highest_high - lowest_low)) * -100

        self.dataframe['Williams_R'] = williams_r
        
        logger.info("Williams %R Done")
        print("Williams %R Done")
        return self.dataframe
    
    # Volatility Indicators (Bollinger Bands)
    def bollinger_bands(self) -> pd.DataFrame:

        self.dataframe['Middle_Band'] = self.dataframe['SMA_20']
        
        standard_deviation = (
            self.dataframe.groupby('company_id')['close']
            .rolling(window=20, min_periods=20)
            .std()
            .multiply(2)
            .reset_index(level=0, drop=True)    
        )

        self.dataframe['Upper_Band'] = self.dataframe['SMA_20'] + standard_deviation

        self.dataframe['Lower_Band'] = self.dataframe['SMA_20'] - standard_deviation
        

        self.dataframe['Band_Width'] = (
            (self.dataframe['Upper_Band'] - self.dataframe['Lower_Band']) / 
            self.dataframe['Middle_Band'].replace(0, np.nan)
        )

        self.dataframe['BB_Percent'] = (
             (self.dataframe['close'] - self.dataframe['Lower_Band']) / 
             (self.dataframe['Upper_Band'] - self.dataframe['Lower_Band']).replace(0, np.nan)
            )

        self.dataframe['Price_to_Upper'] = self.dataframe['close'] / self.dataframe['Upper_Band'].replace(0, np.nan)
        self.dataframe['Price_to_Lower'] = self.dataframe['close'] / self.dataframe['Lower_Band'].replace(0, np.nan)

        logger.info("Bollinger Bands Done")
        print("Bollinger Bands Done")
        return self.dataframe
    
    # Average True Range (ATR)
    def atr(self) -> pd.DataFrame:
        time_period:int = 14

        self.dataframe['Previous_Close'] = self.dataframe.groupby('company_id')['close'].shift(1)
        
        hml = (self.dataframe['high'] - self.dataframe['low'])

        hmpc = (np.absolute(self.dataframe['high'] - self.dataframe['Previous_Close']))

        lmpc = (np.absolute(self.dataframe['low'] - self.dataframe['Previous_Close']))

        self.dataframe['true_range'] = np.maximum(hml, np.maximum(hmpc, lmpc))
        
        self.dataframe['ATR'] = (
            self.dataframe.groupby('company_id')['true_range']
            .rolling(window=time_period, min_periods=time_period)
            .mean()
            .reset_index(level=0, drop=True)
        )

        self.dataframe.drop(['Previous_Close', 'true_range'], axis=1, inplace=True)

        logger.info("ATR Done")
        print("ATR Done")
        return self.dataframe

    # Volume Indicators (On-Balance Volume (OBV))
    def obv(self) -> pd.DataFrame:
        self.dataframe['OBV'] = np.nan 
        self.dataframe.loc[self.dataframe.groupby('company_id').cumcount() == 0, 'OBV'] = 0 # make first OBV value of each group as 0

        for company_id in self.dataframe['company_id'].unique():
            company_mask = self.dataframe['company_id' ] == company_id
            company_data = self.dataframe[company_mask]

            for i in range(1, len(company_data)):
                current_index = company_data.index[i]
                previous_index = company_data.index[i - 1]

                if self.dataframe.at[current_index, 'close'] > self.dataframe.at[previous_index, 'close']:
                    self.dataframe.at[current_index, 'OBV'] = self.dataframe.at[previous_index, 'OBV'] + self.dataframe.at[current_index, 'volume']
                
                elif self.dataframe.at[current_index, 'close'] < self.dataframe.at[previous_index, 'close']:
                    self.dataframe.at[current_index, 'OBV'] = self.dataframe.at[previous_index, 'OBV'] - self.dataframe.at[current_index, 'volume']
                
                else:
                    self.dataframe.at[current_index, 'OBV'] = self.dataframe.at[previous_index, 'OBV']

        logger.info("OBV Done")
        print("OBV Done")
        return self.dataframe

    # Volume Indicators, Volume Moving Average
    def volume_moving_avg(self) -> pd.DataFrame:
        self.dataframe['Volume_Moving_Avg'] = np.nan
        time_period:int = 10

        self.dataframe['Volume_Moving_Avg'] = (

            self.dataframe.groupby('company_id')['volume']
            .rolling(window = time_period, min_periods = time_period)
            .mean()
            .reset_index(level = 0, drop = True)
        )

        logger.info("Volume Moving Average Done")
        print("Volume Moving Average Done")
        return self.dataframe 
    
    # Support/Resistance Indicators (Pivot Points)
    def pivot_points(self) -> pd.DataFrame:
        
        self.dataframe['Pivot_Point'] = np.nan
        self.dataframe['Support_1'] = np.nan
        self.dataframe['Support_2'] = np.nan
        self.dataframe['Support_3'] = np.nan
        self.dataframe['Resistance_1'] = np.nan
        self.dataframe['Resistance_2'] = np.nan
        self.dataframe['Resistance_3'] = np.nan
        
        self.dataframe['Pivot_Points'] = (
            (self.dataframe['high'] + self.dataframe['low'] + self.dataframe['close']) / 3
        )
        
        self.dataframe['Support_1'] = (
            (2 * self.dataframe['Pivot_Points']) - self.dataframe['high']
        )
        
        self.dataframe['Support_2'] = (
            self.dataframe['Pivot_Points'] - (self.dataframe['high'] - self.dataframe['low'])
        )
        
        self.dataframe['Support_3'] = (
            self.dataframe['low'] - 2 * (self.dataframe['high'] - self.dataframe['Pivot_Points'])
        )
        
        self.dataframe['Resistance_1'] = (
            (2 * self.dataframe['Pivot_Points']) - self.dataframe['low']
        )
        
        self.dataframe['Resistance_2'] = (
            self.dataframe['Pivot_Points'] + (self.dataframe['high'] - self.dataframe['low'])
        )
        
        self.dataframe['Resistance_3'] = (
            self.dataframe['high'] + 2 * (self.dataframe['Pivot_Points'] - self.dataframe['low'])
        )
        
        logger.info("Pivot Points Done")
        print("Pivot Points Done")
        return self.dataframe
    

    # Custom Ratio Indicators (Price to MA Ratio)
    def price_to_ma_ratio(self) -> pd.DataFrame:
        self.dataframe['Price_to_MA_Ratio'] = np.nan

        self.dataframe['Price_to_MA_Ratio'] = np.where(
        (self.dataframe['SMA_20'] == 0) | (self.dataframe['SMA_20'].isna()) | (self.dataframe['close'].isna()),
        np.nan,
        self.dataframe['close'] / self.dataframe['SMA_20']
    )

        logger.info("Price to MA Ratio Done")
        print("Price to MA Ratio Done")
        return self.dataframe
    
    # Custom Ratio Indicators (MA Crossovers)
    def ma_crossover(self) -> pd.DataFrame:
        self.dataframe['SMA5_SMA20_Ratio'] = np.nan
        self.dataframe['EMA5_EMA20_Ratio'] = np.nan

        self.dataframe['SMA5_SMA20_Ratio'] = np.where(
            (self.dataframe['SMA_20'] == 0) | (self.dataframe['SMA_20'].isna()) | (self.dataframe['SMA_5'].isna()),
            np.nan,
            self.dataframe['SMA_5'] / self.dataframe['SMA_20']
        )

        self.dataframe['EMA5_EMA20_Ratio'] = np.where(
            (self.dataframe['EMA_20'] == 0) | (self.dataframe['EMA_20'].isna()) | (self.dataframe['EMA_5'].isna()),
            np.nan,
            self.dataframe['EMA_5'] / self.dataframe['EMA_20']
        )

        logger.info("MA Crossover Done")
        print("MA Crossover Done")
        return self.dataframe

    #Custom Ratio Indicators (High-Low Range Ratio)
    def high_low_range_ratio(self)-> pd.DataFrame:
        self.dataframe['High_Low_Range_Ratio'] = np.nan

        self.dataframe['High_Low_Range_Ratio'] = np.where(
            (self.dataframe['close'] == 0) | (self.dataframe['close'].isna()) | (self.dataframe['high'].isna()) | (self.dataframe['low'].isna()),
            np.nan,
            (self.dataframe['high'] - self.dataframe['low']) / self.dataframe['close']
        )

        logger.info("High-Low Range Ratio Done")
        print("High-Low Range Ratio Done")
        return self.dataframe

    # Save dataframe to excel file
    def save_df_to_excel(self) -> None:
        self.dataframe.to_excel('df.xlsx', index=False)
        logger.info("Excel file saved")
        print("Excel file saved")

    def calculate_all_indicators(self) ->  pd.DataFrame:
        try:  
            self.ema()
            self.sma()
            self.macd()
            self.roc()
            self.rsi()
            # self.stochastic_oscillator()
            # self.williams_R()
            # self.bollinger_bands()
            # self.atr()
            # self.obv()
            # self.volume_moving_avg()
            self.pivot_points()
            self.price_to_ma_ratio()
            # self.ma_crossover()
            # self.high_low_range_ratio()
            
            return self.dataframe
        
        except Exception as e:
            logger.error(f"Error happened while calculating the indicators: {str(e)}")
            print(f"Error happened while calculating the indicators: {str(e)}")
            

        print("All indicators has been calculated")
        logger.info("All indicators has been calculated")
        return self.dataframe    

if __name__ == "__main__":
    stock_indicators = StockIndicators()
    stock_indicators.calculate_all_indicators()
    # stock_indicators.save_df_to_excel()
