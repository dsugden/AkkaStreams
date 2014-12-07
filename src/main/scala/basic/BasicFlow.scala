package basic

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl._
import akka.stream.scaladsl.FlowGraphImplicits


import scala.concurrent.forkjoin.ThreadLocalRandom

object BasicFlow {

  def main(args:Array[String]):Unit = {


    implicit val system = ActorSystem("Sys")
    import system.dispatcher

    implicit val materializer = FlowMaterializer()

    val text1 =
      """|Lorem Ipsum is simply dummy text of the printing and typesetting industry.
        |Lorem Ipsum has been the industry's standard dummy text ever since the 1500s,
        |when an unknown printer took a galley of type and scrambled it to make a type
        |specimen book.""".stripMargin


    val text2 =
      """1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,g,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1""".stripMargin


    val text1Source: Source[String] = Source(() => text1.split("\\s").iterator)

    val text2Source: Source[String] = Source(() => text2.split(",").iterator)


    val f1 = Flow[String].map(x => x.toUpperCase)

    val f2 = Flow[String].map(e => (e.toInt + 1).toString)

    val consoleSink = ForeachSink[String](println)

    FlowGraph {  implicit builder =>
      import FlowGraphImplicits._
      val merge = Merge[String]
      text1Source ~> f1  ~> merge ~> consoleSink
      text2Source ~> merge
    }.run()
  }


}
