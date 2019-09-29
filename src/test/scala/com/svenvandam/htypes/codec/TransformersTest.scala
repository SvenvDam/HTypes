package com.svenvandam.htypes.codec

import com.svenvandam.htypes.model.{CellValue, Column, Row}
import org.scalatest.FunSuite
import cats.implicits._
import org.apache.hadoop.hbase.util.Bytes
import org.scalatest.Matchers._

class TransformersTest extends FunSuite {
  import Transformers._
  import TransformersTest._

  test("it should map a decoder") {
    val userDecoder = userNoIdDecoder.map { noId =>
      User("testId", noId.name, noId.age)
    }

    userDecoder.decode(testRow) shouldBe Some(testUser)
  }

  test("it should contramap an encoder") {
    val userEncoder = userNoIdEncoder.contramap[User] { user =>
      UserNoId(
        user.name,
        user.age
      )
    }

    val result = userEncoder.encode(testUser)
    result.key shouldBe testRow.key
    result.values.forall {
      case (col, cellval) => testRow.values(col).value.sameElements(cellval.value)
    }
  }

  test("it should bimap a codec") {
    val userCodec =
      userNoIdCodec
        .imap(noId => User("testId", noId.name, noId.age))(user => UserNoId(user.name, user.age))

    val result = userCodec.encode(testUser)
    result.key shouldBe testRow.key
    result.values.forall {
      case (col, cellval) => testRow.values(col).value.sameElements(cellval.value)
    }
    userCodec.decode(testRow) shouldBe Some(testUser)
  }
}

object TransformersTest {
  case class User(id: String, name: String, age: Int)
  case class UserNoId(name: String, age: Int)

  implicit val userNoIdDecoder = new HBaseDecoder[UserNoId] {
    def decode(row: Row): Option[UserNoId] = for {
      name <- row.values.get(Column("profile", "name"))
      age  <- row.values.get(Column("profile", "age"))
    } yield UserNoId(
      Bytes.toString(name.value),
      Bytes.toString(age.value).toInt
    )
  }

  implicit val userNoIdEncoder = new HBaseEncoder[UserNoId] {
    def encode(user: UserNoId) = Row(
      "testId",
      Map(
        Column("profile", "name") -> CellValue(user.name.getBytes),
        Column("profile", "age") -> CellValue(user.age.toString.getBytes)
      )
    )
  }

  implicit val userNoIdCodec = new HBaseCodec[UserNoId] {
    def decode(row: Row): Option[UserNoId] = userNoIdDecoder.decode(row)

    def encode(noId: UserNoId): Row = userNoIdEncoder.encode(noId)
  }

  val testRow = Row(
    "testId",
    Map(
      Column("profile", "name") -> CellValue("Sven".getBytes),
      Column("profile", "age") -> CellValue("24".getBytes)
    )
  )

  val testUser = User("testId", "Sven", 24)
}
