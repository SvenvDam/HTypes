package com.svenvandam.htypes

import com.svenvandam.htypes.effect.EffectBackend

case class TestIO[A](action: () => A) {
  def run() = action()
}

object TestIO {
  implicit val ioBackend = new EffectBackend[TestIO] {
    def wrap[A](a: => A): TestIO[A] =
      TestIO(() => a)
  }
}
