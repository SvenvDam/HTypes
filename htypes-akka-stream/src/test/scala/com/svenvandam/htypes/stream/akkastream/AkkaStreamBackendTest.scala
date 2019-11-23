package com.svenvandam.htypes.stream.akkastream

import akka.actor.ActorSystem
import akka.stream.{Materializer, ActorMaterializer}
import akka.stream.testkit.scaladsl.TestSink
import com.svenvandam.htypes.BaseHbaseTest
import com.svenvandam.htypes.bytes.ByteUtils
import org.apache.hadoop.hbase.client.{Scan, Result, Put}
import com.svenvandam.htypes.Implicits._

class AkkaStreamBackendTest extends BaseHbaseTest {
  import AkkaStreamBackend._
  implicit val system = ActorSystem("AkkaStreamBackendTest")
  implicit val mat = ActorMaterializer()

  test("it should stream in Results from a ResultScanner") {
    val table = getTable(Seq("cf"))

    val put1 = new Put(ByteUtils.toBytes("id1"))
      .addColumn(ByteUtils.toBytes("cf"), ByteUtils.toBytes("c1"), 0, ByteUtils.toBytes("v1"))

    val put2 = new Put(ByteUtils.toBytes("id2"))
      .addColumn(ByteUtils.toBytes("cf"), ByteUtils.toBytes("c1"), 0, ByteUtils.toBytes("v2"))

    table.put(put1)
    table.put(put2)

    val resultSource = table
      .getScanner(new Scan())
      .stream[AkkaStream]
      .map { res: Result =>
        ByteUtils.fromBytes[String](
          res.getValue(ByteUtils.toBytes("cf"), ByteUtils.toBytes("c1"))
        )
      }

    resultSource
      .runWith(TestSink.probe[Option[String]])
      .request(2)
      .expectNext(Some("v1"), Some("v2"))
      .expectComplete()

  }
}
