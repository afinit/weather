# The Silly Weather App

Using the OpenWeatherMap api to get and display
weather data.

## Getting setup

To get started, you need an openweathermap appid. You can
get this by creating a free account, or I can provide
you with an appid.

Here is some information about it: https://openweathermap.org/appid

## Running the app

From the root of the scala project, run this replacing
`<APPID>` with the appid:

```bash
sbt "run <APPID>"
```

## Hitting the endpoints

Currently, there are 2 endpoints, and they are both
used the same way:

### Detailed Weather

For more detailed information about the weather, this endpoint
will return part of the data included in this API:
https://openweathermap.org/current

Provide the lat,long as part of the URL like this.
(TODO: maybe this should be a query param instead. Notes
for future me)
```
GET localhost:8080/weather/38.624399,-90.184242
```

### Simple Weather

This API provides a processed version of the above data.

```json
{
  "weatherCondition": "Clear",
  "temperatureSummary": "Fan Weather",
  "temp": 88.45,
  "feelsLikeTemp": 87.22
}
```

Provide the lat,long as part of the URL like this.
(TODO: maybe this should be a query param instead. Notes
for future me)
```
GET localhost:8080/simpleweather/38.624399,-90.184242
```
