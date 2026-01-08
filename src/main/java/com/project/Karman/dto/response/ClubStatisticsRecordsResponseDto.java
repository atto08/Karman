package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ClubStatisticsRecordsResponseDto(
        Long matchCount,
        Long win,
        Long draw,
        Long lose,
        Long totalScoreGoal,
        Long totalConcedeGoal
) {

    public static ClubStatisticsRecordsResponseDto of(Long matchCount, Long win, Long draw, Long lose, Long totalScoreGoal, Long totalConcedeGoal) {

        return ClubStatisticsRecordsResponseDto.builder()
                .matchCount(matchCount)
                .win(win)
                .draw(draw)
                .lose(lose)
                .totalScoreGoal(totalScoreGoal)
                .totalConcedeGoal(totalConcedeGoal)
                .build();
    }
}
