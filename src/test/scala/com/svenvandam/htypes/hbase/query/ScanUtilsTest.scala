package com.svenvandam.htypes.hbase.query

import com.svenvandam.htypes.BaseHbaseTest
import com.svenvandam.htypes.bytes.ByteUtils
import org.apache.hadoop.hbase.client.{Put, Scan}
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.TestTypes._
import org.scalatest.Matchers._

class ScanUtilsTest extends BaseHbaseTest {
  import com.svenvandam.htypes.hbase.query.ScanUtils._

  test("addColumns should add columns to a Scan") {
    implicit val decoder = userDecoder

    val table = getTable(Seq("profile"))

    val scan = addColumns[User](new Scan())

    table.put(
      new Put(ByteUtils.toBytes("id"))
        .addColumn(nameColumn.family, nameColumn.qualifier, ByteUtils.toBytes("Alice"))
        .addColumn(ageColumn.family, ageColumn.qualifier, ByteUtils.toBytes(30))
    )

    val result = table.getScanner(scan).toSeq
    result.size shouldBe 1
    result.head.getValue(nameColumn.family, nameColumn.qualifier).as[String] shouldBe Some("Alice")
    result.head.getValue(ageColumn.family, ageColumn.qualifier).as[Int] shouldBe Some(30)

  }

}
