package org.example
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.scalatest._
class TestBefore_andAfter extends FunSuite with BeforeAndAfter{

  Logger.getLogger("org").setLevel(Level.ERROR)
  var sparkSession : SparkSession  = SparkSession.builder()
    .master("local[*]")
    .appName("Jointable")
    .getOrCreate()
  sparkSession.sparkContext.setLogLevel("ERROR")
  before {
    val lines1 = Count_location.readfile(sparkSession.read.csv("src/main/resources/transactions.csv"),Seq ("transcationid", "productid", "userid", "price", "productdesc"))
    test(" spending done by each user on each product"){
    assert(Count_location.Spending_eachuser(lines1).toString=== lines1.groupBy("userid","productid","productdesc").agg(sum("price")).toString())}
  }
  after {
    sparkSession.stop()
  }
}
