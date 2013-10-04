package models;

import org.junit.Test;
import play.db.jpa.JPA;

import javax.persistence.PersistenceException;

import static org.fest.assertions.Assertions.assertThat;

public class PollTest extends BaseTest {

    @Test
    public void shouldGenerateCombination() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            Poll poll = Poll.newInstance();
            assertThat(poll.votes).isNotNull();
            assertThat(poll.votes).isNotEmpty();
            }
        });
    }

    @Test
    public void shouldSelectOptionValid() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            Poll poll = Poll.newInstance();

            Vote vote = poll.getCurrentVote();

            Movie select = vote.options.iterator().next();

            poll.vote(select);

            assertThat(vote.selected).isNotNull();
            assertThat(vote.selected).isEqualTo(select);
            }
        });
    }

    @Test
    public void shouldValidateOptionInvalid() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            Poll poll = Poll.newInstance();

            Movie newMovie = new Movie("Teste", "Synopsis", 120, "Jennifer Aniston", "Drama");
            newMovie.save();

            try {
                poll.vote(newMovie);
            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).isEqualTo("Option invalid!");
            }
            }
        });
    }

    @Test
    public void shouldValidatePollFinalized() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            Poll poll = Poll.newInstance();

            while (!poll.isFinalized()) {
                Vote vote = poll.getCurrentVote();
                poll.vote(vote.options.iterator().next());
            }

            try {
                poll.vote(null);
            } catch (IllegalArgumentException e) {
                assertThat(e.getMessage()).isEqualTo("Poll finalized!");
            }

            }
        });
    }

    private Poll donePoll(User user) {
        Poll poll = Poll.newInstance();

        while (!poll.isFinalized()) {
            Vote vote = poll.getCurrentVote();
            poll.vote(vote.options.iterator().next());
        }

        poll.user = user;
        poll.save();
        return poll;
    }

    @Test
    public void shouldSavePoll() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            User user = new User("Diego Domingues", "diego.domingues16@gmail.com");
            Poll poll = donePoll(user);

            assertThat(poll.id).isNotNull();
            assertThat(poll.user.id).isNotNull();
            assertThat(poll.user.email).isEqualTo(user.email);
            }
        });
    }

    @Test
    public void shouldNotSavePollForSameUser() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            User user = new User("Diego Domingues", "diego.domingues16@gmail.com");

            donePoll(user);

            try {
                donePoll(user);
            } catch (PersistenceException e) {
                assertThat(e.getMessage()).containsIgnoringCase("ConstraintViolationException");
            }

            }
        });
    }


}
