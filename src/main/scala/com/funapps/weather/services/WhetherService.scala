package com.funapps.weather.services

import cats.MonadThrow
import cats.implicits._
import com.funapps.weather.models.{BasicWeatherStats, WeatherOutput, WeatherResponse}

trait WeatherService[F[_]] {

  def getWeather(
    weatherResponse: WeatherResponse
  ): F[WeatherOutput]

}

object WeatherService {
  def build[F[_]: MonadThrow]: WeatherService[F] = new WeatherServiceImpl[F]

}

class WeatherServiceImpl[F[_]: MonadThrow] extends WeatherService[F] {

  // based on fahrenheit temps. this isn't ideal, but this is just a bit of a toy
  private def convertWeatherToSummary(basicWeatherStats: BasicWeatherStats): String = basicWeatherStats.feelsLikeTemp.map { feels_like =>
    if (feels_like > 80) "Fan Weather"
    else if (feels_like > 60) "Nice Weather"
    else if (feels_like > 45) "Pants Weather"
    else if (feels_like > 32) "Jacket Weather"
    else "Coat Weather"
  }.getOrElse("No Weather")

  override def getWeather(weatherResponse: WeatherResponse): F[WeatherOutput] = {
    WeatherOutput(
      weatherCondition = weatherResponse.weather.headOption.map(_.main),
      temperatureSummary = convertWeatherToSummary(weatherResponse.main),
      temp = weatherResponse.main.temp,
      feelsLikeTemp = weatherResponse.main.feelsLikeTemp
    ).pure[F]
  }
}
