package com.svenvandam.htypes

import java.util.UUID
import com.svenvandam.htypes.converters.ScalaConverter
import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.hbase.client.Table
import org.apache.hadoop.hbase.{HBaseTestingUtility, TableName}
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

trait BaseHbaseTest extends FunSuiteLike with BeforeAndAfterAll with LazyLogging with ScalaConverter {

  val hBaseUtility = new HBaseTestingUtility()

  override def beforeAll {
    super.beforeAll()
    hBaseUtility.startMiniCluster()
    logger.info("HBase test cluster started.")
  }

  override def afterAll() {
    super.afterAll()
    hBaseUtility.shutdownMiniCluster()
    logger.info("HBase test cluster stopped.")
  }

  def getTable(families: Array[String] = Array("cf1"), maxValues: Int = 10): Table = {
    val name = UUID.randomUUID().toString
    hBaseUtility.createTable(TableName.valueOf(name), families.map(_.getBytes), maxValues)
  }
}
