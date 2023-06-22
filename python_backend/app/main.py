from fastapi import FastAPI
from .routers import weather
from .internal.service.weather_service import WeatherService
import logging
import sys
from fastapi.middleware.cors import CORSMiddleware

logging.basicConfig(stream=sys.stdout, level=logging.DEBUG,
                    format="%(asctime)s - %(levelname)s - %(message)s")

app = FastAPI()

origins = [
    "http://localhost:3000",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(weather.router)

@app.get("/")
async def root():
    return {"message": "Hello World"}