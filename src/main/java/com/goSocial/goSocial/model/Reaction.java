package com.goSocial.goSocial.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "reactions")
@Getter
@Setter
@Embeddable
public class Reaction {

    private String whoReacted;
    private int whatReacted;

    // Constructors
    public Reaction() {}

    public Reaction(String whoReacted, int whatReacted) {
        this.whoReacted = whoReacted;
        this.whatReacted = whatReacted;
    }

}
