package models;

import org.junit.Test;
import play.db.jpa.JPA;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class MovieTest extends BaseTest {

    @Test
    public void shouldRetrieve5Movies() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            List<Movie> movies = Movie.findAll();
            assertThat(movies.size()).isEqualTo(5);
            }
        });
    }

    private Poll donePoll() {
        Poll poll = Poll.newInstance();

        while (!poll.isFinalized()) {
            Vote vote = poll.getCurrentVote();
            poll.vote(vote.options.iterator().next());
        }

        poll.user = new User("Diego Domingues", "diego.domingues16@gmail.com");
        poll.save();
        return poll;
    }

    @Test
    public void shouldRetrieveRanking() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {

            donePoll();

            Movie.Ranking ranking = Movie.getRanking();

            assertThat(ranking).isNotNull();
            assertThat(ranking.movies).isNotNull();
            assertThat(ranking.total).isGreaterThan(0);
            }
        });
    }

    @Test
    public void shouldRetrieveRankingOfUser() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {

            Poll poll = donePoll();

            Movie.Ranking ranking = Movie.getRankingByUser(poll.user);

            assertThat(ranking).isNotNull();
            assertThat(ranking.movies).isNotNull();
            assertThat(ranking.total).isGreaterThan(0);
            }
        });
    }
}
