package com.svenvandam.htypes.stream.akkastream

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.svenvandam.htypes.stream.StreamBackend

object AkkaStreamBackend {

  /**
    * Type alias
    */
  type AkkaStream[A] = Source[A, NotUsed] // TODO: use Source directly

  /**
    * [[StreamBackend]] instance for [[Source]]
    */
  implicit val akkaStreamBackend = new StreamBackend[AkkaStream] {
    def getStream[A](iterator: Iterator[A]): Source[A, NotUsed] = Source.fromIterator(() => iterator)
  }
}
