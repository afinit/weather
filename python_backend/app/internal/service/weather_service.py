from ..model.weather_response import WeatherResponse
from ..model.coordinates import Coordinates
from ..model.errors import InvalidLatLongError, InternalError

import httpx
import os
import re
import logging

class WeatherService():
    def __init__(self):
        self.__api_key = os.environ.get('OPENWEATHERMAP_API_KEY')

    def __validate_latlong(self, latlong: str) -> Coordinates:
        try:
            pattern = "(?P<lat>^[-+]?(?:[1-8]?\d(?:\.\d*)?|90(?:\.0*)?))\s*,\s*(?P<lon>[-+]?(?:180(?:\.0*)?|(?:1[0-7]\d|[1-9]?\d)(?:\.\d*)?))$"
            m = re.match(pattern, latlong)
            if (m == None):
                error = InvalidLatLongError(latlong)
                logging.info(str(error))
                raise error

            lat = float(m.group("lat"))
            lon = float(m.group("lon"))

            return Coordinates(latitude=lat, longitude=lon)
        except ValueError:
            error = InternalError(f"Error converting lat,long to floats: {latlong}")
            logging.error(str(error))
            raise error

    def __build_url(self, coordinates: Coordinates):
        lat = coordinates.lat
        lon = coordinates.lon
        api_url = 'http://api.openweathermap.org/data/2.5/weather?lat={}&lon={}&units=imperial&appid={}'.format(lat, lon, self.__api_key)
        return api_url

    async def __fetch_weather_data(self, coordinates: Coordinates) -> WeatherResponse:
        async with httpx.AsyncClient() as client:
            url = self.__build_url(coordinates)

            response = await client.get(url)
            response.raise_for_status()  # Raise an exception for non-2xx status codes

            data = WeatherResponse.parse_raw(response.text)

            return data


    async def get_weather(self, latlong: str) -> WeatherResponse:
        coordinates = self.__validate_latlong(latlong)

        return await self.__fetch_weather_data(coordinates)
