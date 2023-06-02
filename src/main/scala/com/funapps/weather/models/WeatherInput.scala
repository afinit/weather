package com.funapps.weather.models

import cats.implicits.catsSyntaxEitherId

case class WeatherInput(
  latitude: Double,
  longitude: Double
)

object WeatherInput {
  final case class WeatherInputError(errorMessage: String) extends RuntimeException(errorMessage)

  private def makeException(input: String): Exception =
    WeatherInputError(s"Please provide latitude,longitude in the format: \"21.02,-10.001\". Input provided is invalid: ${input}")

  def fromString(s: String): Either[Exception, WeatherInput] = {
    val splits = s.split(",")
    if (splits.length != 2) makeException(s).asLeft[WeatherInput]
    else {
      try {
        val lat = splits(0).toDouble
        val long = splits(1).toDouble
        WeatherInput(latitude = lat, longitude = long).asRight[Exception]
      } catch {
        case _: Exception =>
          makeException(s).asLeft[WeatherInput]
      }
    }
  }
}
