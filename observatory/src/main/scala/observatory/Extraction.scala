package observatory

import java.io.FileReader
import java.nio.file.Paths
import java.time.LocalDate

import akka.stream.javadsl.FileIO
import com.univocity.parsers.csv.{CsvParser, CsvParserSettings}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source
//import cats.effect.Sync
import fs2.{io, text}

/**
  * 1st milestone: data extraction
  */
object Extraction {

//  @transient lazy val conf: SparkConf = new SparkConf().setMaster("local").setAppName("StackOverflow")
//  @transient lazy val sc: SparkContext = new SparkContext(conf)

  implicit val spark:SparkSession = SparkSession
  .builder()
  .master("local")
  .appName(this.getClass.getSimpleName)
  .getOrCreate()

  import spark.implicits._
  /** Main function */
  //  def main(args: Array[String]): Unit = {
  //
  //    val lines   = sc.textFile("src/main/resources/stackoverflow/stackoverflow.csv")
  //
  //  }


  def fahrenheitToCelsius(f: Double): Double =
    (f - 32.0) * (5.0 / 9.0)


  def parseLine(line: String): Array[String] = {
    val array = Array("", "", "", "", "")
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

  def toStation(line: String): Station = {
    val parsed = parseLine(line)
    new Station(parsed(0), parsed(1), Location(if (parsed(2).isEmpty) .0 else parsed(2).toDouble, if (parsed(3).isEmpty) .0 else parsed(3).toDouble))
  }

  def toTemperature(line: String): TemperatureRecord = {
    val parsed = parseLine(line)
    new TemperatureRecord(parsed(0), parsed(1), parsed(2).toInt, parsed(3).toInt, parsed(4).toDouble)
  }

  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Year, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Temperature)] = {
    val path = "src/main/resources/"
    val stations = Source.fromFile(path +stationsFile, "UTF-8").getLines().toStream
      .map(toStation).toList

    Source.fromFile(path +temperaturesFile, "UTF-8").getLines().toStream
      .map(toTemperature)
      .map(tr => {
        val station = stations.find(st => st.stn == tr.stn && st.wban == tr.wban ).get
        (LocalDate.of(year, tr.month, tr.day), station.location, tr.getCelsius)
      })
//      .toList
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Temperature)]): Iterable[(Location, Temperature)] = {
    ???
  }

}
