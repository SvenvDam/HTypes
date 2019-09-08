package com.svenvdam.hbase.async

import com.svenvdam.hbase.BaseHbaseTest
import org.apache.hadoop.hbase.client.{Scan, Put}
import org.apache.hadoop.hbase.util.Bytes
import org.scalatest.Matchers._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.jdk.CollectionConverters._

class AsyncTableTest extends BaseHbaseTest {
  import AsyncTable._

  test("it should wraps scan results in future") {
    import scala.concurrent.ExecutionContext.Implicits.global

    val table = getTable()
    val put = new Put("r1".getBytes).addColumn("cf1".getBytes, "c1".getBytes, "v1".getBytes)
    table.put(put)
    val scan = new Scan().addColumn("cf1".getBytes, "c1".getBytes)


    val scanResult = Await.result(table.scanAsync(scan), 3 seconds)
    val res = scanResult.asScala.head

    Bytes.toString(res.getRow) shouldBe "r1"
    Bytes.toString(res.getValue("cf1".getBytes, "c1".getBytes)) shouldBe "v1"
  }
}
