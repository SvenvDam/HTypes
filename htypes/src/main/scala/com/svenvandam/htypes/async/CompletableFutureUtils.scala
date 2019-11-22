package com.svenvandam.htypes.async

import com.svenvandam.htypes.effect.EffectBackend
import java.util.concurrent.CompletableFuture

object CompletableFutureUtils {

  /**
    * Adapter method for [[CompletableFuture]] to an effect wrapper `E`.
    * The HBase Client API's [[org.apache.hadoop.hbase.client.AsyncTable]] returns [[CompletableFuture]]s.
    *
    * The [[CompletableFuture]] is passed by-name and will not be triggered directly.
    * This allows lazy effect wrappers to delay the query.
    *
    * Blocks on the [[CompletableFuture]] inside the new wrapper.
    * Exceptions might be thrown here and should be handled!
    */
  def interceptCompletableFuture[A, B[_]](f: => CompletableFuture[A])(implicit backend: EffectBackend[B]): B[A] =
    backend.wrap(f.get())
}
