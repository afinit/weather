from pydantic import BaseModel, Field

class Coordinates(BaseModel):
    lat: float = Field(alias='latitude')
    lon: float = Field(alias='longitude')

    class Config:
        allow_population_by_field_name = True
