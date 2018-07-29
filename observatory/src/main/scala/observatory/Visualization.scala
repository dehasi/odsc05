package observatory

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  val p: Int = 2
  val r: Int = 6371

  def distance(from: Location, to: Location): Double = {
    if (from.lat == to.lat && from.lon == to.lon) 0
    // todo: check antidotes
    else r * math.acos(math.sin(from.lat) * math.sin(to.lat) + math.cos(from.lat) * math.cos(to.lat) * math.cos(from.lon - to.lon))
  }

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location     Location where to predict the temperature
    * @return The predicted temperature at `location`
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

