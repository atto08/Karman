package com.project.Karman.controller;

import com.project.Karman.dto.request.AskTacticsRequestDto;
import com.project.Karman.dto.response.AiCoachResponseDto;
import com.project.Karman.dto.response.ApiResponse;
import com.project.Karman.service.TacticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.Karman.exception.SuccessMessage.*;

@RestController
@RequiredArgsConstructor
public class TacticsController {

    private final TacticsService tacticsService;

    @PostMapping("/tactics/index")
    public ResponseEntity<String> indexTactics(@RequestBody String request) {
        tacticsService.indexTactics(request);
        return ResponseEntity.ok("인덱싱 성공");
    }

    @PostMapping("/tactics/ask")
    public ResponseEntity<ApiResponse<AiCoachResponseDto>> askTacticalCoach(@RequestBody AskTacticsRequestDto request) {
        AiCoachResponseDto aiCoachResponseDto = tacticsService.askTacticalCoach(request);
        return ResponseEntity
                .status(GET_AI_SEARCH_TACTICS_RESPONSE.getHttpStatus())
                .body(ApiResponse.success(GET_AI_SEARCH_TACTICS_RESPONSE.getMessage(), aiCoachResponseDto));
    }
}
