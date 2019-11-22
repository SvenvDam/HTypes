package com.svenvandam.htypes.hbase.query

import com.svenvandam.htypes.BaseHbaseTest
import org.apache.hadoop.hbase.client.{Delete, Get, Put}
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.TestTypes._
import com.svenvandam.htypes.bytes.ByteUtils
import org.scalatest.Matchers._

class DeleteUtilsTest extends BaseHbaseTest {
  import DeleteUtilsTest._
  import com.svenvandam.htypes.hbase.query.DeleteUtils._

  test("addColumnsSingleVersion should delete the most recent value") {
    implicit val encoder = userEncoderNoTimestamp

    val delete = addColumnsSingleVersion(baseDelete)(alice)

    val table = getTable(Seq("profile"))

    table.put(alicePut())

    table.delete(delete)

    val result = table.get(aliceGet.setTimeRange(0, 2))

    result.getValue(nameColumn.family, nameColumn.qualifier) shouldBe null
    result.getValue(ageColumn.family, ageColumn.qualifier) shouldBe null
  }

  test("addColumnsSingleVersion should delete one value at a timestamp from CellValue") {
    implicit val encoder = userEncoderWithTimestamp(1)

    val delete = addColumnsSingleVersion(baseDelete)(alice)

    val table = getTable(Seq("profile"))

    table.put(alicePut(timestamp = 1))
    table.put(alicePut(timestamp = 3))

    table.delete(delete)

    val result1 = table.get(aliceGet.setTimeRange(0, 2))
    result1.getValue(nameColumn.family, nameColumn.qualifier) shouldBe null
    result1.getValue(ageColumn.family, ageColumn.qualifier) shouldBe null

    val result2 = table.get(aliceGet.setTimeRange(0, 4))
    ByteUtils.fromBytes[String](result2.getValue(nameColumn.family, nameColumn.qualifier)) shouldBe Some(alice.name)
    ByteUtils.fromBytes[Int](result2.getValue(ageColumn.family, ageColumn.qualifier)) shouldBe Some(alice.age)
  }

  test("addColumnsAllVersion should delete all values") {
    implicit val encoder = userEncoderNoTimestamp

    val delete = addColumnsAllVersions(baseDelete)(alice)

    val table = getTable(Seq("profile"))

    table.put(alicePut())
    table.put(alicePut(timestamp = 1))

    table.delete(delete)

    val result = table.get(aliceGet.setTimeRange(0, 2))

    result.getValue(nameColumn.family, nameColumn.qualifier) shouldBe null
    result.getValue(ageColumn.family, ageColumn.qualifier) shouldBe null
  }

  test("addColumnsAllVersion should delete all versions up to timestamp from CellValue") {
    implicit val encoder = userEncoderWithTimestamp(2)

    val delete = addColumnsAllVersions(baseDelete)(alice)

    val table = getTable(Seq("profile"))

    table.put(alicePut())
    table.put(alicePut(timestamp = 1))
    table.put(alicePut(timestamp = 4))

    table.delete(delete)

    val result1 = table.get(aliceGet.setTimeRange(0, 3))
    result1.getValue(nameColumn.family, nameColumn.qualifier) shouldBe null
    result1.getValue(ageColumn.family, ageColumn.qualifier) shouldBe null

    val result2 = table.get(aliceGet.setTimeRange(0, 5))
    ByteUtils.fromBytes[String](result2.getValue(nameColumn.family, nameColumn.qualifier)) shouldBe Some(alice.name)
    ByteUtils.fromBytes[Int](result2.getValue(ageColumn.family, ageColumn.qualifier)) shouldBe Some(alice.age)
  }

  test("addColumnsAllVersion should delete all versions up to timestamp set before adding columns") {
    implicit val encoder = userEncoderNoTimestamp

    val delete = addColumnsAllVersions(baseDelete.setTimestamp(2))(alice)

    val table = getTable(Seq("profile"))

    table.put(alicePut())
    table.put(alicePut(timestamp = 1))
    table.put(alicePut(timestamp = 4))

    table.delete(delete)

    val result1 = table.get(aliceGet.setTimeRange(0, 3))
    result1.getValue(nameColumn.family, nameColumn.qualifier) shouldBe null
    result1.getValue(ageColumn.family, ageColumn.qualifier) shouldBe null

    val result2 = table.get(aliceGet.setTimeRange(0, 5))
    ByteUtils.fromBytes[String](result2.getValue(nameColumn.family, nameColumn.qualifier)) shouldBe Some(alice.name)
    ByteUtils.fromBytes[Int](result2.getValue(ageColumn.family, ageColumn.qualifier)) shouldBe Some(alice.age)
  }
}

object DeleteUtilsTest {
  val id = "id1"
  def baseDelete = new Delete(ByteUtils.toBytes(id))

  val alice = User(id, "Alice", 30)

  def alicePut(name: String = "Alice", age: Int = 30, timestamp: Long = 0) =
    new Put(ByteUtils.toBytes(id))
      .addColumn(nameColumn.family, nameColumn.qualifier, timestamp, ByteUtils.toBytes(name))
      .addColumn(ageColumn.family, ageColumn.qualifier, timestamp, ByteUtils.toBytes(age))

  val aliceGet = new Get(ByteUtils.toBytes(id)).readAllVersions()
}
