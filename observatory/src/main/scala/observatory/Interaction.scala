package observatory

import com.sksamuel.scrimage.{Image, Pixel}
import observatory.Visualization.{interpolateColor, predictTemperature}

/**
  * 3rd milestone: interactive visualization
  */
object Interaction {

  /**
    * @param tile Tile coordinates
    * @return The latitude and longitude of the top-left corner of the tile, as per http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
    */
  def tileLocation(tile: Tile): Location = {
    val lon = 360 * tile.x / math.pow(2, tile.zoom) - 180
    val lat = math.atan(math.sinh(math.Pi - 2 * math.Pi * tile.y / math.pow(2, tile.zoom))) * (180 / math.Pi)
    Location(lat, lon)
  }

  /**
    * @param temperatures Known temperatures
    * @param colors       Color scale
    * @param tile         Tile coordinates
    * @return A 256×256 image showing the contents of the given tile
    */
  def tile(temperatures: Iterable[(Location, Temperature)], colors: Iterable[(Temperature, Color)], tile: Tile): Image = {
    val size = 256
    val fromX = tile.x * size
    val toX = fromX + size
    val fromY = tile.y * size
    val toY = fromY + size

    val pixels = for {
      y1 <- (fromY until toY).par
      x1 <- (fromX until toX).par
    } yield {
      val t = predictTemperature(temperatures, tileLocation(Tile(x1, y1, tile.zoom+8)))
      val c = interpolateColor(colors, t)
      Pixel(c.red, c.green, c.blue, 127)
    }

    Image(size, size, pixels.toArray)
  }

  /**
    * Generates all the tiles for zoom levels 0 to 3 (included), for all the given years.
    *
    * @param yearlyData    Sequence of (year, data), where `data` is some data associated with
    *                      `year`. The type of `data` can be anything.
    * @param generateImage Function that generates an image given a year, a zoom level, the x and
    *                      y coordinates of the tile and the data to build the image from
    */
  def generateTiles[Data](
                           yearlyData: Iterable[(Year, Data)],
                           generateImage: (Year, Tile, Data) => Unit
                         ): Unit = {
    ???
  }

}
