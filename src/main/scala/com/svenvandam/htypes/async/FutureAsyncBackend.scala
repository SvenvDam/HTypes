package com.svenvandam.htypes.async

import scala.concurrent.{blocking, ExecutionContext, Future}

/**
  * Future based implementation of AsyncBackend.
  * Executes the query eagerly in a thread provided by an ExecutionContext
  */
case class FutureAsyncBackend()(implicit ec: ExecutionContext) extends AsyncBackend[Future] {
  def makeAsync[B](b: => B) = Future {
    blocking {
      b
    }
  }
}
