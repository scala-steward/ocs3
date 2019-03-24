// Copyright (c) 2016-2019 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package seqexec.web.client.services

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import cats.Show
import cats.implicits._

object WebpackResources {

  // marker trait
  trait WebpackResource extends js.Object

  object WebpackResource {
    implicit val show: Show[WebpackResource] = Show.fromToString
  }

  implicit class WebpackResourceOps(val r: WebpackResource) extends AnyVal {
    def resource: String = r.show
  }

  @JSImport("sounds/sequencepaused.mp3", JSImport.Default)
  @js.native
  object SequencePausedResourceMP3 extends WebpackResource

  @JSImport("sounds/sequencepaused.webm", JSImport.Default)
  @js.native
  object SequencePausedResourceWebM extends WebpackResource

  @JSImport("sounds/exposurepaused.mp3", JSImport.Default)
  @js.native
  object ExposurePausedResourceMP3 extends WebpackResource

  @JSImport("sounds/exposurepaused.webm", JSImport.Default)
  @js.native
  object ExposurePausedResourceWebM extends WebpackResource

  @JSImport("sounds/sequenceerror.mp3", JSImport.Default)
  @js.native
  object SequenceErrorResourceMP3 extends WebpackResource

  @JSImport("sounds/sequenceerror.webm", JSImport.Default)
  @js.native
  object SequenceErrorResourceWebM extends WebpackResource

  @JSImport("sounds/sequencecomplete.mp3", JSImport.Default)
  @js.native
  object SequenceCompleteResourceMP3 extends WebpackResource

  @JSImport("sounds/sequencecomplete.webm", JSImport.Default)
  @js.native
  object SequenceCompleteResourceWebM extends WebpackResource

  @JSImport("sounds/beep-22.mp3", JSImport.Default)
  @js.native
  object BeepResourceMP3 extends WebpackResource

  @JSImport("sounds/beep-22.webm", JSImport.Default)
  @js.native
  object BeepResourceWebM extends WebpackResource

}
