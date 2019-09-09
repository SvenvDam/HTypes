package com.svenvandam.htypes.converters

import com.svenvandam.htypes.model.Column

// TODO: rename
trait HBaseColumnGetter[T] {
  def getColumns: Set[Column]
}
