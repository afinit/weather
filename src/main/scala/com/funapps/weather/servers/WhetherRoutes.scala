package com.funapps.weather.servers

import cats.effect.Concurrent
import cats.implicits._
import com.funapps.weather.models.WeatherInput
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

    HttpRoutes.of[F] {
      case GET -> Root / "simpleweather" / latlong =>
        val res = for {
          input <- WeatherInput.fromString(latlong).liftTo[F]
          weatherResponse <- openWeatherService.weather(input)
          _ = println(weatherResponse)
          output <- weatherService.getWeather(weatherResponse)
          resp <- Ok(output)
        } yield resp

        def logAndInternalServerError(e: Throwable): F[Response[F]] = {
          logger.error(e.getMessage)
          logger.error(e.getStackTrace.mkString("\n"))
          InternalServerError()
        }

        res.handleErrorWith {
          case e: IllegalStateException => logAndInternalServerError(e)
          case openWeatherError: OpenWeatherService.OpenWeatherError => logAndInternalServerError(openWeatherError)
          case weatherInputError: WeatherInput.WeatherInputError =>
            logger.error(weatherInputError.getStackTrace.mkString("\n"))
            BadRequest(weatherInputError.getMessage)
          case e: Throwable => logAndInternalServerError(e)
        }
    }
  }

}
