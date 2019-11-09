[![Build Status](https://travis-ci.org/SvenvDam/HTypes.svg?branch=master)](https://travis-ci.org/SvenvDam/HTypes)

# HTypes
> A type-safe, asynchronous, composable, lightweight Scala extension to the HBase API 

HTypes is a simple Scala extension to the Apache HBase API with no dependencies.
It assumes you have have the HBase client API available as a dependency in your project.
It adds the following improvements to the Java API:

* Asynchronous execution of queries.
* Automatic conversion between objects and an encoded form.
* Automatic conversion between common primitive types and byte arrays (and the option to add more yourself).

## Installation

```sbtshell
libraryDependencies += "com.svenvandam" %% "htypes" % "0.2"
```

## Example

### Basic usage

```scala
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.model._
import com.svenvandam.htypes.hbase._
import com.svenvandam.htypes.hbase.{PutUtils, GetUtils}
import org.apache.hadoop.hbase.client.{Connection, Put, Scan}
import org.apache.hadoop.hbase._


// an example type we'll be encoding and decoding

case class User(id: String, name: String, age: Int)

// typeclass instances for User to en/decode it to an HBase compatible format

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

// you'll have to do this yourself

val conn: Connection = getConnection()

val table = conn.getTable(TableName.valueOf("MyTable"))

// a user gets automatically encoded to an HBase compatible form and stored in a Put query

val put = PutUtils.createFrom(User("id123", "Alice", 30))

table.put(put)

// Automatically scan all columns associated with User

val scan = new Scan().addColumnsFrom[User]

// Automatically convert scan result to a series of time-versioned User's

val scanResult = table.getScanner(scan).as[User]

for {
  usersInScan       <- scanResult
  userValues        <- usersInScan
  (user, timestamp) <- userValues
} println(s"Found user $user at timestamp $timestamp")

```

### Async queries

```scala
// HTypes lets you execute queries asynchronous
// You have to define a backend to execute your query in (Future, Task, IO, etc)
// An AsyncBackend instance for Future is provided by HTypes

import com.svenvandam.htypes.async.FutureAsyncBackend
import scala.concurrent.ExecutionContext.Implicits.global
implicit val asyncBackend = FutureAsyncBackend()

val f: Future[Unit] = table.putAsync(put)

f.foreach(_ => println("Put result!"))
```

### Composing encoders and decoders

```scala
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.model._
import com.svenvandam.htypes.hbase._

case class UserWithSession(id: String, name: String, age: Int, SessionId: Long)

case class User(id: String, name: String, age: Int)

val userCodec = new RowCodec[User] {
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


// mapping an existing RowDecoder
def getNewSessionId(): Long = ???

val userSessionDecoder: RowDecoder[UserWithSession] = 
  userCodec.map { case user =>
    UserWithSession(user.id, user.name, user.age, getNewSessionId())
  }

// contramap an existing RowDecoder
val userSessionEncoder: RowEncoder[UserWithSession] = 
  userCodec.contramap[UserWithSession] { case userWithSession =>
    User(userWithSession.id, userWithSession.name, userWithSession.age)
  } 

// combining decoders

case class UserInfo(id: String, lastBoughtItem: Int)

case class UserWithInfo(id: String, name: String, age: Int, lastBoughtItem: Int)

val productColumn = Column("history", "last_bought_item")

def decode(row: Row): Option[UserInfo] = for {
  id                          <- row.key.as[String]
  CellValue(productBytes, _)  <- row.values.get(productColumn)
  product                     <- productBytes.as[Int]
} yield UserInfo(id, product)

val userInfoDecoder = RowDecoder(decode, Set(productColumn))

val userWithInfoDecoder = userCodec.combine(
  userInfoDecoder, 
  (user: User, info: UserInfo) => UserWithInfo(user.id, user.name, user.age, info.lastBoughtItem)
)

```