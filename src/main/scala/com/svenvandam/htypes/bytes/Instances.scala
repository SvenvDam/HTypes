package com.svenvandam.htypes.bytes

import org.apache.hadoop.hbase.util.Bytes

trait Instances {
  implicit val identityByteCodec = ByteCodec[Array[Byte]](identity, identity)
  implicit val stringByteCodec = ByteCodec[String](Bytes.toString, Bytes.toBytes)
  implicit val boolByteCodec = ByteCodec[Boolean](Bytes.toBoolean, Bytes.toBytes)
  implicit val intByteCodec = ByteCodec[Int](Bytes.toInt, Bytes.toBytes)
  implicit val longByteCodec = ByteCodec[Long](Bytes.toLong, Bytes.toBytes)
  implicit val floatByteCodec = ByteCodec[Float](Bytes.toFloat, Bytes.toBytes)
}

object Instances extends Instances
