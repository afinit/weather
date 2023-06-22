from pydantic import BaseModel, Field

class BasicWeatherStats(BaseModel):
    temp: float | None
    feels_like: float | None = Field(None, alias="feelsLikeTemp")
    pressure: float | None
    humidity: float | None
    temp_min: float | None = Field(None, alias="tempMin")
    temp_max: float | None = Field(None, alias="tempMax")

    class Config:
        allow_population_by_field_name = True