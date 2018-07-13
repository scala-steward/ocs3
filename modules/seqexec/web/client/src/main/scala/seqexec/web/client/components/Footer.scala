// Copyright (c) 2016-2018 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package seqexec.web.client.components

import diode.react.ModelProxy
import diode.react.ReactPot._
import gem.enum.Site
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import japgolly.scalajs.react.component.Scala.Unmounted
import seqexec.web.client.actions.NavigateTo
import seqexec.web.client.circuit.SeqexecCircuit
import seqexec.web.client.model.WebSocketConnection
import seqexec.web.client.model.Pages.Root
import seqexec.web.client.OcsBuildInfo
import seqexec.web.client.semanticui.elements.icon.Icon._
import seqexec.web.client.semanticui.elements.menu.HeaderItem
import web.client.style._

/**
  * Component for the bar at the top of the page
  */
object Footer {
  private val userConnect = SeqexecCircuit.connect(SeqexecCircuit.statusReader)
  private val wsConnect = SeqexecCircuit.connect(_.ws)

  private def goHome(e: ReactEvent): Callback = {
    e.preventDefault
    Callback(SeqexecCircuit.dispatch(NavigateTo(Root)))
  }

  private val component = ScalaComponent.builder[Site]("SeqexecAppBar")
    .stateless
    .render_P(p =>
      <.div(
        ^.cls := "ui footer inverted menu",
        <.a(
          ^.cls := "header item",
          ^.onClick ==> goHome,
          s"Seqexec - ${p.shortName}"
        ),
        HeaderItem(HeaderItem.Props(OcsBuildInfo.version, sub = true)),
        wsConnect(ConnectionState.apply),
        userConnect(ControlMenu.apply)
      )
    )
    .componentDidMount(ctx =>
      Callback {
        // Mount the Semantic component using jQuery
        import org.querki.jquery.$
        import web.client.facades.semanticui.SemanticUIVisibility._

        // Pick the top bar and make it stay visible regardless of scrolling
        ctx.getDOMNode.foreach { dom => $(dom).visibility(JsVisiblityOptions.visibilityType("fixed").offset(0)) }
      }
    )
    .build

  def apply(s: Site): Unmounted[Site, Unit, Unit] = component(s)
}

/**
  * Alert message when the connection disappears
  */
object ConnectionState {

  final case class Props(u: WebSocketConnection)

  def formatTime(delay: Int): String = if (delay < 1000) {
    f"${delay / 1000.0}%.1f"
  } else {
    f"${delay / 1000}%d"
  }

  private val component = ScalaComponent.builder[Props]("ConnectionState")
    .stateless
    .render_P( p =>
      <.div(
        ^.cls := "ui header item sub",
        p.u.ws.renderPending(t =>
          <.div(
            IconAttention.copyIcon(color = Option("red")),
            <.span(
              SeqexecStyles.errorText,
              s"Connection lost, retrying in ${formatTime(p.u.nextAttempt)} [s] ..."
            )
          )
        )
      )
    )
    .build

  def apply(u: ModelProxy[WebSocketConnection]): Unmounted[Props, Unit, Unit] = component(Props(u()))
}