package com.funapps.weather.services

import cats.effect.Temporal
import cats.effect.kernel.Resource
import cats.implicits._
import com.funapps.weather.models.{Coordinates, WeatherResponse}
import com.typesafe.scalalogging.StrictLogging
import io.chrisdavenport.mules.{MemoryCache, TimeSpec}
import org.http4s.UriTemplate._
import org.http4s._
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.io.GET

import scala.concurrent.duration._

trait OpenWeatherService[F[_]] {

  def weather(weatherInput: Coordinates): F[WeatherResponse]

}

object OpenWeatherService {
  def build[F[_] : Temporal](
    httpClient: Client[F],
    openWeatherAppId: String
  ): Resource[F, OpenWeatherService[F]] = {
    val cacheDuration = TimeSpec.unsafeFromDuration(10.seconds)
    val cacheResource = Resource.make(
      MemoryCache.ofSingleImmutableMap[F, String, WeatherResponse](Some(cacheDuration))
    )(_.pure[F].void)

    cacheResource.map { new OpenWeatherServiceImpl[F](httpClient, openWeatherAppId, _) }
  }

  final case class OpenWeatherError(e: Throwable) extends RuntimeException(e)

}

class OpenWeatherServiceImpl[F[_] : Temporal](
  httpClient: Client[F],
  openWeatherAppId: String,
  cache: MemoryCache[F, String, WeatherResponse]
) extends OpenWeatherService[F] with StrictLogging {

  private val dsl = new Http4sClientDsl[F] {}

  import dsl._

  private def callOpenWeatherWeather(weatherInput: Coordinates): F[WeatherResponse] =
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
          logger.error(t.getStackTrace.mkString("\n"))
          OpenWeatherService.OpenWeatherError(t)
        }
      _ <- cache.insert(weatherInput.toString, response)
      _ = logger.info(s"retrieved new weather: $response")
    } yield response

  override def weather(weatherInput: Coordinates): F[WeatherResponse] = for {
    cacheResult <- cache.lookup(weatherInput.toString)
    result <- cacheResult.map(_.pure[F]).getOrElse(callOpenWeatherWeather(weatherInput))
  } yield result
}
