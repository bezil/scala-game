package devbez
import devbez.GameOptions.*
import indigo.*

final case class Model(
    playerRotation: Float,
    playerPosition: Point
)

// Accessing width and height from gameOptions
val viewportWidth: Int = GameOptions.screenWidth
val viewportHeight: Int = GameOptions.screenHeight

// Create a new GameViewport with the width and height from gameOptions
val viewport = GameViewport(width = viewportWidth, height = viewportHeight)
val origin: Point = viewport.center

object Model {

   val Initial: Model = Model(
        playerRotation = 55.0f,
        playerPosition = (origin)
    )
}
