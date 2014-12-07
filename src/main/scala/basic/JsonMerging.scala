package basic

import akka.actor.ActorSystem
import akka.stream.FlowMaterializer
import rapture._
import core._, json._
import jsonBackends.jawn._
import modes.returnTry


object JsonMerging {

  def main(args:Array[String]):Unit = {


    implicit val system = ActorSystem("Sys")
    import system.dispatcher

    implicit val materializer = FlowMaterializer()

    case class User(id:Int,name:String)
    case class Car(name:String)
    case class UsersCar(userId:Int,carName:String)
    val userJson  = Json(User(1,"martin"))
    val carJson  = Json(Car("myata"))





  }

}
