package com.svenvandam.htypes.async

object TableUtils {

  /**
    * Execute a query and wrap it in an effect wrapper `E` using [[AsyncBackend]].
    *
    * @tparam Q query type
    * @tparam R result type
    * @tparam E effect wrapper type
    */
  def asyncQuery[Q, R, E[_]](query: Q, run: Q => R)(implicit backend: AsyncBackend[E]): E[R] =
    backend.makeAsync(run(query))

}
