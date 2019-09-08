package com.svenvandam.htypes.converters

import com.svenvandam.htypes.BaseHbaseTest
import com.svenvandam.htypes.model.{CellValue, Row, Column}
import org.apache.hadoop.hbase.client.{Put, Get}
import org.apache.hadoop.hbase.util.Bytes
import org.scalatest.Matchers._

class MutationConvertersTest extends BaseHbaseTest {
  import MutationConverters._
  import MutationConvertersTest._

  implicit val userEncoder = new HBaseEncoder[User] {
    override def encode(user: User): Row =
      Row(
        user.id,
        Map(
          Column("profile", "name") -> CellValue(user.name, None),
          Column("profile", "age") -> CellValue(user.age.toString, None),
        )
      )
  }

  test("it should encode a User to a Put") {
    val table = getTable(families = Array("profile"))
    table.put(User("abc", "Sven", 24))

    val result = table.get(new Get("abc"))

    Bytes.toString(result.getRow) shouldBe "abc"
    Bytes.toString(result.getValue("profile", "name")) shouldBe "Sven"
    Bytes.toString(result.getValue("profile", "age")) shouldBe "24"
  }

  test("it should encode a List of Users to Puts") {
    val puts = List(User("abc", "Sven", 24), User("xyz", "Jack", 23))

    val table = getTable(families = Array("profile"))
    table.put(puts)

    Bytes.toString(table.get(new Get("abc")).getValue("profile", "name")) shouldBe "Sven"
    Bytes.toString(table.get(new Get("xyz")).getValue("profile", "name")) shouldBe "Jack"
  }

  test("it should encode a User to a Delete") {
    val table = getTable(families = Array("profile"))
    table.put(
      new Put("abc")
        .addColumn("profile", "name", "Sven")
        .addColumn("profile", "age", "24")
        .addColumn("profile", "col", "xx")
    )

    table.delete(User("abc", "Sven", 24))

    val result = table.get(new Get("abc"))

    Bytes.toString(result.getRow) shouldBe "abc"
    Bytes.toString(result.getValue("profile", "name")) shouldBe null
    Bytes.toString(result.getValue("profile", "age")) shouldBe null
    Bytes.toString(result.getValue("profile", "col")) shouldBe "xx"
  }

  test("it should encode a List of Users to Delete") {
    val table = getTable(families = Array("profile"))
    val deletes = List(User("abc", "Sven", 24), User("xyz", "Jack", 23))

    table.put(
      new Put("abc")
        .addColumn("profile", "name", "Sven")
        .addColumn("profile", "age", "24")
        .addColumn("profile", "col", "xx")
    )
    table.put(
      new Put("xyz")
        .addColumn("profile", "name", "Jack")
        .addColumn("profile", "age", "23")
        .addColumn("profile", "col", "yy")
    )

    table.delete(deletes)

    val res1 = table.get(new Get("abc"))
    val res2 = table.get(new Get("xyz"))

    Bytes.toString(res1.getRow) shouldBe "abc"
    Bytes.toString(res1.getValue("profile", "name")) shouldBe null
    Bytes.toString(res1.getValue("profile", "age")) shouldBe null
    Bytes.toString(res1.getValue("profile", "col")) shouldBe "xx"
    Bytes.toString(res2.getRow) shouldBe "xyz"
    Bytes.toString(res2.getValue("profile", "name")) shouldBe null
    Bytes.toString(res2.getValue("profile", "age")) shouldBe null
    Bytes.toString(res2.getValue("profile", "col")) shouldBe "yy"
  }
}

object MutationConvertersTest {
  case class User(id: String, name: String, age: Int)
}
