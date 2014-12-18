name := "AkkaStreams"

version := "1.0"

scalaVersion := "2.11.4"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream-experimental" % "1.0-M1",
  "com.typesafe.akka" % "akka-http-core-experimental_2.11" % "1.0-M1",
  "com.typesafe.akka" % "akka-http-testkit-experimental_2.11" % "1.0-M1",
  "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "1.0-M1",
  "com.typesafe.akka" % " akka-http-experimental_2.11" % "1.0-M1",
  "com.propensive" %% "rapture-core" % "1.0.0",
  "com.propensive" %% "rapture-json-jawn" % "1.0.6"
)