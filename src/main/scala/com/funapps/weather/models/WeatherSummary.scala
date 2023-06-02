package com.funapps.weather.models

import cats.effect.Concurrent
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class WeatherSummary(
  id: Option[Int],
  main: String,
  description: Option[String],
  icon: Option[String]
)

object WeatherSummary {
  implicit val decoder = deriveDecoder[WeatherSummary]
  implicit def entityDecoder[F[_] : Concurrent]: EntityDecoder[F, WeatherSummary] = jsonOf
}
