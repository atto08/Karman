package com.project.Karman.controller;

import com.project.Karman.config.security.CustomUserDetails;
import com.project.Karman.dto.request.ClubCreateRequestDto;
import com.project.Karman.dto.request.ClubUpdateRequestDto;
import com.project.Karman.dto.request.JoinStatusUpdateRequestDto;
import com.project.Karman.dto.response.ApiResponse;
import com.project.Karman.dto.response.ClubInfoResponseDto;
import com.project.Karman.dto.response.PlayersInfoResponse;
import com.project.Karman.dto.response.SearchClubResponseDto;
import com.project.Karman.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.project.Karman.exception.SuccessMessage.*;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }


    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createClub(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @Valid @RequestBody ClubCreateRequestDto requestDto) {
        clubService.createClub(userDetails.getMember(), requestDto);
        return ResponseEntity
                .status(CREATE_CLUB.getHttpStatus())
                .body(ApiResponse.success(CREATE_CLUB.getMessage()));
    }

    @GetMapping("/{club_id}")
    public ResponseEntity<ApiResponse<ClubInfoResponseDto>> getClubInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathVariable(value = "club_id") UUID clubId) {
        ClubInfoResponseDto clubInfoDto = clubService.getClubInfo(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(GET_CLUB_INFO.getHttpStatus())
                .body(ApiResponse.success(GET_CLUB_INFO.getMessage(), clubInfoDto));
    }

    @PatchMapping("/{club_id}")
    public ResponseEntity<ApiResponse<Void>> modifyClub(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable(value = "club_id") UUID clubId,
                                                        @Valid @RequestBody ClubUpdateRequestDto requestDto) {
        clubService.modifyClubInfo(userDetails.getMember(), clubId, requestDto);
        return ResponseEntity
                .status(UPDATE_CLUB.getHttpStatus())
                .body(ApiResponse.success(UPDATE_CLUB.getMessage()));
    }

    @DeleteMapping("/{club_id}")
    public ResponseEntity<ApiResponse<Void>> deleteClub(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable(value = "club_id") UUID clubId) {
        clubService.deleteClub(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(DELETE_CLUB.getHttpStatus())
                .body(ApiResponse.success(DELETE_CLUB.getMessage()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SearchClubResponseDto>>> searchClub(@RequestParam(name = "clubName") String param) {
        List<SearchClubResponseDto> searchClubDtoList = clubService.searchClub(param);
        return ResponseEntity
                .status(SEARCH_CLUB.getHttpStatus())
                .body(ApiResponse.success(SEARCH_CLUB.getMessage(), searchClubDtoList));
    }

    @GetMapping("/{club_id}/player-list")
    public ResponseEntity<ApiResponse<List<PlayersInfoResponse>>> getPlayersByClub(@PathVariable(value = "club_id") UUID clubId) {
        List<PlayersInfoResponse> playersInfo = clubService.findPlayersInfoByClub(clubId);
        return ResponseEntity
                .status(GET_PLAYERS_IN_CLUB.getHttpStatus())
                .body(ApiResponse.success(GET_PLAYERS_IN_CLUB.getMessage(), playersInfo));
    }

    @PostMapping("/{club_id}/join")
    public ResponseEntity<ApiResponse<Void>> requestJoinClub(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable(value = "club_id") UUID clubId) {
        clubService.requestJoinClub(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(REQUEST_JOIN_CLUB.getHttpStatus())
                .body(ApiResponse.success(REQUEST_JOIN_CLUB.getMessage()));
    }

    @PatchMapping("/{club_id}/players/{player_id}/update")
    public ResponseEntity<ApiResponse<Void>> updateClubJoinStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @PathVariable(value = "club_id") UUID clubId,
                                                                  @PathVariable(value = "player_id") UUID playerId,
                                                                  @RequestBody JoinStatusUpdateRequestDto requestDto) {
        String message = clubService.updateClubJoinStatus(userDetails.getMember(), clubId, playerId, requestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(message));
    }

    @DeleteMapping("/{club_id}/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdrawClub(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @PathVariable(value = "club_id") UUID clubId) {
        clubService.withdrawClub(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(WITHDRAW_CLUB.getHttpStatus())
                .body(ApiResponse.success(WITHDRAW_CLUB.getMessage()));
    }
}
