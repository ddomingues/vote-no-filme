package controllers;

import models.Movie;
import models.Poll;
import models.User;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.polls.*;
import views.html.users.*;

import static controllers.Application.*;
import static play.data.Form.form;

public class Polls extends Controller {

    @Transactional(readOnly = true)
    public static Result vote(Long idMovie) {

        Poll poll = getPoll();
        poll.vote(new Movie(idMovie));

        if (poll.isFinalized()) {

            return redirect(
                routes.Users.newUser()
            );

        } else {

            return HOME;
        }

    }

    @Transactional
    public static Result create() {
        Form<User> userForm = form(User.class).bindFromRequest();

        if(userForm.hasErrors())
            return badRequest(insert.render(userForm));

        User user = userForm.get();

        Poll poll = getPoll();
        poll.user = user;
        poll.save();

        Application.clearCache();

        return redirect(
                routes.Polls.ranking(user.email)
        );
    }

    @Transactional(readOnly = true)
    public static Result ranking(String email) {
        User user = null;
        Movie.Ranking userRanking = null;
        Movie.Ranking generalRanking = Movie.getRanking();

        if (email != null) {
            user = User.findByEmail(email);

            if (user != null)
                userRanking = Movie.getRankingByUser(user);
        }

        return ok(
                ranking.render(
                        user,
                        userRanking,
                        generalRanking
                )
        );
    }
}
