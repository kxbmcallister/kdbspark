package com.kx.spark

import com.kx.spark.KdbDataSource
import com.kx.spark.ReadTask
import org.apache.spark.sql.sources.v2._
import java.util.HashMap
import scala.collection.JavaConversions._
import org.apache.spark.sql.types._

object Main extends App {
  @transient lazy val log = org.apache.log4j.Logger.getLogger("Main")

  log.info("Running!")

  val kdbSchema = StructType(List(
	  StructField("jcolumn", LongType, false),
	  StructField("pcolumn", TimestampType, false),
	  StructField("clcolumn", StringType, false)
  ))

  val reader = new KdbDataSource()
  val myMap = Map("function"->"exampleSimple", 
        "host" -> "localhost", 
        "port" -> "3024", 
        "pushFilters" -> "false")
        
  val jmap = new java.util.HashMap[String,String](myMap)

  val obj = reader.createReader(kdbSchema, new DataSourceOptions(jmap))
  val kdbTask = obj.createBatchDataReaderFactories()
  log.info("NEXT:" + kdbTask.get(0).asInstanceOf[ReadTask].next)
  log.info("RESULT:" + kdbTask.get(0).asInstanceOf[ReadTask].get)
  log.info("Done")

}