package controllers;

import models.Movie;
import models.Poll;
import models.User;
import play.cache.Cache;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

public class Application extends Controller {

    public static final String POLL = "poll";

    public static Result HOME = redirect(
        routes.Application.index()
    );

    public static String getSessionId() {
        if (!session().containsKey("id"))
            session().put("id", Double.toString(Math.random()));

        return session().get("id");
    }

    public static Poll getPoll() {
        Poll poll = (Poll) Cache.get(getKeyPoll());

        if (poll == null) {
            poll = Poll.newInstance();
            Cache.set(getKeyPoll(), poll, 60*30);
        }

        return poll;
    }

    public static void clearCache() {
        Cache.remove(getKeyPoll());
    }

    public static String getKeyPoll() {
        return POLL+getSessionId();
    }

    @Transactional(readOnly=true)
    public static Result index() {

        Poll poll = getPoll();

        if (poll.isFinalized())
            return redirect(
                routes.Users.newUser()
            );
        else
            return ok(
                index.render(
                    poll.getCurrentVote(),
                    poll.getCurrentStage(),
                    poll.getTotalStages()
                )
            );
    }
}
