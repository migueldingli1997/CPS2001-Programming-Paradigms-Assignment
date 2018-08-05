package TaskB

import scala.concurrent.duration._

trait SimConfig {
  val inverterDelay = 100 milliseconds
  val andDelay = 100 milliseconds
  val orDelay = 1000 milliseconds
}