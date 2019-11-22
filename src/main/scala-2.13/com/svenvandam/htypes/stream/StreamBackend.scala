package com.svenvandam.htypes.stream

trait StreamBackend[A[_]] {
  def getStream[B](iterator: Iterator[B]): A[B]
}

object StreamBackend {

  /**
    * Identity [[StreamBackend]] instance for [[Iterator]]
    */
  implicit val iteratorStreamBackend = new StreamBackend[Iterator] {
    def getStream[B](iterator: Iterator[B]): Iterator[B] = identity(iterator)
  }

  /**
    * Naive [[StreamBackend]] instance for [[Seq]].
    */
  implicit val seqStreamBackend = new StreamBackend[Seq] {
    def getStream[B](iterator: Iterator[B]): Seq[B] = iterator.toSeq
  }

  /**
    * [[StreamBackend]] instance for [[LazyList]]. Has both a lazy head and tail.
    */
  implicit val lazyListStreamBackend = new StreamBackend[LazyList] {
    def getStream[B](iterator: Iterator[B]): LazyList[B] = LazyList.from(iterator)
  }
}
