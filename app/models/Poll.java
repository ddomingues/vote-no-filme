package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class Poll extends BaseEntity {

    private static final long serialVersionUID = -7581038385010788497L;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
    public List<Vote> votes;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    public User user;

    private transient Integer stage = 0;

    public static Poll newInstance() {
        Poll poll = new Poll();
        generateCombination(poll);
        return poll;
    }

    private static void generateCombination(Poll poll) {
        List<Movie> movies = Movie.findAll();

        for (int i=0; i<(movies.size()-1); i++)
            for (int j=i+1; j<movies.size(); j++)
                poll.addVote(new Vote(movies.get(i), movies.get(j)));

        Collections.shuffle(poll.votes);
    }

    public Vote getCurrentVote() {
        return isFinalized()? null: this.votes.get(this.stage);
    }

    public Integer getCurrentStage() {
        return this.stage+1;
    }

    public Integer getTotalStages() {
        return this.votes.size();
    }

    private void addVote(Vote vote) {
        if (votes == null)
            votes = new ArrayList<>();

        vote.poll = this;
        votes.add(vote);
    }

    public void vote(Movie movie) {
        if (isFinalized())
            throw new IllegalArgumentException("Poll finalized!");

        Vote vote = votes.get(stage);
        vote.selected(movie);
        ++this.stage;
    }

    public boolean isFinalized() {
        return stage == this.votes.size();
    }
}
