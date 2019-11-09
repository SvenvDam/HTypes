package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.BaseHbaseTest
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.TestTypes._
import com.svenvandam.htypes.bytes.ByteUtils
import org.apache.hadoop.hbase.client.{Get, Put}
import org.scalatest.Matchers._

class PutUtilsTest extends BaseHbaseTest {
  import PutUtilsTest._
  import PutUtils._

  test("createFrom should create a Put and add values to it") {
    implicit val encoder = userEncoderNoTimestamp

    val table = getTable(Seq("profile"))

    val put = createFrom(alice)

    table.put(put)

    val result = table.get(new Get(ByteUtils.toBytes("id")))

    result.getValue(nameColumn.family, nameColumn.qualifier).as[String] shouldBe Some("Alice")
    result.getValue(ageColumn.family, ageColumn.qualifier).as[Int] shouldBe Some(30)
  }

  test("addValues should add values to an existing Put") {
    implicit val encoder = userEncoderNoTimestamp

    val table = getTable(Seq("profile"))

    val put = addValues(new Put(ByteUtils.toBytes("id")))(alice)

    table.put(put)

    val result = table.get(new Get(ByteUtils.toBytes("id")))

    result.getValue(nameColumn.family, nameColumn.qualifier).as[String] shouldBe Some("Alice")
    result.getValue(ageColumn.family, ageColumn.qualifier).as[Int] shouldBe Some(30)
  }

}

object PutUtilsTest {
  def alice = User("id", "Alice", 30)
}
