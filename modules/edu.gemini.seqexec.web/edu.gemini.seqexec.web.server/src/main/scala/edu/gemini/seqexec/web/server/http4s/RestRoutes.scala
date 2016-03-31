package edu.gemini.seqexec.web.server.http4s

import org.http4s._
import org.http4s.dsl._
import upickle.default._
import edu.gemini.seqexec.web.common._
import edu.gemini.seqexec.web.server.model.CannedModel

object RestRoutes {
  val service = HttpService {
    case req @ GET -> Root / "api" / "seqexec" / "current" / "queue" =>
      Ok(write(CannedModel.currentQueue))
  }
}
