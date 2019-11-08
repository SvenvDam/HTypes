[![Build Status](https://travis-ci.org/SvenvDam/HTypes.svg?branch=master)](https://travis-ci.org/SvenvDam/HTypes)

# HTypes
> A type-safe, asynchronous, lightweight Scala extension to the HBase API 

HTypes is a simple Scala extension to the Apache HBase API.
It adds two improvements to the existing API:

* Asynchronous execution of I/O operations
* Object-relation mapping (ORM)

## Installation

```sbtshell
libraryDependencies += "com.svenvandam" %% "htypes" % "0.2"
```

## Example

```scala
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.async.FutureAsyncBackend
import com.svenvandam.htypes.model._
import com.svenvandam.htypes.hbase.RowCodec
import com.svenvandam.htypes.converters.{PutUtils, GetUtils}
import org.apache.hadoop.hbase.client.{Connection, Put, Scan}
import org.apache.hadoop.hbase._
import scala.concurrent.Future


// an example type we'll be encoding and decoding

case class User(id: String, name: String, age: Int)

// typeclass instance for User to en/decode it to an HBase compatible format

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

// you'll have to do this youself

val conn: Connection = getConnection()

val table = conn.getTable(TableName.valueOf("MyTable"))

// a user gets automatically encoded to an HBase compatible form and stored in a Put query

val put = PutUtils.createFrom(User("id123", "Alice", 30))

// HTypes lets you execute queries asynchronous
// You have to define a backend to execute your query in (Future, Task, IO, etc)
// An AsyncBackend instance for Future is provided by HTypes

import scala.concurrent.ExecutionContext.Implicits.global
implicit val asyncBackend = FutureAsyncBackend()

val f: Future[Unit] = table.putAsync(put)

// Automatically scan all columns associated with User

val scan = new Scan().from[User]

// Automatically convert scan result to a series of time-versioned User's

val futureScanResult = table.getScannerAsync(scan).as[User]

for {
  usersInScan       <- futureScanResult
  userValues        <- usersInScan
  (user, timestamp) <- userValues
} println(s"Found user $user at timestamp $timestamp")

```

