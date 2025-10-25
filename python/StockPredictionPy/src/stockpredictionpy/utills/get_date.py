from datetime import timedelta
import pandas as pd

def get_date() -> tuple[str, str, str, str, bool, bool, str]:

    today: pd.Timestamp = pd.Timestamp.today().normalize()
    today_date: str = today.strftime('%Y-%m-%d')
    yesterday_date: str = (today - timedelta(days=1)).strftime('%Y-%m-%d')
    tomorrow_date: str = (today + timedelta(days=1)).strftime('%Y-%m-%d')

    days_since_thursday = (today.weekday() - 3 + 7) % 7
    thursday_date: str = (today - timedelta(days=days_since_thursday)).strftime('%Y-%m-%d')

    days_until_sunday = (6 - today.weekday() + 7) % 7
    sunday_date: str = (today + timedelta(days=days_until_sunday)).strftime('%Y-%m-%d')

    is_thursday: bool = today.weekday() == 3
    is_sunday: bool = today.weekday() == 6

    return today_date, yesterday_date, tomorrow_date, thursday_date, is_thursday, is_sunday, sunday_date