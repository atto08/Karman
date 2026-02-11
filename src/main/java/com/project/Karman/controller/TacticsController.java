package com.project.Karman.controller;

import com.project.Karman.dto.request.AskTacticsRequestDto;
import com.project.Karman.dto.response.AiCoachResponseDto;
import com.project.Karman.dto.response.ApiResponse;
import com.project.Karman.service.TacticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.project.Karman.exception.SuccessMessage.*;

@RestController
@RequestMapping("/ai/tactics")
public class TacticsController {

    private final TacticsService tacticsService;

    public TacticsController(TacticsService tacticsService) {
        this.tacticsService = tacticsService;
    }

    @PostMapping("/documents")
    public ResponseEntity<ApiResponse<Void>> indexTactics(@RequestBody String request) {
        tacticsService.indexTactics(request);
        return ResponseEntity
                .status(INDEXING_SUCCESS_RESPONSE.getHttpStatus())
                .body(ApiResponse.success(INDEXING_SUCCESS_RESPONSE.getMessage()));
    }

    @PostMapping("/ask")
    public ResponseEntity<ApiResponse<AiCoachResponseDto>> askTacticalCoach(@RequestBody AskTacticsRequestDto request) {
        AiCoachResponseDto aiCoachResponseDto = tacticsService.askTacticalCoach(request);
        return ResponseEntity
                .status(GET_AI_SEARCH_TACTICS_RESPONSE.getHttpStatus())
                .body(ApiResponse.success(GET_AI_SEARCH_TACTICS_RESPONSE.getMessage(), aiCoachResponseDto));
    }
}
