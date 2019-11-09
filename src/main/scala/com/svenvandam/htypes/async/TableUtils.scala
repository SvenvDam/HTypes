package com.svenvandam.htypes.async

object TableUtils {

  /**
    * Execute a query asynchronously.
    *
    * @param query the query to execute
    * @param run function which performs the query
    * @param backend instance of AsyncBackend to wrap effect
    * @tparam Q query type
    * @tparam R result type
    * @tparam E effect wrapper type
    */
  def asyncQuery[Q, R, E[_]](query: Q, run: Q => R)(implicit backend: AsyncBackend[E]): E[R] =
    backend.makeAsync(run(query))

}
