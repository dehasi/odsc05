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
  /*
  [Test Description] [#2 - Raw data display] predicted temperature at location z should be closer to known temperature at location x than to known temperature at location y,
  if z is closer (in distance) to x than y, and vice versa
[Observed Error] NaN did not equal 10.0 +- 1.0E-4 Incorrect predicted temperature at Location(0.0,0.0): NaN. Expected: 10.0
[Lost Points] 10
   */
  def predictTemperature(temperatures: Iterable[(Location, Temperature)], location: Location): Temperature = {
    val nom = temperatures
      .map(lt => (distance(location, lt._1), lt._2))
      .map(dt => (1.0 / math.pow(dt._1, p), dt._2))
      .map(wt => wt._1 * wt._2)
      .sum

    val denom = temperatures
      .map(lt => distance(location, lt._1))
      .map(dt => 1.0 / math.pow(dt, p))
      .sum

    nom / denom
  }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value  The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Temperature, Color)], value: Temperature): Color = {
    ???
  }

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)]): Image = {
    ???
  }

}

