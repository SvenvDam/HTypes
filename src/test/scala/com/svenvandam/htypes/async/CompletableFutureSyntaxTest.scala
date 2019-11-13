package com.svenvandam.htypes.async

import java.util.concurrent.CompletableFuture
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.TestIO

class CompletableFutureSyntaxTest extends FunSuite {
  test("it should transform a CompletableFuture before its inner computations starts") {
    var x = 0

    def completableFuture(i: Int): CompletableFuture[Void] =
      CompletableFuture.runAsync(() => x = i)

    val f = completableFuture(1).as[TestIO]

    x shouldBe 0

    f.run()

    x shouldBe 1
  }

}
