package object observatory {
  type Temperature = Double // °C, introduced in Week 1
  type Year = Int // Calendar year, introduced in Week 1

  case class Station(stn: String, wban: String, location: Location)

  case class TemperatureRecord(stn: String, wban: String, month: Int, day: Int, temperature: Double) {
    def getCelsius: Double = (temperature - 32.0) * (5.0 / 9.0)
  }

}
