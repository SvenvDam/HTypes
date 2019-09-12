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
import Implicits._
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
val table: Table = conn.getTable(...)
val get = new Get("rowKey".getBytes)

val f: Future[Result] = table.getAsync(get)

// continue with the rest of your code
```

### ORM

To easily construct objects from/to a `Result`, HTypes introduces a couple of typeclasses which allow for easy conversions between fetched results and objects.
These typeclasses are:

* `HBaseDecoder`, to create objects from a fetched results
* `HBaseEncoder`, to transform objects to mutation queries
* `HBaseCodec`, a combination of the two above
* `HBaseClassEncoder`, to transform classes to a set of associated columns
