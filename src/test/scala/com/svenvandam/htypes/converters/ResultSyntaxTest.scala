package com.svenvandam.htypes.converters

import com.svenvandam.htypes.BaseHbaseTest
import com.svenvandam.htypes.codec.HBaseDecoder
import com.svenvandam.htypes.model.{Column, Row}
import org.apache.hadoop.hbase.client.{Get, Put, Scan}
import org.apache.hadoop.hbase.util.Bytes
import org.scalatest.Matchers._
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class ResultSyntaxTest extends BaseHbaseTest {
  import ResultSyntax._
  import ResultSyntaxTest._

  implicit val userDecoder = new HBaseDecoder[User] {
    def decode(row: Row): Option[User] = for {
      name <- row.values.get(Column("profile", "name"))
      age  <- row.values.get(Column("profile", "age"))
    } yield User(
      row.key,
      Bytes.toString(name.value),
      Bytes.toString(age.value).toInt
    )
  }

  test("it should construct a user from a Result") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", 1, "Sven")
        .addColumn("profile", "age", 1, "24")
    )

    table.get(new Get("abc")).as[User] shouldBe List((User("abc", "Sven", 24), 1))
  }

  test("it should construct a user from a Future[Result]") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", 1, "Sven")
        .addColumn("profile", "age", 1, "24")
    )

    val result = Await.result(Future(table.get(new Get("abc"))).as[User], 1 seconds)
    result shouldBe List((User("abc", "Sven", 24), 1))
  }

  test("it should handle missing data in Result") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", 1, "Sven")
    )

    table.get(new Get("abc")).as[User] shouldBe List.empty
  }

  test("it should handle updated values in Result") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", 1, "Sven")
        .addColumn("profile", "age", 2, "24")
        .addColumn("profile", "age", 3, "25")
    )

    table.get(new Get("abc").readAllVersions).as[User] shouldBe List(
      (User("abc", "Sven", 25), 3),
      (User("abc", "Sven", 24), 2)
    )
  }

  test("it should produce one instance per timestamp") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", 2, "Sven")
        .addColumn("profile", "age", 2, "25")
        .addColumn("profile", "age", 1, "24")
    )

    table.get(new Get("abc").readAllVersions).as[User] shouldBe List((User("abc", "Sven", 25), 2))
  }

  test("it should construct multiple users using Scan") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", 1, "Sven")
        .addColumn("profile", "age", 1, "24")
    )
    table.put(
      new Put("xyz")
        .addColumn("profile", "name", 1, "Jack")
        .addColumn("profile", "age", 1, "23")
    )

    table.getScanner(new Scan()).as[User] shouldBe List(
      List((User("abc", "Sven", 24), 1)),
      List((User("xyz", "Jack", 23), 1))
    )

  }
}

object ResultSyntaxTest {
  case class User(id: String, name: String, age: Int)
}
