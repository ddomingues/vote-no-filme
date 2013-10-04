package models;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;

@Entity
public class Vote extends BaseEntity {

    private static final long serialVersionUID = -7581038385020788437L;

    @ManyToOne(optional = false)
    public Poll poll;

    @ManyToMany
    public List<Movie> options;

    @ManyToOne(optional = false)
    public Movie selected;

    public Vote() {
        super();
    }

    public Vote(Movie ... movies) {
        options = Arrays.asList(movies);
    }

    public void selected(Movie movie) {
        if (!options.contains(movie))
             throw new IllegalArgumentException("Option invalid!");

        this.selected = movie;
    }
}
