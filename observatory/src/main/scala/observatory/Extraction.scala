package observatory

import java.io.FileReader
import java.nio.file.Paths
import java.time.LocalDate

import akka.stream.javadsl.FileIO
import com.univocity.parsers.csv.{CsvParser, CsvParserSettings}

import scala.io.Source
//import cats.effect.Sync
import fs2.{io, text}

/**
  * 1st milestone: data extraction
  */
object Extraction {

  def fahrenheitToCelsius(f: Double): Double =
    (f - 32.0) * (5.0 / 9.0)


  def toStation(line: String): Station = {
    def parseLine(line: String): Array[String] = {
      val array = Array("", "", "", "")
      var i: Int = 0
      for (c <- line) {
        if (c == ',') {
          i = i + 1
        } else {
          array(i) = array(i) + c
        }
      }
      array
    }

    val parsed = parseLine(line)
    new Station(parsed(0), parsed(1), Location(if (parsed(2).isEmpty) .0 else parsed(2).toDouble, if (parsed(3).isEmpty) .0 else parsed(3).toDouble))
  }

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Year, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Temperature)] = {
    val stations = Source.fromFile(stationsFile, "UTF-8").getLines().toStream
      .map(toStation).toList
    Seq()
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Temperature)]): Iterable[(Location, Temperature)] = {
    ???
  }

}
