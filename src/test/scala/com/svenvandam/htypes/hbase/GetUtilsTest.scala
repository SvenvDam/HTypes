package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.BaseHbaseTest
import com.svenvandam.htypes.bytes.ByteUtils
import org.apache.hadoop.hbase.client.{Get, Put}
import org.scalatest.Matchers._
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.TestTypes._

class GetUtilsTest extends BaseHbaseTest {
  import GetUtils._
  test("addColumns should bind columns to a Get") {
    implicit val decoder = userDecoder
    val get = new Get(ByteUtils.toBytes("id"))
    addColumns[User](get)

    val table = getTable(Seq("profile"))

    table.put(
      new Put(ByteUtils.toBytes("id"))
        .addColumn(nameColumn.family, nameColumn.qualifier, ByteUtils.toBytes("Alice"))
        .addColumn(ageColumn.family, ageColumn.qualifier, ByteUtils.toBytes(30))
    )

    val result = table.get(get)

    result.getValue(nameColumn.family, nameColumn.qualifier).as[String] shouldBe Some("Alice")
    result.getValue(ageColumn.family, ageColumn.qualifier).as[Int] shouldBe Some(30)

  }
}
