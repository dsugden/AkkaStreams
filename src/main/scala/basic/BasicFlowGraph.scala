package basic

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import akka.stream.scaladsl._
import akka.stream.scaladsl.FlowGraphImplicits
import scala.util.{Failure, Success, Try}


/**
 * This FlowGraph has 2 Sources and 1 Sink
 */

object BasicFlowGraph {

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


    val f1 = Flow[String].map(x => Success(x.toUpperCase))

    val f2 = Flow[String].map(e => Try{(e.toInt + 1).toString})

    val consoleSink = ForeachSink[Try[String]]{
      case Success(v) => println(v)
      case Failure(t) => println("!!!!")
    }

    FlowGraph {  implicit builder =>
      import FlowGraphImplicits._
      val merge = Merge[Try[String]](OperationAttributes.inputBuffer(1,8))
      text1Source ~> f1  ~> merge ~> consoleSink
      text2Source ~> f2 ~> merge
    }.run()
  }


}
