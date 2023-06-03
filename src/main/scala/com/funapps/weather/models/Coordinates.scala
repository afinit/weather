package com.funapps.weather.models

import cats.effect.Concurrent
import cats.implicits.catsSyntaxEitherId
import io.circe.{Decoder, HCursor}
import io.circe.Decoder.Result
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class Coordinates(
  latitude: Double,
  longitude: Double
)

object Coordinates {
  implicit val decoder = new Decoder[Coordinates] {
    override final def apply(c: HCursor): Result[Coordinates] =
      for {
        latitude <- c.downField("lat").as[Double]
        longitude <- c.downField("lon").as[Double]
      } yield Coordinates(
        latitude = latitude,
        longitude = longitude
      )
  }
  implicit val encoder = deriveEncoder[Coordinates]
  implicit def entityDecoder[F[_] : Concurrent]: EntityDecoder[F, Coordinates] = jsonOf
  implicit def entityEncoder[F[_]]: EntityEncoder[F, Coordinates] = jsonEncoderOf

  final case class WeatherInputError(errorMessage: String) extends RuntimeException(errorMessage)

  private def makeException(input: String): Exception =
    WeatherInputError(s"Please provide latitude,longitude in the format: \"21.02,-10.001\". Input provided is invalid: ${input}")

  def fromString(s: String): Either[Exception, Coordinates] = {
    val splits = s.split(",")
    if (splits.length != 2) makeException(s).asLeft[Coordinates]
    else {
      try {
        val lat = splits(0).toDouble
        val long = splits(1).toDouble
        Coordinates(latitude = lat, longitude = long).asRight[Exception]
      } catch {
        case _: Exception =>
          makeException(s).asLeft[Coordinates]
      }
    }
  }
}
