package com.funapps.weather.models

import cats.effect.Concurrent
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class WeatherSummary(
  id: Option[Int],
  main: String,
  description: Option[String],
  icon: Option[String]
)

object WeatherSummary {
  implicit val decoder = deriveDecoder[WeatherSummary]
  implicit val encoder = deriveEncoder[WeatherSummary]
  implicit def entityDecoder[F[_] : Concurrent]: EntityDecoder[F, WeatherSummary] = jsonOf
  implicit def entityEncoder[F[_]]: EntityEncoder[F, WeatherSummary] = jsonEncoderOf
}
