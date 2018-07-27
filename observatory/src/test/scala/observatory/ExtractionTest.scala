package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
trait ExtractionTest extends FunSuite {

  test("locateTemperatures reads stations from a file") {
    val s = Extraction.locateTemperatures(2015, "/Users/rgaleyev/experimental/odsc05/observatory/src/main/resources/stations.csv","2015.csv")
  }
}