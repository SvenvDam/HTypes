package com.svenvandam.htypes.async

import com.svenvandam.htypes.BaseHbaseTest
import org.apache.hadoop.hbase.client.{Put, Scan}
import org.apache.hadoop.hbase.util.Bytes
import org.scalatest.Matchers._
import scala.concurrent.Await
import scala.concurrent.duration._

class TableSyntaxTest extends BaseHbaseTest {
  import TableSyntax._

  test("it should wraps scan results in future") {
    import scala.concurrent.ExecutionContext.Implicits.global

    val table = getTable()
    val put = new Put("r1").addColumn("cf1", "c1", "v1")
    table.put(put)
    val scan = new Scan().addColumn("cf1", "c1")

    val scanResult = Await.result(table.getScannerAsync(scan), 3 seconds)
    val res = scanResult.asScala.head

    Bytes.toString(res.getRow) shouldBe "r1"
    Bytes.toString(res.getValue("cf1", "c1")) shouldBe "v1"
  }

}
