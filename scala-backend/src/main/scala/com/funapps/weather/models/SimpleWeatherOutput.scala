package com.funapps.weather.models

import io.circe.generic.semiauto.deriveEncoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

case class SimpleWeatherOutput(
  weatherCondition: Option[String],
  temperatureSummary: String,
  temp: Option[Double],
  feelsLikeTemp: Option[Double]
)

object SimpleWeatherOutput {
  implicit val encoder = deriveEncoder[SimpleWeatherOutput]

  implicit def entityEncoder[F[_]]: EntityEncoder[F, SimpleWeatherOutput] = jsonEncoderOf
}