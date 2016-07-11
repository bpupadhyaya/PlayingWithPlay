package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok("Test message")
  }

  def sayMessage(name: String) = Action {
    Ok("Hi " + name)
  }
}

object ApplicationNew extends Controller {
  def sayMessage(name: String) = Action {
    Ok(views.html.sayMessage(name))
  }
}

object ApplicationProductList extends Controller {
  def index = Action {
    implicit request => Redirect(routes.Products.list())
  }
}