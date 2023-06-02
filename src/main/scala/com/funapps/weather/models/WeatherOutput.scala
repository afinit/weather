package com.funapps.weather.models

import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class WeatherOutput(
  weatherCondition: Option[String],
  temperatureSummary: String,
  temp: Option[Double],
  feelsLikeTemp: Option[Double]
)

object WeatherOutput {
  implicit val encoder = deriveEncoder[WeatherOutput]

  implicit def entityEncoder[F[_]]: EntityEncoder[F, WeatherOutput] = jsonEncoderOf
}