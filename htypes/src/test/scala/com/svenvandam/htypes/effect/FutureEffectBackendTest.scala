package com.svenvandam.htypes.effect

import org.scalatest.FunSuite
import org.scalatest.Matchers._
import scala.concurrent.Await
import scala.concurrent.duration._

class FutureEffectBackendTest extends FunSuite {
  test("it should wrap a side-effect in a Future") {
    import scala.concurrent.ExecutionContext.Implicits.global

    val backend = FutureEffectBackend()

    var x = 0

    val f = backend.lift {
      Thread.sleep(1000)
      x = 1
    }

    x shouldBe 0

    Await.ready(f, 2 seconds)

    x shouldBe 1
  }
}
