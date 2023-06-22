from fastapi import FastAPI
from .routers import weather
from .internal.service.weather_service import WeatherService
import logging
import sys

logging.basicConfig(stream=sys.stdout, level=logging.DEBUG,
                    format="%(asctime)s - %(levelname)s - %(message)s")

app = FastAPI()

app.include_router(weather.router)

@app.get("/")
async def root():
    return {"message": "Hello World"}