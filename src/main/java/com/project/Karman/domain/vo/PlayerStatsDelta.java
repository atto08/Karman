package com.project.Karman.domain.vo;

import lombok.Getter;

@Getter
public class PlayerStatsDelta {

    private long matchCount;
    private long goal;
    private long assist;

    public PlayerStatsDelta(long matchCount, long goal, long assist) {
        this.matchCount = matchCount;
        this.goal = goal;
        this.assist = assist;
    }

    public void addMatchCount() {
        this.matchCount++;
    }

    public void addGoal() {
        this.goal++;
    }

    public void addAssist() {
        this.assist++;
    }

    public void minusMatchCount() {
        this.matchCount--;
    }

    public void minusGoal() {
        this.goal--;
    }

    public void minusAssist() {
        this.assist--;
    }
}
