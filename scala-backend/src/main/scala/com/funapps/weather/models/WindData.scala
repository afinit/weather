package com.funapps.weather.models

import cats.effect.Concurrent
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class WindData(
  speed: Option[Double],
  deg: Option[Double],
  gust: Option[Double]
)

object WindData {
  implicit val decoder = deriveDecoder[WindData]
  implicit val encoder = deriveEncoder[WindData]
  implicit def entityDecoder[F[_] : Concurrent]: EntityDecoder[F, WindData] = jsonOf
  implicit def entityEncoder[F[_]]: EntityEncoder[F, WindData] = jsonEncoderOf
}
