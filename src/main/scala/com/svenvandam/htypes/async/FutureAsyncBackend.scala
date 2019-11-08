package com.svenvandam.htypes.async

import scala.concurrent.{blocking, ExecutionContext, Future}

case class FutureAsyncBackend()(implicit ec: ExecutionContext) extends AsyncBackend[Future] {
  def makeAsync[B](b: => B) = Future {
    blocking {
      b
    }
  }
}
