import ProgressBarr.ProgressBar.progressBar
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.JavaConverters._
import scala.collection.mutable.ListBuffer

object MyJsoupWebscraping extends App {
  val ctm: Float = System.currentTimeMillis()

  val doc: Document = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()


  val links = doc.select(".section-content").nextAll.select("div.index ul li a")

//  val linkData = links.asScala.map(link => (link.attr("href"), link.attr("title"), link.text)

  val links_list = links.asScala.map(link => link.attr("href"))
  var count: Float = 0
  var state: Int = 0
  var list_links = new ListBuffer[String]()
  val mongoCRUD = new MongoCRUD

  for (link <- links_list) {
    if (!list_links.contains(link)) {
      list_links += link
    }
  }
  val unique_links = list_links.toList

  for (link <- unique_links) {
    try {
      val doc_spec: Document = Jsoup.connect(s"https://developer.mozilla.org/$link").get()

      val concept: String = doc_spec.select(".section-content p").text

      val link_str: String = link.substring(20)

      try {
        mongoCRUD.verify_drop("APIS", link_str)
      }
      catch {
        case e: Throwable => println(s"The collection $link_str don't exist.")
      }
      mongoCRUD.create_insert("APIS", link_str, concept)

      count += 1
      if (state == 4) state = 0
      progressBar(count, unique_links.length.toFloat, state)
      state += 1

    }

    catch {
      case _: Throwable => println(s"$link - Error to get this api link. Error code 404")
    }

  }
  println(((ctm-System.currentTimeMillis())/1000) + "Seconds elapsed.")
  println((((ctm-System.currentTimeMillis())/1000)/60) + "Minutes elapsed.")
}
