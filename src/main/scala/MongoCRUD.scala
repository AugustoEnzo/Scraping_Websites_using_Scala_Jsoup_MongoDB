import com.mongodb.client.model.Indexes
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates.{combine, currentDate, set}
import org.mongodb.scala.result.InsertOneResult
import org.mongodb.scala.result.UpdateResult
import scala.collection.View.Drop

class MongoCRUD {

  val mongoClient: MongoClient = (new MongoConn).mongoClient

  def create_insert(database: String, name: String, value: String): Unit = {
    mongoClient.startSession()

    val db: MongoDatabase = mongoClient.getDatabase(database)

    db.createCollection(name)
//      db.createCollection(name).collect()
//        .subscribe((results: Seq[Void]) => println(s"Created: ${results.size}"))

    val collection: MongoCollection[Document] = db.getCollection(name)

    val document: Document = Document("_id" -> "concept", "value" -> value)

    val observable = collection.insertOne(document)
    observable.subscribe(new Observer[InsertOneResult] {

      override def onSubscribe(subscription: Subscription): Unit = subscription.request(1)

//      override def onNext(result: InsertOneResult): Unit = println(s"onNext $result")
      override def onNext(result: InsertOneResult): Unit = null

      override def onError(e: Throwable): Unit = println(e)

//      override def onComplete(): Unit = println("Completed")
      override def onComplete(): Unit = null
    })
  }

  def createIndex(database: String, collection: String, column: String): Unit = {
    val db: MongoDatabase = mongoClient.getDatabase(database)

    val coll: MongoCollection[Document] = db.getCollection(collection)

    coll.createIndex(Indexes.text(column)).collect().subscribe((results: Seq[String]) => println(s"Created: #${results.size} Index"))

  }

  def dropIndex(database: String, collection: String, column: String): Unit = {
    val db: MongoDatabase = mongoClient.getDatabase(database)

    val coll: MongoCollection[Document] = db.getCollection(collection)

//    coll.dropIndex(Indexes.text(column)).collect().subscribe(results => println(s"Dropped #${results.size} Index"))
    coll.dropIndex(Indexes.text(column)).collect().subscribe(results => results)

  }

  def read(database: String, collection: String, name: String): Unit = {
    val db: MongoDatabase = mongoClient.getDatabase(database)

    val coll: MongoCollection[Document] = db.getCollection(collection)

    coll.find(equal("_id", name)).collect().subscribe((results: Seq[Document]) => println(s"Found: #${results.size}"))
  }

  def verify_drop(database: String, collection: String): Unit = {
    val db: MongoDatabase = mongoClient.getDatabase(database)

//    db.getCollection(collection).drop.collect().subscribe((results: Seq[Void]) => println(s"Dropped $collection"))
    db.getCollection(collection).drop.collect().subscribe((results: Seq[Void]) => results)
  }

  def update(database: String, collection: String, value: String): Unit = {
    val db: MongoDatabase = mongoClient.getDatabase(database)

    val coll: MongoCollection[Document] = db.getCollection(collection)

    coll.updateOne(
      equal("_id", "concept"),
      combine(set("value", value ), currentDate("lastModified"))
    ).collect().subscribe((results: Seq[UpdateResult]) => println(s"Updated: #${results.size}"))

  }

  def drop(database: String, collection: String): Unit = {
    val db: MongoDatabase = mongoClient.getDatabase(database)

    val coll: MongoCollection[Document] = db.getCollection(collection)

    coll.drop().collect().subscribe((results: Seq[Void]) => println(s"Dropped: #${results.size}"))

  }
}