package com.funapps.weather.models

import cats.effect.Concurrent
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class WeatherResponse(
  weather: Vector[WeatherSummary],
  main: BasicWeatherStats
)

object WeatherResponse {
  implicit val decoder = deriveDecoder[WeatherResponse]
  implicit def entityDecoder[F[_]: Concurrent]: EntityDecoder[F, WeatherResponse] = jsonOf
}
