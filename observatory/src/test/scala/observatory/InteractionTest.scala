package observatory

import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

import scala.collection.concurrent.TrieMap

trait InteractionTest extends FunSuite with Checkers {
  test("predictTemperature should guess temperature from given dataset") {
    val dataset: Iterable[(Location, Double)] =
      Seq(
        (Location(0.0, 0.0), 0.0),
        (Location(1.0, 0.0), 0.0),
        (Location(0.0, 1.0), 10.0),
        (Location(1.0, 1.0), 10.0)
      )
    val result = Visualization.predictTemperature(dataset, Location(0.5, 0.5))
    assert(result == 5.0)
  }
}
