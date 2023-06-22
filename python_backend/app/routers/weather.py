from fastapi import APIRouter, HTTPException
from ..internal.model.weather_response import WeatherResponse
from ..internal.model.errors import InvalidLatLongError, InternalError
from ..internal.service.weather_service import WeatherService
import httpx


weatherService = WeatherService()

router = APIRouter()

@router.get("/weather/", tags=["weather"])
async def get_weather(latlong: str) -> WeatherResponse:
    try:
        data = await weatherService.get_weather(latlong)
        return data
    except httpx.HTTPStatusError as exc:
        raise HTTPException(status_code=exc.response.status_code, detail=str(exc))
    except httpx.RequestError as exc:
        raise HTTPException(status_code=500, detail=str(exc))
    except InvalidLatLongError as exc:
        raise HTTPException(status_code=400, detail=str(exc))
    except InternalError:
        raise HTTPException(status_code=500)

