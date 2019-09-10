package com.svenvandam.htypes.converters

import com.svenvandam.htypes.model.Column

trait HBaseClassEncoder[T] {
  def getColumns: Set[Column]
}
