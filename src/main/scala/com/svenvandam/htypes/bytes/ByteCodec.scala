package com.svenvandam.htypes.bytes

import com.svenvandam.htypes.codec.Codec
import org.apache.hadoop.hbase.util.Bytes

trait ByteCodec[A] extends Codec[Array[Byte], A]

object ByteCodec {
  def apply[A](f: Array[Byte] => A, g: A => Array[Byte]) = Codec[Array[Byte], A](
    ByteDecoder.safeDecode(f),
    g
  )

  implicit val identityByteCodec = ByteCodec[Array[Byte]](identity, identity)
  implicit val stringByteCodec = ByteCodec[String](Bytes.toString, Bytes.toBytes)
  implicit val boolByteCodec = ByteCodec[Boolean](Bytes.toBoolean, Bytes.toBytes)
  implicit val intByteCodec = ByteCodec[Int](Bytes.toInt, Bytes.toBytes)
  implicit val longByteCodec = ByteCodec[Long](Bytes.toLong, Bytes.toBytes)
  implicit val floatByteCodec = ByteCodec[Float](Bytes.toFloat, Bytes.toBytes)
}
