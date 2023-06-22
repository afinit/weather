from pydantic import BaseModel

class WindData(BaseModel):
    speed: float | None
    deg: float | None
    gust: float | None