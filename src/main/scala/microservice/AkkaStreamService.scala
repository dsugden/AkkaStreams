package microservice

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model._
import akka.io.IO
import akka.stream.FlowMaterializer
import akka.http.model.HttpMethods._
import akka.stream.scaladsl._
import akka.http.Http
import rapture._
import core._, json._
import jsonBackends.jawn._
import modes.returnTry
import scala.concurrent.Future
import scala.util.{Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.server.Directives._
import akka.http.server.Route._
import akka.http.server.Route

object AkkaStreamService {
  implicit val system = ActorSystem()
  implicit val materializer = FlowMaterializer()


  def main(args: Array[String]):Unit={


    /** Dummy model */
    sealed trait Model
    case class User(id:Int,name:String) extends Model
    case class Car(name:String) extends Model
    case class UsersCar(name:String,carName:String)
    val userJson  = Json(User(1,"martin"))
    val carJson  = Json(Car("myata"))

    sealed trait Command
    case class GetUser(id:Int) extends Command
    case class GetCar(id:Int) extends Command
    case object Noop extends Command


    val flow:Flow[HttpRequest, HttpResponse] = Flow[HttpRequest].map(r =>HttpResponse(
      entity = HttpEntity(MediaTypes.`application/json`,"S" )))


//    val combineUserAndCarFlow:Flow[] =



    val requestHandler: HttpRequest => Future[HttpResponse] = {

      case HttpRequest(GET, Uri.Path("/"), _, _, _) => {

        Source(() => List(1).iterator ).map(x =>
          User(1,"martin")).map(user =>
          (user,Car("myata"))).map{ case(fu,fc) =>
          UsersCar(fu.name,fc.name)
        }.map(  cu => HttpResponse(
          entity = HttpEntity(MediaTypes.`application/json`,
            Json(cu).toString()))).fold( HttpResponse(500, entity = "Error!")  )((b,a) => a)
      }

      case _: HttpRequest => Future{HttpResponse(404, entity = "Unknown resource!")}
    }

//    val serverBinding = Http(system).bind(interface = "localhost", port = 8080).connections foreach { connection =>
//      println("Accepted new connection from " + connection.remoteAddress)
////      connection handleWithAsyncHandler  {requestHandler }
//      connection handleWith  {flow }
//    }
//
    val binding = Http().bind("localhost", 8080)

    binding startHandlingWith {
      path("user" / LongNumber) { id =>
        get {
          val res = complete( Source(() => List(id).iterator ).map(x =>
            User(1,"martin")).map(user =>
            (user,Car("myata"))).map{ case(fu,fc) =>
            UsersCar(fu.name,fc.name)
          }.map(  cu => HttpResponse(
            entity = HttpEntity(MediaTypes.`application/json`,
              Json(cu).toString()))).fold( HttpResponse(500, entity = "Error!")  )((b,a) => a))
//          complete(s"Received GET for order $id")
          res
        } ~
          put {
            complete(s"Received PUT for order $id")
          }
      }
    }


  }


}
