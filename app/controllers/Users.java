package controllers;

import models.Poll;
import models.User;
import play.cache.Cache;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.users.*;

import static play.data.Form.form;

public class Users extends Controller {

    @Transactional(readOnly = true)
    public static Result newUser() {
        Form<User> userForm = form(User.class);
        return ok(
                insert.render(userForm)
        );
    }

    @Transactional
    public static Result save() {
        Form<User> userForm = form(User.class).bindFromRequest();

        if(userForm.hasErrors())
            return badRequest(insert.render(userForm));

        User user = userForm.get();

        Poll poll = Application.getPoll();
        poll.user = user;
        poll.save();

        Application.clearCache();

        return redirect(
            routes.Application.ranking(user.email)
        );
    }

}
