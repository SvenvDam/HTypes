package com.svenvandam.htypes.async

import java.util.concurrent.CompletableFuture
import org.scalatest.FunSuite
import org.scalatest.Matchers._
import com.svenvandam.htypes.Implicits._
import com.svenvandam.htypes.TestIO

class CompletableFutureSyntaxTest extends FunSuite {
  test("it should transform a CompletableFuture before its inner computations starts") {
    var x = 0

    val action = new Runnable {
      override def run(): Unit = x = 1
    }

    def completableFuture(i: Int): CompletableFuture[Void] =
      CompletableFuture.runAsync(action)

    val io = completableFuture(1).convertEffect[TestIO]

    x shouldBe 0

    io.run()

    x shouldBe 1
  }

}
