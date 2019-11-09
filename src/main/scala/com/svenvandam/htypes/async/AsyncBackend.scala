package com.svenvandam.htypes.async

/**
  * Typeclass to wrap asynchronous side-effects
  */
trait AsyncBackend[A[_]] {
  def makeAsync[B](b: => B): A[B]
}
