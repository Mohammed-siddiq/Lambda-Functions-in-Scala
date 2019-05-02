scalaVersion := "2.12.4"

resolvers += Resolver.bintrayRepo("beyondthelines", "maven")

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)

val scalapbVersion = com.trueaccord.scalapb.compiler.Version.scalapbVersion

libraryDependencies ++= Seq(
  "com.trueaccord.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
  // for gRPC
  "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion,
  // for GRPC Gateway
  "beyondthelines" %% "grpcgatewayruntime" % "0.0.6" % "compile,protobuf",
  "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
  "com.amazonaws" % "aws-lambda-java-events" % "2.2.6")


scalacOptions in ThisBuild ++= Seq("-unchecked", "-deprecation")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
