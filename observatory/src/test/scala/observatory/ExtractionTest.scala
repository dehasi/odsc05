package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
trait ExtractionTest extends FunSuite {

  test("locateTemperatures reads stations from a file") {
//    val path = "/Users/rgaleyev/experimental/odsc05/observatory/src/main/resources/"
//    val path = "src/main/resources/"
    val s = Extraction.locateTemperatures(2015, "/stations.csv", "/2015.csv")
  }
}