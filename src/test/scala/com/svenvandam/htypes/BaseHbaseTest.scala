package com.svenvandam.htypes

import java.util.UUID
import com.typesafe.scalalogging.LazyLogging
import org.apache.hadoop.hbase.client.Table
import org.apache.hadoop.hbase.{TableName, HBaseTestingUtility, HBaseCommonTestingUtility}
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}

trait BaseHbaseTest extends FunSuiteLike with BeforeAndAfterAll with LazyLogging {

  val hBaseUtility = new HBaseTestingUtility()

  override def beforeAll {
    super.beforeAll()
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog")
    val dir = sys.env.getOrElse("HBASE_TMP_DIR", "/tmp")
    val testDataDir = s"$dir/embedded-hbase-test-data-${UUID.randomUUID().toString}"
    logger.info(s"TestDir for hbase test: $testDataDir")
    System.setProperty(HBaseCommonTestingUtility.BASE_TEST_DIRECTORY_KEY, testDataDir)
    hBaseUtility.getConfiguration.set("hbase.master.hostname", "localhost")
    hBaseUtility.getConfiguration.set("hbase.regionserver.hostname", "localhost")
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

  implicit def strToBytes(str: String): Array[Byte] = str.getBytes
}
