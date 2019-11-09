package com.svenvandam.htypes.async

/**
  * Type class to wrap asynchronous side-effects.
  */
trait AsyncBackend[A[_]] {
  def makeAsync[B](b: => B): A[B]
}
