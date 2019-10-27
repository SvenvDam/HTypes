package com.svenvandam.htypes.converters

import com.svenvandam.htypes.BaseHbaseTest
import com.svenvandam.htypes.hbase.{ColumnEncoder, RowEncoder}
import com.svenvandam.htypes.model.{CellValue, Column, Row}
import org.apache.hadoop.hbase.client.{Get, Put, Scan}
import org.apache.hadoop.hbase.util.Bytes
import org.scalatest.Matchers._

class QuerySyntaxTest extends BaseHbaseTest {
  import QuerySyntax._
  import QuerySyntaxTest._

  implicit val userEncoder = new RowEncoder[User] {
    override def encode(user: User): Row =
      Row(
        user.id,
        Map(
          Column("profile", "name") -> CellValue(user.name, None),
          Column("profile", "age") -> CellValue(user.age.toString, None)
        )
      )
  }

  implicit val userClassEncoder = new ColumnEncoder[User] {
    def getColumns = Set(
      Column("profile", "name"),
      Column("profile", "age")
    )
  }

  test("it should encode a User to a Put") {
    val table = getTable(families = Array("profile"))
    val put = PutUtils.createFrom(User("abc", "Sven", 24))
    table.put(put)

    val result = table.get(new Get("abc"))

    Bytes.toString(result.getRow) shouldBe "abc"
    Bytes.toString(result.getValue("profile", "name")) shouldBe "Sven"
    Bytes.toString(result.getValue("profile", "age")) shouldBe "24"
  }

  test("it should encode a User to a Delete") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", "Sven")
        .addColumn("profile", "age", "24")
        .addColumn("profile", "col", "xx")
    )

    val delete = DeleteUtils.createFrom(User("abc", "Sven", 24))

    table.delete(delete)

    val result = table.get(new Get("abc"))

    Bytes.toString(result.getRow) shouldBe "abc"
    Bytes.toString(result.getValue("profile", "name")) shouldBe null
    Bytes.toString(result.getValue("profile", "age")) shouldBe null
    Bytes.toString(result.getValue("profile", "col")) shouldBe "xx"
  }

  test("it should add user fields to a Scan") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", "Sven")
        .addColumn("profile", "age", "24")
    )

    val scan = new Scan().from[User]

    val result = table.getScanner(scan).asScala.head
    result.getValue("profile", "name") shouldBe "Sven".getBytes
    result.getRow shouldBe "abc".getBytes
  }

  test("it should add user fields to a Get") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", "Sven")
        .addColumn("profile", "age", "24")
    )

    val get = new Get("abc").from[User]

    val result = table.get(get)
    result.getValue("profile", "name") shouldBe "Sven".getBytes
  }
}

object QuerySyntaxTest {
  case class User(id: String, name: String, age: Int)
}
