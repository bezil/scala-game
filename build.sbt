import scala.sys.process._
import scala.language.postfixOps

import sbtwelcome._
import indigoplugin._

Global / onChangedBuildSource := ReloadOnSourceChanges

Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }

lazy val gameOptions: IndigoOptions =
  IndigoOptions.defaults
    .withTitle("Scala-game")
    .withWindowSize(1200, 800)
    .withBackgroundColor("black")
    .withAssetDirectory("assets")
    .excludeAssets {
      case p if p.endsWith(os.RelPath.rel / ".gitkeep") => true
      case _                                            => false
    }

lazy val scalagame =
  (project in file("scalagame"))
    .enablePlugins(ScalaJSPlugin, SbtIndigo)
    .settings( // Normal SBT settings
      name         := "scalagame",
      version      := "0.0.1",
      scalaVersion := "3.3.1",
      organization := "devbez",
      libraryDependencies ++= Seq(
        "org.scalameta" %%% "munit" % "0.7.29" % Test
      ),
      testFrameworks += new TestFramework("munit.Framework"),
      scalafixOnCompile  := true,
      semanticdbEnabled  := true,
      semanticdbVersion  := scalafixSemanticdb.revision,
    )
    .settings( // Indigo specific settings
      indigoOptions := gameOptions,
      libraryDependencies ++= Seq(
        "io.indigoengine" %%% "indigo-json-circe" % "0.16.0",
        "io.indigoengine" %%% "indigo"            % "0.16.0",
        "io.indigoengine" %%% "indigo-extras"     % "0.16.0"
      ),
      Compile / sourceGenerators += Def.task {
        IndigoGenerators("devbez.generated")
          .generateConfig("Config", gameOptions)
          .listAssets("Assets", gameOptions.assets)
          .toSourceFiles((Compile / sourceManaged).value)
      }
    )

lazy val indigo =
  (project in file("."))
    .settings(
      code := {
        val command = Seq("code", ".")
        val run = sys.props("os.name").toLowerCase match {
          case x if x contains "windows" => Seq("cmd", "/C") ++ command
          case _                         => command
        }
        run.!
      }
    )
    .settings(
      logo := "Scala-game (v" + version.value.toString + ")",
      usefulTasks := Seq(
        UsefulTask("runGame", "Run the game").noAlias,
        UsefulTask("buildGame", "Build web version").noAlias,
        UsefulTask("runGameFull", "Run the fully optimised game").noAlias,
        UsefulTask("buildGameFull", "Build the fully optimised web version").noAlias,
        UsefulTask("code", "Launch VSCode").noAlias
      ),
      logoColor        := scala.Console.MAGENTA,
      aliasColor       := scala.Console.YELLOW,
      commandColor     := scala.Console.CYAN,
      descriptionColor := scala.Console.WHITE
    )
    .aggregate(scalagame)

addCommandAlias(
  "buildGame",
  List(
    "scalagame/compile",
    "scalagame/fastLinkJS",
    "scalagame/indigoBuild"
  ).mkString(";", ";", "")
)
addCommandAlias(
  "buildGameFull",
  List(
    "scalagame/compile",
    "scalagame/fullLinkJS",
    "scalagame/indigoBuildFull"
  ).mkString(";", ";", "")
)
addCommandAlias(
  "runGame",
  List(
    "scalagame/compile",
    "scalagame/fastLinkJS",
    "scalagame/indigoRun"
  ).mkString(";", ";", "")
)
addCommandAlias(
  "runGameFull",
  List(
    "scalagame/compile",
    "scalagame/fullLinkJS",
    "scalagame/indigoRunFull"
  ).mkString(";", ";", "")
)

lazy val code =
  taskKey[Unit]("Launch VSCode in the current directory")
