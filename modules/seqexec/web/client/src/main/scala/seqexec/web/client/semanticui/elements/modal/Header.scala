// Copyright (c) 2016-2018 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package seqexec.web.client.semanticui.elements.modal

import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.html_<^._

object Header {
  private val component = ScalaComponent.builder[Unit]("Header")
    .stateless
    .renderPC((_, p, c) =>
      <.div(
        ^.cls := "header",
        c
      )
    ).build

  def apply(children: VdomNode*): Unmounted[Unit, Unit, Unit] = component(children: _*)
}
