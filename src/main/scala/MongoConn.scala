import org.mongodb.scala._
import org.mongodb.scala.connection.ClusterSettings

import scala.collection.JavaConverters._

class MongoConn() {
  val mongoClient: MongoClient = MongoClient("mongodb://172.17.0.2:27017")
}