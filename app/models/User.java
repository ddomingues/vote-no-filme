package models;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.db.jpa.JPA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User_")
public class User extends BaseEntity {

    private static final long serialVersionUID = -7581038385020728497L;

    @Constraints.Required
    @Column(nullable = false)
    public String name;

    @Constraints.Required
    @Constraints.Email
    @Column(nullable = false, unique = true)
    public String email;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();

        if (this.email != null && User.getByEmail(this.email) != null)
            errors.add(new ValidationError("email", "error.user.registred"));

        return errors.isEmpty()? null: errors;
    }

    public User() {
        super();
    }

    public User(String name, String email) {
        this();
        this.name = name;
        this.email = email;
    }

    public static User getByEmail(String email) {
        List<User> users = JPA.em().createQuery("from User where email = :email")
                .setParameter("email", email)
                .getResultList();

        return users.isEmpty()? null: users.iterator().next();
    }
}
