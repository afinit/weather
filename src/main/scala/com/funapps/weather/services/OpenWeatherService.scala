package com.funapps.weather.services

import cats.effect.Concurrent
import cats.implicits._
import com.funapps.weather.models.{WeatherInput, WeatherResponse}
import org.http4s._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.io.GET
import org.http4s.UriTemplate._

trait OpenWeatherService[F[_]] {

  def weather(weatherInput: WeatherInput): F[WeatherResponse]

}

object OpenWeatherService {
  def build[F[_] : Concurrent](
    httpClient: Client[F],
    openWeatherAppId: String
  ): OpenWeatherService[F] = new OpenWeatherServiceImpl[F](httpClient, openWeatherAppId)

  final case class OpenWeatherError(e: Throwable) extends RuntimeException(e)

}

class OpenWeatherServiceImpl[F[_] : Concurrent](
  httpClient: Client[F],
  openWeatherAppId: String
) extends OpenWeatherService[F] {

  private val dsl = new Http4sClientDsl[F] {}

  import dsl._

  override def weather(weatherInput: WeatherInput): F[WeatherResponse] = {
    for {
      uri <-
        UriTemplate(
          authority = Some(Uri.Authority(host = Uri.RegName("api.openweathermap.org"))),
          scheme = Some(Uri.Scheme.http),
          path = List(PathElm("data"), PathElm("2.5"), PathElm("weather")),
          query = List(
            ParamElm("lat", List(weatherInput.latitude.toString)),
            ParamElm("lon", List(weatherInput.longitude.toString)),
            ParamElm("units", List("imperial")),
            ParamElm("appid", List(openWeatherAppId))
          )
        ).toUriIfPossible
          .toEither
          .liftTo[F]

      request = GET(uri)
      response <- httpClient.expect[WeatherResponse](request)
        .adaptError { case t =>
          println(t.getStackTrace.mkString("\n"))
          OpenWeatherService.OpenWeatherError(t)
        }
    } yield response
  }
}
