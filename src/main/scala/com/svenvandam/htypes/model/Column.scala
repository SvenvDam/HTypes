package com.svenvandam.htypes.model

import com.svenvandam.htypes.codec.Encoder

case class Column(family: Array[Byte], qualifier: Array[Byte]) {
  override def equals(that: Any): Boolean = that match {
    case Column(fam, qual) => fam.toSeq == family.toSeq && qual.toSeq == qualifier.toSeq
    case _                 => false
  }
}

object Column {
  def apply[A, B](
      family: A,
      qualifier: B
    )(implicit familyEncoder: Encoder[Array[Byte], A],
      qualifierEnCoder: Encoder[Array[Byte], B]
    ) =
    new Column(
      familyEncoder.encode(family),
      qualifierEnCoder.encode(qualifier)
    )

}
