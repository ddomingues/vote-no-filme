package models;

import org.junit.Test;
import play.db.jpa.JPA;

import static org.fest.assertions.Assertions.assertThat;

public class UserTest extends BaseTest {

    @Test
    public void shouldSaveUser() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            User user = new User("Diego Domingues", "diego.domingues16@gmail.com");
            user.save();
            assertThat(user.id).isNotNull();
            }
        });
    }

    @Test
    public void shouldValidateDuplicateUser() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            User user = new User("Diego Domingues", "diego.domingues16@gmail.com");
            user.save();

            try {
                user = new User("Diego Domingues", "diego.domingues16@gmail.com");
                user.save();
            } catch (Exception e) {
                assertThat(e.getMessage()).containsIgnoringCase("User");
            }

            }
        });
    }

    @Test
    public void shouldValidateRequiredFields() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            try {
                new User().save();
            } catch (Exception e) {
                assertThat(e.getMessage()).containsIgnoringCase("error.required");
            }
            }
        });
    }

    @Test
    public void shouldRetrieveByEmail() {
        JPA.withTransaction(new play.libs.F.Callback0() {
            public void invoke() {
            User user = new User("Diego Domingues", "diego.domingues16@gmail.com");
            user.save();

            User retrievedUser = User.findByEmail(user.email);

            assertThat(retrievedUser).isNotNull();
            assertThat(user).isEqualTo(retrievedUser);
            }
        });
    }


}
