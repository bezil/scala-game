package devbez

import indigo.*
import indigo.scenes.*
import devbez.GameOptions.*

object GameScene extends Scene[Unit, Model, Unit] {
  type SceneModel     = Model
  type SceneViewModel = Unit

  val name: SceneName =
    SceneName("game")

  val THICKNESS: Int = 2
  val SCALE: Int = 1

  // Accessing width and height from gameOptions
  val viewportWidth: Int = GameOptions.screenWidth
  val viewportHeight: Int = GameOptions.screenHeight

  // Create a new GameViewport with the width and height from gameOptions
  val viewport = GameViewport(width = viewportWidth, height = viewportHeight)

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
      Point(origin.x, origin.y - 10 * scale),
      Point(origin.x + 7 * scale, origin.y + 10 * scale),
      Point(origin.x + 3 * scale, origin.y + 7 * scale),
      Point(origin.x - 3 * scale, origin.y + 7 * scale),
      Point(origin.x - 7 * scale, origin.y + 10 * scale),
      Point(origin.x, origin.y - 10 * scale)
    ).rotateBy(Radians.fromDegrees(rotate))
  }

  def updateModel(
      context: SceneContext[Unit],
      model: Model
  ): GlobalEvent => Outcome[Model] =
    _ => Outcome(model)

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
    val origin: Point = viewport.center

    val triangle = drawTriangle(origin, SCALE, -55.0)

    Outcome(SceneUpdateFragment(triangle))
  }
}
