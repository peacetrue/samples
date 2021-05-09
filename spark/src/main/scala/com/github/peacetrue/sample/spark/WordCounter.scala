package com.github.peacetrue.sample.spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.slf4j.LoggerFactory

/**
 *
 * ${cursor}
 *
 * @author : xiayx
 * @since : 2021-05-09 07:53
 * */
object WordCounter {

  def log = LoggerFactory.getLogger(WordCounter.getClass)

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sparkContext = new SparkContext(sparkConf)

    val path = "/Users/xiayx/Documents/Projects/samples/spark/src/main/resources"
    val lines: RDD[String] = sparkContext.textFile(s"${path}/input")
    log.debug("lines: {}", lines.collect())
    //[Hello Scala, Hello Spark, Hello Scala, Hello Spark]

    val words: RDD[String] = lines.flatMap(_.split(" +"))
    log.debug("words: {}", words.collect())
    //[Hello, Scala, Hello, Spark, Hello, Scala, Hello, Spark]

    val wordOneTuples = words.map(item => (item, 1))
    log.debug("wordOneTuples: {}", wordOneTuples.collect())
    //[(Hello,1), (Scala,1), (Hello,1), (Spark,1), (Hello,1), (Scala,1), (Hello,1), (Spark,1)]

    val wordTupleLists = wordOneTuples.groupBy(item => item._1)
    log.debug("wordTupleLists: {}", wordTupleLists.collect())
    //[(Hello,CompactBuffer((Hello,1), (Hello,1), (Hello,1), (Hello,1))), (Scala,CompactBuffer((Scala,1), (Scala,1))), (Spark,CompactBuffer((Spark,1), (Spark,1)))]

    val wordCountTuples = wordTupleLists.map(item => (item._1, item._2.map(item => item._2).sum))
    log.debug("wordCountTuples: {}", wordCountTuples.collect())

    sparkContext.stop()
  }

}
