package com.project.Karman.dto.response;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ClubStaticsRecordsResponseDto(
        // 총 경기수
        // 총 승,무,패 수
        // 총 득점
        // 총 실점
        Long matchCount,
        Long win,
        Long draw,
        Long lose,
        Long totalScoreGoal,
        Long totalConcedeGoal
) {

    public static ClubStaticsRecordsResponseDto of(Long matchCount, Long win, Long draw, Long lose, Long totalScoreGoal, Long totalConcedeGoal) {

        return ClubStaticsRecordsResponseDto.builder()
                .matchCount(matchCount)
                .win(win)
                .draw(draw)
                .lose(lose)
                .totalScoreGoal(totalScoreGoal)
                .totalConcedeGoal(totalConcedeGoal)
                .build();
    }
}
