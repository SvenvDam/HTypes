[![Build Status](https://travis-ci.org/SvenvDam/HTypes.svg?branch=master)](https://travis-ci.org/SvenvDam/HTypes)

# HTypes
> A type-safe, asynchronous, lightweight Scala extension to the HBase API 

HTypes is a simple Scala extension to the Apache HBase API.
It adds two improvements to the existing API:

* Asynchronous execution of I/O operations
* Object-relation mapping (ORM)

## Installation

TODO

## Usage
To get started easily, simply import all members of `Implicits`.

```scala
import com.svenvandam.htypes.Implicits._
```

This will make all extensions to the HBase API available to you.

### Asynchronous interactions

HTypes provides asynchronous operations by extending the HBase `Table` class with the following methods:
* `getScannerAsync(scan: Scan)`
* `getAsync(get: Get)`
* `putAsync(put: Put)`
* `deleteAsyncAsync(delete: Delete)`

The behaviour of these methods is almost self-explanatory.
It calls the related methods on table and wraps the result in a `Future`.
This can be used as follows:

```scala
import concurrent.ExecutionContext.Implicits.global
import org.apache.hadoop.hbase.client._
import com.svenvandam.htypes.async.TableSyntax._ // for additional table syntax

val table: Table = conn.getTable(...)
val get = new Get("rowKey".getBytes)

val f: Future[Result] = table.getAsync(get)

// continue with the rest of your code
```

### ORM

To easily construct objects from/to a `Result`, HTypes introduces a couple of typeclasses which allow for easy conversions between fetched results and objects.
These typeclasses are:

* `HBaseDecoder`, to create objects from a fetched results.
* `HBaseEncoder`, to transform objects to mutation queries
* `HBaseCodec`, a combination of the two above
* `HBaseClassEncoder`, to transform classes to a set of associated columns

The encoder and decoder describe mappings between objects of class `A` and objects of class `Row`.
`Row` is defined as follows:

```scala
case class Column(family: String, qualifier: String)
case class CellValue(value: Array[Byte], timestamp: Long)

case class Row(key: String, values: Map[Column, CellValue])
```

The `HBaseClassEncoder` does not describe how to construct a `Row` but rather which `Column`'s should be fetched to instantiate objects.
When implicit typeclass instances are in scope, the syntax of the HBAse client API is be enriched.
`Result` and `ResultScanner` need a `HBaseDecoder[T]` to construct objects of type `T`.
`Get` and `Scan` need a `HBaseClassEncoder[T]` to add the columns required to construct a `T` to the query.
`Put` and `Delete` need a `HBaseEncoder[T]` to add a `T` to  the query.

An example:

```scala
case class User(name: String, age: Int)

implicit val userDecoder = new HBaseDecoder[User] {
  def decoder(row: Row): Option[User] = for {
    ageB <- row.values.get(Column("profile", "age"))
  } yield User(
    row.key,
    Bytes.toInt(ageB)
  )
}

val table = conn.getTable(...)
val get = new Get("Jack".getBytes).addColumn("profile".getBytes, "age".getBytes)

val user: Iterable[(User, Long)] = table.get(get).as[User]
```

Note that we get an `Iterable[(User, Long)]`. We get an `User` at each timestamp where we had enough data to construct one!

#### Transforming typeclasses

For if you are in to that sort of thing, `Transformers` defines functor instances from the `cats` library.
Concretely, `HBaseDecoder` has a `Functor`, `HBaseEncoder` has a `Contravariant` and `HBaseCodec` has an `Invariant`.