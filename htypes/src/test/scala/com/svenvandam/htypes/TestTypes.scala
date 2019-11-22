package com.svenvandam.htypes

import com.svenvandam.htypes.hbase.{RowDecoder, RowEncoder}
import com.svenvandam.htypes.model.{CellValue, Column, Row}
import com.svenvandam.htypes.Implicits._

object TestTypes {

  val ageColumn = Column("profile", "age")
  val nameColumn = Column("profile", "name")
  val userColumns = Set(nameColumn, ageColumn)

  case class User(id: String, name: String, age: Int)

  def decodeUser(row: Row): Option[User] = for {
    id                      <- row.key.as[String]
    CellValue(ageBytes, _)  <- row.values.get(ageColumn)
    age                     <- ageBytes.as[Int]
    CellValue(nameBytes, _) <- row.values.get(nameColumn)
    name                    <- nameBytes.as[String]
  } yield User(id, name, age)

  def encodeUser(timestamp: Option[Long])(user: User): Row = Row(
    user.id,
    Map(
      ageColumn -> CellValue(user.age, timestamp),
      nameColumn -> CellValue(user.name, timestamp)
    )
  )

  val userEncoderNoTimestamp = RowEncoder(encodeUser(None))

  def userEncoderWithTimestamp(timestamp: Long) = RowEncoder(encodeUser(Some(timestamp)))

  val userDecoder = RowDecoder(decodeUser, userColumns)
}
