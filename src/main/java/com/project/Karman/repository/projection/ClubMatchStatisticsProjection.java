package com.project.Karman.repository.projection;

public interface ClubMatchStatisticsProjection {

    Long getMatchCount();

    Long getWin();

    Long getDraw();

    Long getLose();

    Long getTotalScoreGoal();

    Long getTotalConcedeGoal();
}
