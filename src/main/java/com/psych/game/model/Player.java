package com.psych.game.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;


@Entity
@Table(name = "players")
public class Player extends Auditable{

    @Getter
    @Setter
    @NotBlank
    private String name;

    @Getter
    @Setter
    @URL
    private String psychFaceUrl;

    @Getter
    @Setter
    @URL
    private String picUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @Getter
    @Setter

    private Stats stats = new Stats();

    @ManyToMany(mappedBy = "players")
    @Getter
    @Setter
    @JsonIdentityReference
    private List<Game> games;


    public Player(){ }


    private Player(Builder builder) {
        this.name = builder.name;
        this.psychFaceUrl = builder.psychFaceUrl;
        this.picUrl = builder.picUrl;
    }

    public static Builder newPlayer() {
        return new Builder();
    }


    public static final class Builder {
        private @NotBlank String name;
        private @URL String psychFaceUrl;
        private @URL String picUrl;

        public Builder() {
        }

        public Player build() {
            return new Player(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder psychFaceUrl(String psychFaceUrl) {
            this.psychFaceUrl = psychFaceUrl;
            return this;
        }

        public Builder picUrl(String picUrl) {
            this.picUrl = picUrl;
            return this;
        }
    }
}
