package com.goSocial.goSocial.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class ReactionUtil {

    private String whoReacted;
    private int whatReacted;

    // Constructors
    public ReactionUtil() {}

    public ReactionUtil(String whoReacted, int whatReacted) {
        this.whoReacted = whoReacted;
        this.whatReacted = whatReacted;
    }

}
