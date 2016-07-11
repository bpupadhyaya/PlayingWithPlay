
package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {

    public static Result index() {
        return ok("Test message");
    }

    public static Result sayMessage(String name) {
        return ok("Hi " + name);
    }

}