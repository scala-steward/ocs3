// Copyright (c) 2016-2018 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package seqexec.web.client.components

import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import seqexec.model.UserDetails
import seqexec.web.client.semanticui.elements.icon.Icon._
import seqexec.web.client.model._
import seqexec.web.client.actions.{CloseLoginBox, LoggedIn}
import seqexec.web.client.semanticui.elements.button.Button
import seqexec.web.client.semanticui.elements.modal.{Content, Header}
import seqexec.web.client.semanticui.elements.label.FormLabel
import seqexec.web.client.services.SeqexecWebClient
import japgolly.scalajs.react.component.Scala.Unmounted
import japgolly.scalajs.react.vdom.TagOf
import org.scalajs.dom.html.Div
import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * UI for the login box
  */
object LoginBox {

  final case class Props(visible: ModelProxy[SectionVisibilityState])

  final case class State(username: String, password: String, progressMsg: Option[String], errorMsg: Option[String])

  private val empty = State("", "", None, None)

  private val formId = "login"

  class Backend($: BackendScope[Props, State]) {
    def pwdMod(e: ReactEventFromInput): CallbackTo[Unit] = {
      // Capture the value outside setState, react reuses the events
      val v = e.target.value
      $.modState(_.copy(password = v))
    }

    def userMod(e: ReactEventFromInput): CallbackTo[Unit] = {
      val v = e.target.value
      $.modState(_.copy(username = v))
    }

    def loggedInEvent(u: UserDetails): Callback = updateProgressMsg("") >> $.props >>= {_.visible.dispatchCB(LoggedIn(u))}
    def updateProgressMsg(m: String): Callback = $.modState(_.copy(progressMsg = Some(m), errorMsg = None))
    def updateErrorMsg(m: String): Callback = $.modState(_.copy(errorMsg = Some(m), progressMsg = None))
    def closeBox: Callback = $.modState(_ => empty) >> $.props >>= {_.visible.dispatchCB(CloseLoginBox)}

    def attemptLogin: Callback = $.state >>= { s =>
      // Change the UI and call login on the remote backend
      updateProgressMsg("Authenticating...") >>
      Callback.future(
        SeqexecWebClient.login(s.username, s.password)
          .map(loggedInEvent)
          .recover {
            case _: Exception => updateErrorMsg("Login failed, check username/password")
          }
      )
    }

    private def toolbar(s: State) =
      <.div(
        ^.cls := "ui actions",
        <.div(
          ^.cls := "ui grid",
          <.div(
            ^.cls := "middle aligned row",
            s.progressMsg.map( m =>
              <.div(
                ^.cls := "left floated left aligned six wide column",
                IconCircleNotched.copyIcon(loading = true),
                m
              )
            ).whenDefined,
            s.errorMsg.map( m =>
              <.div(
                ^.cls := "left floated left aligned six wide column red",
                IconAttention,
                m
              )
            ).whenDefined,
            <.div(
              ^.cls := "right floated right aligned ten wide column",
              Button(Button.Props(onClick = closeBox), "Cancel"),
              Button(Button.Props(onClick = attemptLogin, buttonType = Button.SubmitType, form = Some(formId)), "Login")
            )
          )
        )
      )

    def render(s: State): TagOf[Div] =
      <.div(
        ^.cls := "ui modal",
        Header("Login"),
        Content(
          <.form(
            ^.cls :="ui form",
            ^.id := formId,
            ^.method := "post",
            ^.action := "#",
            <.div(
              ^.cls :="required field",
              FormLabel(FormLabel.Props("Username", Some("username"))),
              <.div(
                ^.cls :="ui icon input",
                <.input(
                  ^.`type` :="text",
                  ^.placeholder := "Username",
                  ^.name := "username",
                  ^.id := "username",
                  ^.value := s.username,
                  ^.onChange ==> userMod
                ),
                IconUser
              )
            ),
            <.div(
              ^.cls :="required field",
              FormLabel(FormLabel.Props("Password", Some("password"))),
              <.div(
                ^.cls := "ui icon input",
                <.input(
                  ^.`type` :="password",
                  ^.placeholder := "Password",
                  ^.name := "password",
                  ^.id := "password",
                  ^.value := s.password,
                  ^.onChange ==> pwdMod
                ),
                IconLock
              )
            )
          )
        ),
        toolbar(s)
      )
  }

  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  private val component = ScalaComponent.builder[Props]("Login")
    .initialState(State("", "", None, None))
    .renderBackend[Backend]
    .componentDidUpdate(ctx =>
      Callback {
        // To properly handle the model we need to do updates with jQuery and
        // the Semantic UI javascript library
        // The calls below use a custom scala.js facade for SemanticUI
        import org.querki.jquery.$
        import web.client.facades.semanticui.SemanticUIModal._

        // Close the modal box if the model changes
        ctx.getDOMNode.toElement.foreach { dom =>
          if (ctx.currentProps.visible() === SectionClosed) {
            $(dom).modal("hide")
          }
          if (ctx.currentProps.visible() === SectionOpen) {
            // Configure the modal to autofocus and to act properly on closing
            $(dom).modal(
              JsModalOptions
                .autofocus(true)
                .onHidden { () =>
                  // Need to call direct access as this is outside the event loop
                  ctx.setState(empty)
                  ctx.currentProps.visible.dispatchCB(CloseLoginBox)
                }
            )
            // Show the modal box
            $(dom).modal("show")
          }
        }
      }
    ).build

  def apply(v: ModelProxy[SectionVisibilityState]): Unmounted[Props, State, Backend] = component(Props(v))
}
