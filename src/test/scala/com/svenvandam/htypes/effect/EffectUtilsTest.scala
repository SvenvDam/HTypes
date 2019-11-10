package com.svenvandam.htypes.effect

import com.svenvandam.htypes.BaseHbaseTest
import com.svenvandam.htypes.bytes.ByteUtils
import org.apache.hadoop.hbase.client.{Get, Put}
import com.svenvandam.htypes.Implicits._
import org.scalatest.Matchers._

class EffectUtilsTest extends BaseHbaseTest {
  import EffectUtilsTest._

  test("wrap should wrap query execution in an effect wrapper") {
    implicit val ioBackend = new EffectBackend[TestIO] {
      def wrap[A](a: => A): TestIO[A] =
        TestIO(() => a)
    }

    val row = ByteUtils.toBytes("r1")
    val family = ByteUtils.toBytes("cf1")
    val qualifier = ByteUtils.toBytes("c1")
    val value = ByteUtils.toBytes("v1")
    val table = getTable(Seq("cf1"))

    val putAsync = EffectUtils.wrap[Put, Unit, TestIO](table.put) _

    val put = new Put(row)
      .addColumn(family, qualifier, value)

    val get = new Get(row)

    val putIO = putAsync(put) // construct IO, should not yet execute

    table.get(get).getValue(family, qualifier).as[String] shouldBe None // table should be empty at this point

    putIO.run() // perform the effect only here

    table.get(get).getValue(family, qualifier).as[String] shouldBe Some("v1")

  }
}

object EffectUtilsTest {
  case class TestIO[A](action: () => A) {
    def run() = action()
  }

}
