package controllers

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import controllers.request.SignInRequest
import play.api.mvc.Cookie.SameSite

import javax.inject.{Inject, Singleton}
import play.api.mvc._
import play.filters.csrf.CSRF.Token
import play.filters.csrf.{CSRF, CSRFAddToken}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SignInController @Inject()(scc: DefaultSilhouetteControllerComponents, addToken: CSRFAddToken)(implicit ex: ExecutionContext) extends AbstractAuthController(scc) {

  def signIn: Action[AnyContent] = addToken(unsecuredAction.async { implicit request: Request[AnyContent] =>
    val json = request.body.asJson.get
    val Token(name, value) = CSRF.getToken.get
    val signInRequest = json.as[SignInRequest]
    val credentials = Credentials(signInRequest.email, signInRequest.password)

    credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
      userRepository.retrieve(loginInfo).flatMap {
        case Some(user) =>
          authenticateUser(user)
            .map(_.withCookies(Cookie(name, value, httpOnly = false, secure = true, sameSite = Option.apply(SameSite.None)), Cookie("user", user.id.toString, httpOnly = false, secure = true, sameSite = Option.apply(SameSite.None)))
              .withHeaders(("Access-Control-Allow-Credentials", "true"),
              ("Access-Control-Allow-Origin", "https://playshop-frontend.azurewebsites.net"),
              ("Access-Control-Expose-Headers", "csrftoken"), ("csrftoken", value)))
        case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
      }
    }.recover {
      case _: ProviderException =>
        Forbidden("Wrong credentials")
          .discardingCookies(DiscardingCookie(name = "PLAY_SESSION"))
    }
  })

  def signOut: Action[AnyContent] = securedAction.async { implicit request: SecuredRequest[EnvType, AnyContent] =>
    authenticatorService.discard(request.authenticator, Ok("Logged out"))
      .map(_.discardingCookies(
        DiscardingCookie(name = "csrfToken"),
        DiscardingCookie(name = "PLAY_SESSION"),
        DiscardingCookie(name = "OAuth2State"),
        DiscardingCookie(name = "user")
      ))
  }
}