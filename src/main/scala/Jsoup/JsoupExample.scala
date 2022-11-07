package Jsoup

import org.jsoup._
import org.jsoup.nodes.Document
import collection.JavaConverters._

object JsoupExample extends App {
  def get_example(): Unit = {
    val doc: Document = Jsoup.connect("https://en.wikipedia.org/").get()

    println(doc)
    println(doc.title())

    val headlines = doc.select("#mp-itn b a")

    for (headline <- headlines.asScala) yield println((headline.attr("title"), headline.attr("href")))

    for (headline <- headlines.asScala) yield println(headline.text)
  }

  get_example()
}
