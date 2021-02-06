name := "whchess"

version := "0.1"

scalaVersion := "2.13.3"
lazy val catsVersion = "2.3.0"
lazy val enumeratumVersion = "1.6.1"

libraryDependencies ++= Seq(
  "org.typelevel"  %% "cats-core"  % catsVersion,
  "com.beachape"   %% "enumeratum" % enumeratumVersion,
  "org.scalacheck" %% "scalacheck" % "1.14.1" % "test"
)
