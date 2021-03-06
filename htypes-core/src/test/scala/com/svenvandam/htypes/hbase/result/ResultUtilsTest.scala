package com.svenvandam.htypes.hbase.result

import com.svenvandam.htypes.BaseHbaseTest
import com.svenvandam.htypes.TestTypes._
import com.svenvandam.htypes.bytes.ByteUtils
import org.apache.hadoop.hbase.client.{Get, Put}
import com.svenvandam.htypes.Implicits._
import org.scalatest.Matchers._

class ResultUtilsTest extends BaseHbaseTest {
  import ResultUtilsTest._
  import com.svenvandam.htypes.hbase.result.ResultUtils._

  test("as should construct a sequence of objects ordered by timestamp from a Result") {
    implicit val decoder = userDecoder
    val table = getTable(families = Array("profile"))
    table.put(
      new Put(ByteUtils.toBytes("id"))
        .addColumn(nameColumn.family, nameColumn.qualifier, 1, ByteUtils.toBytes(alice.name))
        .addColumn(ageColumn.family, ageColumn.qualifier, 1, ByteUtils.toBytes(alice.age))
        .addColumn(ageColumn.family, ageColumn.qualifier, 2, ByteUtils.toBytes(alice.age + 1))
    )

    val result = table.get(new Get(ByteUtils.toBytes(alice.id)).readAllVersions())

    resultAs[User](result) shouldBe Seq((alice.copy(age = 31), 2), (alice, 1))
  }

  test("resultAs should handle missing data") {
    implicit val decoder = userDecoder
    val table = getTable(families = Array("profile"))
    table.put(
      new Put(ByteUtils.toBytes("id"))
        .addColumn(nameColumn.family, nameColumn.qualifier, 1, ByteUtils.toBytes(alice.name))
        .addColumn(ageColumn.family, ageColumn.qualifier, 2, ByteUtils.toBytes(alice.age))
    )

    val result = table.get(new Get(ByteUtils.toBytes(alice.id)).readAllVersions())

    resultAs[User](result) shouldBe Seq((alice, 2))
  }

}

object ResultUtilsTest {
  val alice = User("id", "Alice", 30)
  val bob = User("id2", "Bob", 31)
}
