from pydantic import BaseModel

from .coordinates import Coordinates
from .weather_summary import WeatherSummary
from .basic_weather_stats import BasicWeatherStats
from .wind_data import WindData

class WeatherResponse(BaseModel):
    coord: Coordinates
    weather: list[WeatherSummary]
    main: BasicWeatherStats
    wind: WindData
