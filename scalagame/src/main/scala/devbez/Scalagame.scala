package devbez

import indigo.*
import indigo.scenes.*
import scala.scalajs.js.annotation.JSExportTopLevel

import devbez.generated.Config
import devbez.generated.Assets

@JSExportTopLevel("IndigoGame")
object Scalagame extends IndigoGame[Unit, Unit, Model, Unit] {
  def initialScene(bootData: Unit): Option[SceneName] =
    None

  def scenes(bootData: Unit): NonEmptyList[Scene[Unit, Model, Unit]] =
    NonEmptyList(GameScene)

  val eventFilters: EventFilters =
    EventFilters.Permissive

  def boot(flags: Map[String, String]): Outcome[BootResult[Unit]] =
    Outcome(
      BootResult.noData(Config.config)
        .withAssets(Assets.assets.assetSet)
    )

  def initialModel(startupData: Unit): Outcome[Model] =
    Outcome((Model.Initial))

  def initialViewModel(startupData: Unit, model: Model): Outcome[Unit] =
    Outcome(())

  def setup(
      bootData: Unit,
      assetCollection: AssetCollection,
      dice: Dice
  ): Outcome[Startup[Unit]] =
    Outcome(Startup.Success(()))

  def updateModel(
      context: FrameContext[Unit],
      model: Model
  ): GlobalEvent => Outcome[Model] =
    _ => Outcome(model)

  def updateViewModel(
      context: FrameContext[Unit],
      model: Model,
      viewModel: Unit
  ): GlobalEvent => Outcome[Unit] =
    _ => Outcome(viewModel)

  def present(
      context: FrameContext[Unit],
      model: Model,
      viewModel: Unit
  ): Outcome[SceneUpdateFragment] =
    Outcome(SceneUpdateFragment.empty)
}
