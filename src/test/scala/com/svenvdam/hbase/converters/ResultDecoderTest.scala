package com.svenvdam.hbase.converters

import com.svenvdam.hbase.BaseHbaseTest
import com.svenvdam.hbase.model.{DecodedValue, Row, Column}
import org.apache.hadoop.hbase.KeyValue
import org.apache.hadoop.hbase.client.Result
import org.apache.hadoop.hbase.util.Bytes
import org.scalatest.Matchers._
import scala.concurrent.{Future, Await}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class ResultDecoderTest extends BaseHbaseTest {
  import ResultDecoder._
  import ResultDecoderTest._

  implicit val userDecoder = new HBaseDecoder[User] {
    def decode(row: Row): Option[User] = for {
      name <- row.values.get(Column("profile", "name"))
      age <- row.values.get(Column("profile", "age"))
    } yield User(
      row.key,
      Bytes.toString(name.value),
      Bytes.toString(age.value).toInt
    )
  }

  test("it should construct a user from a Result") {
    val kv = Array(
      new KeyValue("abc".getBytes, "profile".getBytes, "name".getBytes, 1, "Sven".getBytes),
      new KeyValue("abc".getBytes, "profile".getBytes, "age".getBytes, 1, "24".getBytes),
    )

    new Result(kv).as[User] shouldBe List(DecodedValue(User("abc", "Sven", 24), 1))
  }

  test("it should construct a user from a Future[Result]") {
    val kv = Array(
      new KeyValue("abc".getBytes, "profile".getBytes, "name".getBytes, 1, "Sven".getBytes),
      new KeyValue("abc".getBytes, "profile".getBytes, "age".getBytes, 1, "24".getBytes),
    )

    val result = Await.result(Future(new Result(kv)).as[User], 1 second)
    result shouldBe List(DecodedValue(User("abc", "Sven", 24), 1))
  }

  test("it should handle missing data in Result") {
    val kv = Array(
      new KeyValue("abc".getBytes, "profile".getBytes, "name".getBytes, 1, "Sven".getBytes),
    )

    new Result(kv).as[User] shouldBe List.empty
  }

  test("it should handle updated values in Result") {
    val kv = Array(
      new KeyValue("abc".getBytes, "profile".getBytes, "age".getBytes, 3, "25".getBytes),
      new KeyValue("abc".getBytes, "profile".getBytes, "age".getBytes, 2, "24".getBytes),
      new KeyValue("abc".getBytes, "profile".getBytes, "name".getBytes, 1, "Sven".getBytes),
    )

    new Result(kv).as[User] shouldBe List(
      DecodedValue(User("abc", "Sven", 25), 3),
      DecodedValue(User("abc", "Sven", 24), 2),
    )
  }

  test("it should get one user using Get") {
    ???
  }

  test("it should construct multiple users using Scan") {
    ???
  }
}

object ResultDecoderTest {
  case class User(id: String, name: String, age: Int)

}
