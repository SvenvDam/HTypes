package com.svenvandam.htypes.converters

import com.svenvandam.htypes.BaseHbaseTest
import org.apache.hadoop.hbase.client.{Get, Put, Scan}
import org.scalatest.Matchers._
import org.apache.hadoop.hbase.util.Bytes
import com.svenvandam.htypes.TestTypes._

class ResultSyntaxTest extends BaseHbaseTest {
  import com.svenvandam.htypes.hbase.ResultSyntax._

  test("it should construct a user from a Result") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc".getBytes)
        .addColumn("profile".getBytes, "name".getBytes, 1, "Alice".getBytes)
        .addColumn("profile".getBytes, "age".getBytes, 1, Bytes.toBytes(24))
    )

    table.get(new Get("abc".getBytes)).as[User] shouldBe List((User("abc", "Alice", 24), 1))
  }

  test("it should handle missing data in Result") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc".getBytes)
        .addColumn("profile".getBytes, "name".getBytes, 1, "Alice".getBytes)
    )

    table.get(new Get("abc".getBytes)).as[User] shouldBe List.empty
  }

  test("it should handle updated values in Result") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc".getBytes)
        .addColumn("profile".getBytes, "name".getBytes, 1, "Alice".getBytes)
        .addColumn("profile".getBytes, "age".getBytes, 2, Bytes.toBytes(24))
        .addColumn("profile".getBytes, "age".getBytes, 3, Bytes.toBytes(25))
    )

    table.get(new Get("abc".getBytes).readAllVersions).as[User] shouldBe List(
      (User("abc", "Alice", 25), 3),
      (User("abc", "Alice", 24), 2)
    )
  }

  test("it should produce one instance per timestamp") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc".getBytes)
        .addColumn("profile".getBytes, "name".getBytes, 2, "Alice".getBytes)
        .addColumn("profile".getBytes, "age".getBytes, 2, Bytes.toBytes(25))
        .addColumn("profile".getBytes, "age".getBytes, 1, Bytes.toBytes(24))
    )

    table.get(new Get("abc".getBytes).readAllVersions).as[User] shouldBe List((User("abc", "Alice", 25), 2))
  }

  test("it should construct multiple users using Scan") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc".getBytes)
        .addColumn("profile".getBytes, "name".getBytes, 1, "Alice".getBytes)
        .addColumn("profile".getBytes, "age".getBytes, 1, Bytes.toBytes(24))
    )
    table.put(
      new Put("xyz".getBytes)
        .addColumn("profile".getBytes, "name".getBytes, 1, "Jack".getBytes)
        .addColumn("profile".getBytes, "age".getBytes, 1, Bytes.toBytes(23))
    )

    table.getScanner(new Scan()).as[User] shouldBe List(
      List((User("abc", "Alice", 24), 1)),
      List((User("xyz", "Jack", 23), 1))
    )

  }
}
