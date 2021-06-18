package controllers

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.impl.providers._
import play.api.mvc.Cookie.SameSite

import javax.inject.Inject
import play.api.mvc.{Action, AnyContent, Cookie, Request}
import play.filters.csrf.CSRF.Token
import play.filters.csrf.{CSRF, CSRFAddToken}

import scala.concurrent.{ExecutionContext, Future}

class SocialAuthController @Inject()(scc: DefaultSilhouetteControllerComponents, addToken: CSRFAddToken)(implicit ex: ExecutionContext) extends SilhouetteController(scc) {

  def authenticate(provider: String): Action[AnyContent] = addToken(Action.async { implicit request: Request[AnyContent] =>
    (socialProviderRegistry.get[SocialProvider](provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) => for {
            profile <- p.retrieveProfile(authInfo)
            user <- userRepository.save(profile.loginInfo.providerID, profile.loginInfo.providerKey, profile.email.getOrElse(""))
            _ <- authInfoRepository.save(profile.loginInfo, authInfo)
            authenticator <- authenticatorService.create(profile.loginInfo)
            value <- authenticatorService.init(authenticator)
            result <- authenticatorService.embed(value, Redirect(scala.util.Properties.envOrElse(s"REACT_APP_URL?csrfToken=${CSRF.getToken.get.value}&userId=${user.id}", s"http://localhost:3000?csrfToken=${CSRF.getToken.get.value}&userId=${user.id}")))
          } yield {
            val Token(name, value) = CSRF.getToken.get
            result.withCookies(Cookie(name, value, httpOnly = false, secure = true, sameSite = Option.apply(SameSite.None)), Cookie("user", user.id.toString, httpOnly = false, secure = true, sameSite = Option.apply(SameSite.None)))
          }
        }
      case _ => Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    }).recover {
      case e: ProviderException =>
        Forbidden(e.getMessage)
    }
  })
}