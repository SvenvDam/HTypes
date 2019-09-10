//package com.svenvandam.htypes.converters
//
//import com.svenvandam.htypes.BaseHbaseTest
//import com.svenvandam.htypes.model.{DecodedValue, Row, Column}
//import org.apache.hadoop.hbase.KeyValue
//import org.apache.hadoop.hbase.client.{Scan, Result, Put, Get}
//import org.apache.hadoop.hbase.util.Bytes
//import org.scalatest.Matchers._
//import scala.concurrent.{Future, Await}
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.duration._
//
//class ResultSyntaxTest extends BaseHbaseTest {
//  import ResultSyntax._
//  import ResultSyntaxTest._
//
//  implicit val userDecoder = new HBaseDecoder[User] {
//    def decode(row: Row): Option[User] = for {
//      name <- row.values.get(Column("profile", "name"))
//      age <- row.values.get(Column("profile", "age"))
//    } yield User(
//      row.key,
//      Bytes.toString(name.value),
//      Bytes.toString(age.value).toInt
//    )
//  }
//
//  test("it should construct a user from a Result") {
//    val kv = Array(
//      new KeyValue("abc", "profile", "name", 1, "Sven"),
//      new KeyValue("abc", "profile", "age", 1, "24"),
//    )
//
//    new Result(kv).as[User] shouldBe List(DecodedValue(User("abc", "Sven", 24), 1))
//  }
//
//  test("it should construct a user from a Future[Result]") {
//    val kv = Array(
//      new KeyValue("abc", "profile", "name", 1, "Sven"),
//      new KeyValue("abc", "profile", "age", 1, "24"),
//    )
//
//    val result = Await.result(Future(new Result(kv)).as[User], 1 second)
//    result shouldBe List(DecodedValue(User("abc", "Sven", 24), 1))
//  }
//
//  test("it should handle missing data in Result") {
//    val kv = Array(
//      new KeyValue("abc", "profile", "name", 1, "Sven"),
//    )
//
//    new Result(kv).as[User] shouldBe List.empty
//  }
//
//  test("it should handle updated values in Result") {
//    val kv = Array(
//      new KeyValue("abc", "profile", "age", 3, "25"),
//      new KeyValue("abc", "profile", "age", 2, "24"),
//      new KeyValue("abc", "profile", "name", 1, "Sven"),
//    )
//
//    new Result(kv).as[User] shouldBe List(
//      DecodedValue(User("abc", "Sven", 25), 3),
//      DecodedValue(User("abc", "Sven", 24), 2),
//    )
//  }
//
//  test("it should get one user using Get") {
//    val table = getTable(families = Array("profile"))
//    table.put(
//      new Put("abc")
//        .addColumn("profile", "name", 1, "Sven")
//        .addColumn("profile", "age", 1, "24")
//    )
//
//    table.get(new Get("abc")).as[User] shouldBe List(DecodedValue(User("abc", "Sven", 24), 1))
//  }
//
//  test("it should construct multiple users using Scan") {
//    val table = getTable(families = Array("profile"))
//    table.put(
//      new Put("abc")
//        .addColumn("profile", "name", 1, "Sven")
//        .addColumn("profile", "age", 1, "24")
//    )
//    table.put(
//      new Put("xyz")
//        .addColumn("profile", "name", 1, "Jack")
//        .addColumn("profile", "age", 1, "23")
//    )
//
//    table.getScanner(new Scan()).as[User] shouldBe List(
//      List(DecodedValue(User("abc", "Sven", 24), 1)),
//      List(DecodedValue(User("xyz", "Jack", 23), 1))
//    )
//
//  }
//}
//
//object ResultSyntaxTest {
//  case class User(id: String, name: String, age: Int)
//}
