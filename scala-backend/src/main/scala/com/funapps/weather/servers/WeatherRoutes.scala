package com.funapps.weather.servers

import cats.effect.Concurrent
import cats.implicits._
import com.funapps.weather.models.Coordinates
import com.funapps.weather.services.{OpenWeatherService, WeatherService}
import com.typesafe.scalalogging.StrictLogging
import org.http4s.{HttpRoutes, Response}
import org.http4s.dsl.Http4sDsl

object WeatherRoutes extends StrictLogging {

  def routes[F[_]: Concurrent](
    weatherService: WeatherService[F],
    openWeatherService: OpenWeatherService[F]
  ): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    def logAndInternalServerError(e: Throwable): F[Response[F]] = {
      logger.error(e.getMessage)
      logger.error(e.getStackTrace.mkString("\n"))
      InternalServerError()
    }

    def handleErrors(res: F[Response[F]]): F[Response[F]] = res.handleErrorWith {
      case e: IllegalStateException => logAndInternalServerError(e)
      case openWeatherError: OpenWeatherService.OpenWeatherError => logAndInternalServerError(openWeatherError)
      case weatherInputError: Coordinates.WeatherInputError =>
        logger.error(weatherInputError.getStackTrace.mkString("\n"))
        BadRequest(weatherInputError.getMessage)
      case e: Throwable => logAndInternalServerError(e)
    }


    HttpRoutes.of[F] {
      case GET -> Root / "simpleweather" / latlong =>
        val res = for {
          input <- Coordinates.fromString(latlong).liftTo[F]
          weatherResponse <- openWeatherService.weather(input)
          _ = println(weatherResponse)
          output <- weatherService.getSimpleWeather(weatherResponse)
          resp <- Ok(output)
        } yield resp

        handleErrors(res)

      case GET -> Root / "weather" / latlong =>
        val res = for {
          input <- Coordinates.fromString(latlong).liftTo[F]
          weatherResponse <- openWeatherService.weather(input)
          resp <- Ok(weatherResponse)
        } yield resp

        handleErrors(res)
    }
  }

}
