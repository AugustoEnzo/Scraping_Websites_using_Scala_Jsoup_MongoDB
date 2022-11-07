package ProgressBarr

object ProgressBar {
  def progressBar(value: Float, end: Float, state: Int): Unit = {
    val perc: Float = (value / end) * 100

    val bar: Seq[String] = Seq("|", "/", "-", "\\")

      println(bar(state) + perc.formatted("%.2f%%"))
      try {
        Thread.sleep(400)
        print("\u001b[2J")
        Console.flush()
      }
      catch {
        case e: Throwable => println(e)
      }
  }
}