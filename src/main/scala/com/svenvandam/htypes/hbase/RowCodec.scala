package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.codec.Codec
import com.svenvandam.htypes.model.Row

trait RowCodec[A] extends RowDecoder[A] with RowEncoder[A] with Codec[Row, A]
