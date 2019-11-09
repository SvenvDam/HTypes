package com.svenvandam.htypes.converters

import com.svenvandam.htypes.BaseHbaseTest
import org.apache.hadoop.hbase.client.{Get, Put, Scan}
import org.apache.hadoop.hbase.util.Bytes
import org.scalatest.Matchers._
import com.svenvandam.htypes.TestTypes._
import com.svenvandam.htypes.hbase.{DeleteUtils, PutUtils}

class QuerySyntaxTest extends BaseHbaseTest {
  import com.svenvandam.htypes.hbase.QuerySyntax._

  test("it should encode a User to a Put") {
    val table = getTable(families = Array("profile"))
    val put = PutUtils.createFrom(User("abc", "Alice", 24))
    table.put(put)

    val result = table.get(new Get("abc".getBytes))

    Bytes.toString(result.getRow) shouldBe "abc"
    Bytes.toString(result.getValue("profile".getBytes, "name".getBytes)) shouldBe "Alice"
    Bytes.toInt(result.getValue("profile".getBytes, "age".getBytes)) shouldBe 24
  }

  test("it should encode a User to a Delete") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc".getBytes)
        .addColumn("profile".getBytes, "name".getBytes, "Alice".getBytes)
        .addColumn("profile".getBytes, "age".getBytes, Bytes.toBytes(24))
        .addColumn("profile".getBytes, "col".getBytes, "xx".getBytes)
    )

    val delete = DeleteUtils.createFrom(User("abc", "Alice", 24))

    table.delete(delete)

    val result = table.get(new Get("abc".getBytes))

    Bytes.toString(result.getRow) shouldBe "abc"
    result.getValue("profile".getBytes, "name".getBytes) shouldBe null
    result.getValue("profile".getBytes, "age".getBytes) shouldBe null
    Bytes.toString(result.getValue("profile".getBytes, "col".getBytes)) shouldBe "xx"
  }

  test("it should add user fields to a Scan") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc".getBytes)
        .addColumn("profile".getBytes, "name".getBytes, "Alice".getBytes)
        .addColumn("profile".getBytes, "age".getBytes, Bytes.toBytes(24))
    )

    val scan = new Scan().from[User]

    val result = table.getScanner(scan).asScala.head
    result.getValue("profile".getBytes, "name".getBytes) shouldBe "Alice".getBytes
    result.getRow shouldBe "abc".getBytes
  }

  test("it should add user fields to a Get") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc".getBytes)
        .addColumn("profile".getBytes, "name".getBytes, "Alice".getBytes)
        .addColumn("profile".getBytes, "age".getBytes, Bytes.toBytes(24))
    )

    val get = new Get("abc".getBytes).from[User]

    val result = table.get(get)
    result.getValue("profile".getBytes, "name".getBytes) shouldBe "Alice".getBytes
  }
}
