package microservice

import akka.actor.ActorSystem
import akka.http.Http
import akka.http.model._
import akka.stream.FlowMaterializer
import akka.http.model.HttpMethods._
import akka.stream.scaladsl.Flow
import akka.http.Http


object AkkaStreamService {
  implicit val system = ActorSystem()
  implicit val materializer = FlowMaterializer()


  def main(args: Array[String]):Unit={


    val requestHandler: HttpRequest => HttpResponse = {
      case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
        HttpResponse(
          entity = HttpEntity(MediaTypes.`application/json`,
            """{ "id":1 }"""))

      case _: HttpRequest                                => HttpResponse(404, entity = "Unknown resource!")
    }

    val serverBinding = Http(system).bind(interface = "localhost", port = 8080).connections foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)

      connection handleWith { Flow[HttpRequest] map requestHandler }
    }
  }


}
