Global / onChangedBuildSource := ReloadOnSourceChanges

ThisBuild / scalaVersion := "2.12.10"

libraryDependencies += "org.typelevel" %% "cats-effect"       % "2.0.0"
libraryDependencies += "org.specs2"    %% "specs2-scalacheck" % "4.8.1" % Test