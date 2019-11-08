[![Build Status](https://travis-ci.org/SvenvDam/HTypes.svg?branch=master)](https://travis-ci.org/SvenvDam/HTypes)

# HTypes
> A type-safe, asynchronous, lightweight Scala extension to the HBase API 

HTypes is a simple Scala extension to the Apache HBase API.
It adds two improvements to the existing API:

* Asynchronous execution of I/O operations
* Object-relation mapping (ORM)

## Installation

```sbtshell
libraryDependencies += "com.svenvandam" %% "htypes" % "0.1"
```

## Example

```scala
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.model._
import com.svenvandam.htypes.hbase.RowCodec
import com.svenvandam.htypes.converters.{PutUtils, GetUtils}
import org.apache.hadoop.hbase.client.{Connection, Put, Scan}
import org.apache.hadoop.hbase._
import scala.concurrent.Future

case class User(id: String, name: String, age: Int)

implicit val userCodec = new RowCodec[User] {
  private val ageColumn = Column("profile", "age")
  private val nameColumn = Column("profile", "name")

  def getColumns = Set(
    ageColumn,
    nameColumn
  )
  
  def encode(user: User): Row = Row(
    user.id,
    Map(
      ageColumn -> CellValue(user.age, None),
      nameColumn -> CellValue(user.name, None)
    )
  )
  
  def decode(row: Row): Option[User] = for {
    id                      <- row.key.as[String]
    CellValue(ageBytes, _)  <- row.values.get(ageColumn)
    age                     <- ageBytes.as[Int]
    CellValue(nameBytes, _) <- row.values.get(nameColumn)
    name                    <- nameBytes.as[String]
  } yield User(id, name, age)
}

val conn: Connection = getConnection()

val table = conn.getTable(TableName.valueOf("MyTable"))

val put = PutUtils.createFrom(User("id123", "Alice", 30))

val f: Future[Unit] = table.putAsync(put)


val scan = new Scan().from[User]

val futureScanResult = table.getScannerAsync(scan).as[User]

for {
  usersInScan       <- futureScanResult
  userValues        <- usersInScan
  (user, timestamp) <- userValues
} println(s"Found user $user at timestamp $timestamp")

```

