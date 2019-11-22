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
    * [[StreamBackend]] instance for [[Stream]]. Has strict head and lazy tail
    */
  implicit val streamStreamBackend = new StreamBackend[Stream] {
    def getStream[B](iterator: Iterator[B]): Stream[B] = iterator.toStream
  }
}
