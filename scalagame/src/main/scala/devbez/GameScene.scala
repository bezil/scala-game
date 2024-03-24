package devbez

import indigo.*
import indigo.scenes.*

object GameScene extends Scene[Unit, Model, Unit] {
  type SceneModel     = Model
  type SceneViewModel = Unit

  val name: SceneName =
    SceneName("game")

  val THICKNESS: Int = 2
  val SCALE: Int = 2

  val modelLens: Lens[Model, Model] =
    Lens.keepLatest

  val viewModelLens: Lens[Unit, Unit] =
    Lens.keepLatest

  val eventFilters: EventFilters =
    EventFilters.Permissive

  val subSystems: Set[SubSystem] =
    Set()

  def drawTriangle(origin: Point, scale: Int, rotate: Float): Shape.Polygon = {
    Shape.Polygon(
      Fill.Color(RGBA.Black),
      Stroke(THICKNESS, RGBA.White),
    )(

      Point(origin.x, origin.y),
      Point(origin.x + 9 * scale, origin.y + 18 * scale),
      Point(origin.x, origin.y + 20 * scale),
    ).transformTo(origin, Radians.fromDegrees(rotate), Vector2(1, 1))
  }

  def updateModel(
      context: SceneContext[Unit],
      model: Model
  ): GlobalEvent => Outcome[Model] = {
    {
      case KeyboardEvent.KeyDown(Key.KEY_W) =>
        val newPosition = model.playerPosition.withY(model.playerPosition.y - 2)
        Outcome(model.copy(playerPosition = newPosition))

      case KeyboardEvent.KeyDown(Key.KEY_S) =>
        val newPosition = model.playerPosition.withY(model.playerPosition.y + 2)
        Outcome(model.copy(playerPosition = newPosition))

      case KeyboardEvent.KeyDown(Key.KEY_A) =>
        val newPosition = model.playerPosition.withX(model.playerPosition.x - 2)
        Outcome(model.copy(playerPosition = newPosition))

      case KeyboardEvent.KeyDown(Key.KEY_D) =>
        val newPosition = model.playerPosition.withX(model.playerPosition.x + 2)
        Outcome(model.copy(playerPosition = newPosition))

      case KeyboardEvent.KeyDown(Key.KEY_Q) =>
        val newRotation = model.playerRotation + 5.0f
        Outcome(model.copy(playerRotation = newRotation))

      case KeyboardEvent.KeyDown(Key.KEY_E) =>
        val newRotation = model.playerRotation - 5.0f
        Outcome(model.copy(playerRotation = newRotation))
      case _ => Outcome(model)
    }
  }

  def updateViewModel(
      context: SceneContext[Unit],
      model: Model,
      viewModel: Unit
  ): GlobalEvent => Outcome[Unit] =
    _ => Outcome(viewModel)

  def present(
      context: SceneContext[Unit],
      model: Model,
      viewModel: Unit
  ): Outcome[SceneUpdateFragment] = {
    val triangle = drawTriangle(
      model.playerPosition,
      SCALE,
      model.playerRotation,
    )

    Outcome(SceneUpdateFragment(triangle))
  }
}
