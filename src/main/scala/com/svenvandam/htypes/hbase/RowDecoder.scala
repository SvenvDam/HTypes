package com.svenvandam.htypes.hbase

import com.svenvandam.htypes.codec.Decoder
import com.svenvandam.htypes.model.Row

trait RowDecoder[A] extends Decoder[Row, A]
