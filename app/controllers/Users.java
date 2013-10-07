package controllers;

import models.Poll;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.users.*;

import static play.data.Form.form;
import static controllers.Application.*;

public class Users extends Controller {

    @Transactional(readOnly = true)
    public static Result newUser() {
        Form<User> userForm = form(User.class);
        return ok(
                insert.render(userForm)
        );
    }

}
