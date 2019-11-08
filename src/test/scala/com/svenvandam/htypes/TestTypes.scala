package com.svenvandam.htypes

import com.svenvandam.htypes.hbase.RowCodec
import com.svenvandam.htypes.model.{CellValue, Column, Row}
import com.svenvandam.htypes.Implicits._

object TestTypes {

  case class User(id: String, name: String, age: Int)

  implicit val userCodec = new RowCodec[User] {
    private val ageColumn = Column("profile", "age")
    private val nameColumn = Column("profile", "name")

    def getColumns = Set(
      ageColumn,
      nameColumn
    )

    def encode(user: User): Row = Row(
      user.id,
      Map(
        ageColumn -> CellValue(user.age, None),
        nameColumn -> CellValue(user.name, None)
      )
    )

    def decode(row: Row): Option[User] = for {
      id                      <- row.key.as[String]
      CellValue(ageBytes, _)  <- row.values.get(ageColumn)
      age                     <- ageBytes.as[Int]
      CellValue(nameBytes, _) <- row.values.get(nameColumn)
      name                    <- nameBytes.as[String]
    } yield User(id, name, age)
  }
}
