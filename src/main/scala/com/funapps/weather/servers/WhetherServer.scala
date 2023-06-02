package com.funapps.weather.servers

import cats.effect.Async
import com.comcast.ip4s._
import com.funapps.weather.services.{OpenWeatherService, WeatherService}
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger

object WeatherServer {
  def run[F[_] : Async](openWeatherAppId: String): F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build

      openWeatherService = OpenWeatherService.build(client, openWeatherAppId)
      weatherService = WeatherService.build[F]

      httpApp = (
        WeatherRoutes.routes(weatherService, openWeatherService)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <-
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
}
