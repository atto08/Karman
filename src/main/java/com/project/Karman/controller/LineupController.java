package com.project.Karman.controller;

import com.project.Karman.config.security.CustomUserDetails;
import com.project.Karman.dto.request.AttendPlayerRequestDto;
import com.project.Karman.dto.response.ApiResponse;
import com.project.Karman.dto.response.AiCoachResponseDto;
import com.project.Karman.dto.response.LineupRecommendResponseDto;
import com.project.Karman.service.LineupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.project.Karman.exception.SuccessMessage.*;

@RestController
public class LineupController {

    private final LineupService lineupService;

    public LineupController(LineupService lineupService) {
        this.lineupService = lineupService;
    }

    @PostMapping("/clubs/{club_id}/lineup/recommend")
    public ResponseEntity<ApiResponse<LineupRecommendResponseDto>> recommendLineup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                           @PathVariable(value = "club_id") UUID clubId,
                                                                           @RequestBody AttendPlayerRequestDto requestDto) {
        LineupRecommendResponseDto aiCoachResponseDto = lineupService.recommendLineup(userDetails.getMember(), clubId, requestDto.attendPlayers());
        return ResponseEntity
                .status(GET_AI_LINEUP_RECOMMEND_RESPONSE.getHttpStatus())
                .body(ApiResponse.success(GET_AI_LINEUP_RECOMMEND_RESPONSE.getMessage(), aiCoachResponseDto));
    }
}
