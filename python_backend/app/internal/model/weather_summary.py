from pydantic import BaseModel

class WeatherSummary(BaseModel):
    id: int | None
    main: str
    description: str | None
    icon: str | None
