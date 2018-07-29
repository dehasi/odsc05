package observatory

import java.nio.file.Paths
import java.time.LocalDate

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.types.{DoubleType, StringType}

import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.types.{DoubleType, IntegerType}
import org.apache.spark.sql.functions._
/**
  * 1st milestone: data extraction
  */
object Extraction {
  Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

  implicit val spark:SparkSession = SparkSession
  .builder()
  .master("local")
  .appName("Extraction")
  .getOrCreate()

  import spark.implicits._

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


  def resourcePath(resource: String): String =
  Paths.get(getClass.getResource(resource).toURI).toString
  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Year, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Temperature)] = {

    val stations = spark.read.csv(resourcePath(stationsFile)).select(
      concat_ws("~", coalesce('_c0, lit("")), '_c1).alias("id"),
//      '_c0.alias("stn").cast(StringType),
//      '_c1.alias("wban").cast(StringType),
      '_c2.alias("lat").cast(DoubleType),
      '_c3.alias("lon").cast(DoubleType)
    )
      .where('_c2.isNotNull && '_c3.isNotNull && '_c2 =!= 0.0 && '_c3 =!= 0.0)
      .as[Station]

    val temperatures = spark.read.csv(resourcePath(temperaturesFile)).select(
            concat_ws("~", coalesce('_c0, lit("")), '_c1).alias("id"),
//      '_c0.alias("stn").cast(StringType),
//      '_c1.alias("wban").cast(StringType),
      lit(year).as("year"),
      '_c2.alias("month").cast(IntegerType),
      '_c3.alias("day").cast(IntegerType),
      '_c4.alias("temperature").cast(DoubleType)
    )
      .where('_c4.between(-200, 200))
      .as[TemperatureRecord]

    stations.join(right = temperatures, usingColumn= "id").as[StationTemperature]
        .collect().par
      .map(st => (st.getLocalDate, st.getLocation, st.getCelsius)).seq

  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Temperature)]): Iterable[(Location, Temperature)] = {
    records.par.groupBy(_._2).mapValues(v=> v.foldLeft(0.0)((sum, t)=>sum + t._3) / v.size).seq
  }

}
