resolvers += "HMRC-open-artefacts-maven2" at "https://open.artefacts.tax.service.gov.uk/maven2"
resolvers += Resolver.url("HMRC-open-artefacts-ivy2", url("https://open.artefacts.tax.service.gov.uk/ivy2"))(
  Resolver.ivyStylePatterns
)

addSbtPlugin("ch.epfl.scala"    % "sbt-scalafix"   % "0.14.4")
addSbtPlugin("com.timushev.sbt" % "sbt-updates"    % "0.6.4")
addSbtPlugin("org.scalameta"    % "sbt-scalafmt"   % "2.5.6")
addSbtPlugin("uk.gov.hmrc"      % "sbt-auto-build" % "3.24.0")
