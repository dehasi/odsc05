package observatory

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  val p: Int = 2
  val r: Double = 6371.0

  def distance(from: Location, to: Location): Double = {

    def toRadians(degree: Double) = degree * math.Pi / 180.0

    def isOpposite(l1: Location, l2: Location) = l1.lat == -l2.lat && math.abs(l1.lon - l2.lon) == 180.0

    if (from.lat == to.lat && from.lon == to.lon) 0
    else if (isOpposite(from, to)) r * math.Pi
    else {
      val fromLat = toRadians(from.lat)
      val toLat = toRadians(to.lat)
      val fromLon = toRadians(from.lon)
      val toLon = toRadians(to.lon)
      val deltaLon = math.abs(fromLon - toLon)
      r * math.acos(math.sin(fromLat) * math.sin(toLat) + math.cos(fromLat) * math.cos(toLat) * math.cos(deltaLon))
    }
  }

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location     Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Temperature)], location: Location): Temperature = {
    lazy val nom = temperatures
      .map(lt => (distance(location, lt._1), lt._2))
      .map(dt => (if (dt._1 ==0) 1 else 1.0 / math.pow(dt._1, p), dt._2))
      .map(wt => wt._1 * wt._2)
      .sum

    lazy val denom = temperatures
      .map(lt => distance(location, lt._1))
      .map(dt => 1.0 / math.pow(dt, p))
      .sum

    temperatures.find(temp => distance(temp._1, location) < 1) match {
      case Some(temp) => temp._2
      case None =>  nom / denom
    }
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value  The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Temperature, Color)], value: Temperature): Color = {
    val sorted = points.toSeq.sortBy(_._1)
    if (value <= sorted.head._1) sorted.head._2
    else if (value >= sorted.last._1) sorted.last._2
    else {
      val closestIndex = sorted.indexWhere(_._1 > value)
      val (v1, c1) = sorted(closestIndex - 1)
      val (v2, c2) = sorted(closestIndex)
      val interpolate = (value - v1) / (v2 - v1)
      val red = math.round(c1.red + (c2.red - c1.red) * interpolate).toInt
      val green = math.round(c1.green + (c2.green - c1.green) * interpolate).toInt
      val blue = math.round(c1.blue + (c2.blue - c1.blue) * interpolate).toInt

      Color(red, green, blue)
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)]): Image = {
    val pixels = for {
      lat <- (-89 to 90).reverse
      lon <- -180 to 179
    } yield {
      val t = predictTemperature(temperatures, Location(lat, lon))
      val c = interpolateColor(colors, t)
      Pixel(c.red, c.green, c.blue, 127)
    }

    Image(360, 180, pixels.toArray)
  }

}

