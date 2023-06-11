import React from "react";
import { WeatherResponse } from "../../containers/WeatherContainer";
import { Card, Image } from "semantic-ui-react";
import "./index.css"

interface Props {
    weatherResponse: WeatherResponse;
}

export default function WeatherDisplay(props: Props) {

    let iconImg;
    let weatherTitle;
    let weatherDescription;
    if (props.weatherResponse.weather.length > 0) {
        const weather = props.weatherResponse.weather[0];
        const iconLink = `https://openweathermap.org/img/wn/${weather.icon}@4x.png`;
        iconImg = <Image src={iconLink}/>;
        weatherTitle=weather.main;
        weatherDescription=weather.description;
    };

    return (
        <div className="weather-display">
            <Card>
                {iconImg}
                <Card.Content textAlign="left">
                    <Card.Header>{props.weatherResponse.main.temp}°F</Card.Header>
                    <Card.Meta>feels like {props.weatherResponse.main.feelsLikeTemp}°F</Card.Meta>
                    <Card.Description>{weatherTitle}</Card.Description>
                    <Card.Meta>{weatherDescription}</Card.Meta>
                    <Card.Description>Humidity {props.weatherResponse.main.humidity}%</Card.Description>
                </Card.Content>
            </Card>
        </div>
    );
}