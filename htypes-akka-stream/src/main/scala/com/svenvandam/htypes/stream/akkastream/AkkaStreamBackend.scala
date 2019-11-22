package com.svenvandam.htypes.stream.akkastream

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.svenvandam.htypes.stream.StreamBackend

object AkkaStreamBackend {
  type AkkaStream[A] = Source[A, NotUsed] // TODO: use Source directly

  implicit def akkaStreamBackend = new StreamBackend[AkkaStream] {
    def getStream[A](iterator: Iterator[A]): Source[A, NotUsed] = Source.fromIterator(() => iterator)
  }
}
