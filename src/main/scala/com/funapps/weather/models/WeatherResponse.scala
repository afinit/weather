package com.funapps.weather.models

import cats.effect.Concurrent
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class WeatherResponse(
  weather: Vector[WeatherSummary],
  main: BasicWeatherStats
)

object WeatherResponse {
  implicit val decoder = deriveDecoder[WeatherResponse]
  implicit val encoder = deriveEncoder[WeatherResponse]
  implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, WeatherResponse] = jsonOf
  implicit def entityEncoder[F[_]]: EntityEncoder[F, WeatherResponse] = jsonEncoderOf
}
