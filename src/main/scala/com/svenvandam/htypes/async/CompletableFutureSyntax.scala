package com.svenvandam.htypes.async

import java.util.concurrent.CompletableFuture
import com.svenvandam.htypes.effect.EffectBackend

trait CompletableFutureSyntax {
  implicit class CompletableSyntaxOps[A](f: => CompletableFuture[A]) {
    def as[B[_]](implicit backend: EffectBackend[B]): B[A] = CompletableFutureUtils.interceptCompletableFuture(f)
  }
}

object CompletableFutureSyntax extends CompletableFutureSyntax
