import React, { useEffect, useState } from 'react';
import WeatherDisplay from '../../components/WeatherDisplay';

export interface Coordinates {
    latitude: number;
    longitude: number;
}

export interface WeatherSummary {
    id: number;
    main: string;
    description: string;
    icon: string;
}

export interface BasicWeatherStats {
    temp: number;
    feelsLikeTemp: number;
    pressure: number;
    humidity: number;
    tempMin: number;
    tempMax: number;
}

export interface WeatherResponse {
    coord: Coordinates;
    weather: WeatherSummary[];
    main: BasicWeatherStats;
}

interface Props {
    coords: string
}

export default function WeatherContainer(props: Props) {
    const [weatherResponse, setWeatherResponse] = useState<WeatherResponse | null>(null);

    useEffect(() => {
        if (props.coords) {
            fetch(`http://localhost:8080/weather/${props.coords}`, {
                method: 'GET',
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json',
                },
            })
                .then((response) => response.json())
                .then((responseData: WeatherResponse) => {
                    setWeatherResponse(responseData);
                })
                .catch((error) => {
                    console.error('Error:', error);
                });
        } else {
            setWeatherResponse(null);
        }
    }, [props.coords]);

    if (weatherResponse) return <WeatherDisplay weatherResponse={weatherResponse} />;
    else return null;

}