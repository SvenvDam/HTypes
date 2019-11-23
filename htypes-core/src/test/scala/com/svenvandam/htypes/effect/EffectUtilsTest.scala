package com.svenvandam.htypes.effect

import com.svenvandam.htypes.{BaseHbaseTest, TestIO}
import com.svenvandam.htypes.bytes.ByteUtils
import org.apache.hadoop.hbase.client.{Put, Get}
import com.svenvandam.htypes.Implicits._
import org.scalatest.Matchers._

class EffectUtilsTest extends BaseHbaseTest {

  test("wrap should wrap query execution in an effect wrapper") {

    val row = ByteUtils.toBytes("r1")
    val family = ByteUtils.toBytes("cf1")
    val qualifier = ByteUtils.toBytes("c1")
    val value = ByteUtils.toBytes("v1")
    val table = getTable(Seq("cf1"))

    val put = new Put(row).addColumn(family, qualifier, value)

    val get = new Get(row)

    import TestIO.ioBackend

    val putIO = EffectUtils.lift(table.put(put)) // construct IO, should not yet execute

    table.get(get).getValue(family, qualifier).as[String] shouldBe None // table should be empty at this point

    putIO.run() // perform the effect only here

    table.get(get).getValue(family, qualifier).as[String] shouldBe Some("v1")

  }
}
