package com.project.Karman.controller;

import com.project.Karman.config.security.CustomUserDetails;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.dto.request.MatchQuarterCreateRequestDto;
import com.project.Karman.dto.request.MatchQuarterUpdateRequestDto;
import com.project.Karman.dto.response.ApiResponse;
import com.project.Karman.dto.response.MatchListResponseDto;
import com.project.Karman.exception.SuccessMessage;
import com.project.Karman.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clubs/{club_id}/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping
    public ResponseEntity<String> createMatch(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable(value = "club_id") UUID clubId,
                                              @Valid @RequestBody MatchCreateRequestDto requestDto) {
        matchService.createMatch(requestDto, clubId, userDetails.getMember());
        return new ResponseEntity<>("경기 등록 완료", HttpStatus.OK);
    }

    @PostMapping("/{match_id}/quarters")
    public ResponseEntity<ApiResponse<Void>> createMatchQuarter(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable(value = "club_id") UUID clubId,
                                                                @PathVariable(value = "match_id") UUID matchId,
                                                                @Valid @RequestBody MatchQuarterCreateRequestDto requestDto) {
        matchService.createMatchQuarter(requestDto, clubId, matchId, userDetails.getMember());
        return ResponseEntity
                .status(SuccessMessage.CREATE_MATCH_QUARTER.getHttpStatus())
                .body(ApiResponse.success(SuccessMessage.CREATE_MATCH_QUARTER.getMessage()));
    }

    @PatchMapping("/{match_id}/quarters")
    public ResponseEntity<ApiResponse<Void>> modifyMatchQuarter(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                @PathVariable(value = "club_id") UUID clubId,
                                                                @PathVariable(value = "match_id") UUID matchId,
                                                                @Param(value = "quarter") Integer quarter,
                                                                @Valid @RequestBody MatchQuarterUpdateRequestDto requestDto) {
        matchService.updateMatchQuarter(requestDto, clubId, matchId, userDetails.getMember(), quarter);
        return ResponseEntity
                .status(SuccessMessage.UPDATE_MATHC_QUARTER.getHttpStatus())
                .body(ApiResponse.success(SuccessMessage.UPDATE_MATHC_QUARTER.getMessage()));
    }

    // 매치 전체 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<MatchListResponseDto>>> getMatchList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                @PathVariable(value = "club_id") UUID clubId) {
        List<MatchListResponseDto> matchAll = matchService.getMatchAll(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(SuccessMessage.GET_MATCH_ALL.getHttpStatus())
                .body(ApiResponse.success(SuccessMessage.GET_MATCH_ALL.getMessage(), matchAll));
    }

    // 매치 상세 조회

}
