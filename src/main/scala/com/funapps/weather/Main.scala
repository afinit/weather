package com.funapps.weather

import cats.effect.{ExitCode, IO, IOApp}
import com.funapps.weather.servers.WeatherServer

object Main extends IOApp {
  final def run(args: List[String]): IO[ExitCode] = {
    if (args.length < 1) {
      println("Must include openweather app id as a command line argument")
      IO.pure(ExitCode.Error)
    }
    else {
      val openWeatherAppId = args.head
      WeatherServer.run[IO](openWeatherAppId).as(ExitCode.Success)
    }
  }
}
