package com.funapps.weather.servers

import cats.effect.Async
import com.comcast.ip4s._
import com.funapps.weather.services.{OpenWeatherService, WeatherService}
import org.http4s.Uri
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.headers.Origin
import org.http4s.server.middleware.{CORS, Logger}

object WeatherServer {
  def run[F[_] : Async](openWeatherAppId: String): F[Nothing] = {
    for {
      client <- EmberClientBuilder.default[F].build

      openWeatherService <- OpenWeatherService.build(client, openWeatherAppId)
      weatherService = WeatherService.build[F]

      httpApp = (
        WeatherRoutes.routes(weatherService, openWeatherService)
      ).orNotFound

      httpAppWithCors = CORS.policy.withAllowOriginHost(Set(
        Origin.Host(Uri.Scheme.http, Uri.RegName("localhost"), Some(3000))
      ))
        .apply(httpApp)

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpAppWithCors)

      _ <-
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield ()
  }.useForever
}
