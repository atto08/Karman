package com.project.Karman.domain.vo;

import lombok.Getter;

@Getter
public class MatchScoreDelta {

    private final long scoreGoal;
    private final long concedeGoal;

    public MatchScoreDelta(long scoreGoal, long concedeGoal) {
        this.scoreGoal = scoreGoal;
        this.concedeGoal = concedeGoal;
    }
}
