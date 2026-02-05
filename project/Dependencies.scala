import sbt.*

object Dependencies {
  val pekkoVersion = "1.1.3"

  val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% "api-test-runner"             % "0.10.0" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play"          % "7.0.1"  % Test,
    "org.playframework"      %% "play-ws"                     % "3.0.6"  % Test,
    "org.apache.pekko"       %% "pekko-actor"                 % pekkoVersion,
    "org.apache.pekko"       %% "pekko-stream"                % pekkoVersion,
    "org.apache.pekko"       %% "pekko-protobuf-v3"           % pekkoVersion,
    "org.apache.pekko"       %% "pekko-actor-typed"           % pekkoVersion,
    "org.apache.pekko"       %% "pekko-serialization-jackson" % pekkoVersion,
    "org.apache.pekko"       %% "pekko-slf4j"                 % pekkoVersion,
    "org.playframework"      %% "play-pekko-http-server"      % "3.0.6",
    "uk.gov.hmrc"            %% "totp-generator"              % "1.0.0"  % Test
  )

}
