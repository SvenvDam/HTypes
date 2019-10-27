package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.model.Row
import com.svenvandam.htypes.codec.Codec

trait RowCodec[A] extends Codec[Row, A]
