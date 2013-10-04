package models;

import play.db.jpa.JPA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
public class Movie extends BaseEntity {

    public static class Ranking implements Serializable {

        private static final long serialVersionUID = -7581038385020783497L;

        public Long total = 0l;

        public List<Movie> movies;

        public Ranking(List<Movie> movies) {
            this.movies = movies;
            Collections.sort(movies, new Comparator<Movie>() {
                @Override
                public int compare(Movie o1, Movie o2) {
                    return o2.totalVotes.compareTo(o1.totalVotes);
                }
            });

            for (Movie movie: movies)
                total += movie.totalVotes;
        }
    }

    private static final long serialVersionUID = -7581038385020788497L;

    @Column(nullable = false)
    public String title;

    @Column(length = 1000, nullable = false)
    public String synopsis;

    @Column(nullable = false)
    public Integer duration;

    @Column(nullable = false)
    public String stars;

    @Column(nullable = false)
    public String categories;

    @OneToMany(mappedBy = "selected")
    public List<Vote> votes;

    public transient Long totalVotes;

    public Movie() {
        super();
    }

    public Movie(Long id) {
        this();
        super.id = id;
    }

    public Movie(String title, String synopsis, Integer duration, String stars, String categories) {
        this();
        this.title = title;
        this.synopsis = synopsis;
        this.duration = duration;
        this.stars = stars;
        this.categories = categories;
    }

    public Movie(Long id, String title, Long votes) {
        this(id);
        this.title = title;
        this.totalVotes = votes;
    }

    public static Movie findById(Long id) {
        return JPA.em().find(Movie.class, id);
    }

    public static List<Movie> findAll() {
        return JPA.em().createQuery("from Movie").getResultList();
    }

    public static Ranking getRanking() {
        List<Movie> movies = JPA.em()
                .createQuery("select new Movie(movie.id, movie.title, count(vote)) from Movie as movie left join movie.votes as vote group by movie")
                .getResultList();

        return new Ranking(movies);
    }

    public static Ranking getRankingByUser(User user) {
        List<Movie> movies = JPA.em()
            .createQuery(
                "select new Movie(movie.id, movie.title, sum(case when poll.user = :user then 1 else 0 end)) " +
                "from Movie as movie " +
                    "left join movie.votes as vote " +
                    "left join vote.poll as poll " +
                "group by movie"
            )
            .setParameter("user", user)
            .getResultList();

        return new Ranking(movies);
    }

}
