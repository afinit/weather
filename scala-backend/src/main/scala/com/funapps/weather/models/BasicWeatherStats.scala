package com.funapps.weather.models

import cats.effect.Concurrent
import io.circe.Decoder.Result
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Decoder, HCursor}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class BasicWeatherStats(
  temp: Option[Double],
  feelsLikeTemp: Option[Double],
  pressure: Option[Double],
  humidity: Option[Double],
  tempMin: Option[Double],
  tempMax: Option[Double]
)

object BasicWeatherStats {
  implicit val decoder = new Decoder[BasicWeatherStats] {
    override final def apply(c: HCursor): Result[BasicWeatherStats] =
      for {
        temp <- c.downField("temp").as[Option[Double]]
        feelsLike <- c.downField("feels_like").as[Option[Double]]
        pressure <- c.downField("pressure").as[Option[Double]]
        humidity <- c.downField("humidity").as[Option[Double]]
        tempMin <- c.downField("temp_min").as[Option[Double]]
        tempMax <- c.downField("temp_max").as[Option[Double]]
      } yield BasicWeatherStats(
        temp = temp,
        feelsLikeTemp = feelsLike,
        pressure = pressure,
        humidity = humidity,
        tempMin = tempMin,
        tempMax = tempMax,
      )
  }
  implicit val encoder = deriveEncoder[BasicWeatherStats]
  implicit def entityDecoder[F[_] : Concurrent]: EntityDecoder[F, BasicWeatherStats] = jsonOf
  implicit def entityEncoder[F[_]]: EntityEncoder[F, BasicWeatherStats] = jsonEncoderOf
}
