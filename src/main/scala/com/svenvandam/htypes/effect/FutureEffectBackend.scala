package com.svenvandam.htypes.effect

import scala.concurrent.{blocking, ExecutionContext, Future}

/**
  * Future based instance of [[EffectBackend]].
  * Executes the effect in a thread provided by an [[ExecutionContext]].
  */
case class FutureEffectBackend()(implicit ec: ExecutionContext) extends EffectBackend[Future] {
  def wrap[A](a: => A) = Future {
    blocking {
      a
    }
  }
}
