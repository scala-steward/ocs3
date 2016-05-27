package edu.gemini.seqexec.web.client.components

import diode.react.ModelProxy
import edu.gemini.seqexec.model.UserDetails
import edu.gemini.seqexec.web.client.model.{OpenLoginBox, SeqexecCircuit}
import edu.gemini.seqexec.web.client.semanticui.SemanticUI._
import edu.gemini.seqexec.web.client.semanticui.elements.button.Button
import edu.gemini.seqexec.web.client.semanticui.elements.divider.Divider
import edu.gemini.seqexec.web.client.semanticui.elements.icon.Icon.IconDropdown
import edu.gemini.seqexec.web.client.semanticui.elements.menu.Item
import japgolly.scalajs.react.{Callback, ReactComponentB, ReactDOM, ReactElement}
import japgolly.scalajs.react.vdom.prefix_<^._

object LoggedInMenu {
  case class Props(u: UserDetails)

  val component = ReactComponentB[Props]("SeqexecLoggedInMenu")
    .stateless
    .render_P(p =>
      <.div(
        p.u.displayName,
        IconDropdown,
        <.div(
          ^.cls := "menu",
          Item("Logout"),
          Divider(),
          Item("Settings")
        )
      )
    ).componentDidMount(s =>
      Callback {
        // Enable menu on Semantic UI
        import org.querki.jquery.$

        $(ReactDOM.findDOMNode(s)).dropdown()
      }
    ).build

  def apply(u: UserDetails) = component(Props(u))
}

/**
  * Menu at the top bar
  */
object TopMenu {

  case class Props(u: Option[UserDetails])

  def openLogin = Callback.log("Open") >> Callback {SeqexecCircuit.dispatch(OpenLoginBox)}

  val loginButton = Button(Button.Props(emphasis = Button.Secondary, onClick = openLogin), "Login")

  val component = ReactComponentB[Props]("SeqexecTopMenu")
    .stateless
    .render_P( p =>
      <.a(
        ^.href :="#",
        ^.cls := "ui right floated dropdown item",
        p.u.fold(loginButton: ReactElement)(u => LoggedInMenu(u))
      )
    )
    .build

  def apply(u: ModelProxy[Option[UserDetails]]) = component(Props(u()))
}
