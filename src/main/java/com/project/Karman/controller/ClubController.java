package com.project.Karman.controller;

import com.project.Karman.config.security.CustomUserDetails;
import com.project.Karman.dto.request.*;
import com.project.Karman.dto.response.*;
import com.project.Karman.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<SearchClubListResponseDto>> searchClubs(@RequestParam(name = "clubName") String param) {
        SearchClubListResponseDto searchClubDtoList = clubService.searchClub(param);
        return ResponseEntity
                .status(SEARCH_CLUB.getHttpStatus())
                .body(ApiResponse.success(SEARCH_CLUB.getMessage(), searchClubDtoList));
    }

    @GetMapping("/{club_id}/player-list")
    public ResponseEntity<ApiResponse<PlayerInfoListResponseDto>> getPlayerList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                @PathVariable(value = "club_id") UUID clubId) {
        PlayerInfoListResponseDto playerInfoList = clubService.getPlayerInfoList(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(GET_PLAYERS_INFO_IN_CLUB.getHttpStatus())
                .body(ApiResponse.success(GET_PLAYERS_INFO_IN_CLUB.getMessage(), playerInfoList));
    }

    @GetMapping("/{club_id}/squad")
    public ResponseEntity<ApiResponse<PlayerSelectListResponseDto>> getPlayerSelectList(@PathVariable(value = "club_id") UUID clubId) {
        PlayerSelectListResponseDto playerSelectList = clubService.findPlayerSelectList(clubId);
        return ResponseEntity
                .status(GET_PLAYER_SELECT_IN_CLUB.getHttpStatus())
                .body(ApiResponse.success(GET_PLAYER_SELECT_IN_CLUB.getMessage(), playerSelectList));
    }

    @PostMapping("/{club_id}/join-requests")
    public ResponseEntity<ApiResponse<Void>> requestJoinClub(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable(value = "club_id") UUID clubId) {
        clubService.requestJoinClub(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(REQUEST_JOIN_CLUB.getHttpStatus())
                .body(ApiResponse.success(REQUEST_JOIN_CLUB.getMessage()));
    }

    @PatchMapping("/{club_id}/join-requests/{player_id}")
    public ResponseEntity<ApiResponse<Void>> updateClubJoinStatus(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @PathVariable(value = "club_id") UUID clubId,
                                                                  @PathVariable(value = "player_id") UUID playerId,
                                                                  @Valid @RequestBody JoinStatusUpdateRequestDto requestDto) {
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

    @PatchMapping("/{club_id}/players/{player_id}")
    public ResponseEntity<ApiResponse<Void>> updatePlayerInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @PathVariable(value = "club_id") UUID clubId,
                                                              @PathVariable(value = "player_id") UUID playerId,
                                                              @Valid @RequestBody PlayerInfoUpdateRequestDto requestDto) {
        clubService.updatePlayerInfo(userDetails.getMember(), clubId, playerId, requestDto);
        return ResponseEntity
                .status(UPDATE_PLAYER_INFO.getHttpStatus())
                .body(ApiResponse.success(UPDATE_PLAYER_INFO.getMessage()));
    }

    @GetMapping("/{club_id}/statics-records")
    public ResponseEntity<ApiResponse<ClubStatisticsRecordsResponseDto>> getStaticsRecords(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                           @PathVariable(value = "club_id") UUID clubId) {
        ClubStatisticsRecordsResponseDto clubStatisticsRecordsResponseDto = clubService.getStaticsRecords(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(GET_CLUB_STATICS_RECORDS.getHttpStatus())
                .body(ApiResponse.success(GET_CLUB_STATICS_RECORDS.getMessage(), clubStatisticsRecordsResponseDto));
    }

    @PostMapping("/{club_id}/player")
    public ResponseEntity<ApiResponse<Void>> createPlayer(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                          @PathVariable(value = "club_id") UUID clubId,
                                                          @Valid @RequestBody PlayerCreateRequestDto requestDto) {
        clubService.addPlayerWithoutMember(userDetails.getMember(), clubId, requestDto);
        return ResponseEntity
                .status(ADD_PLAYER_IN_CLUB.getHttpStatus())
                .body(ApiResponse.success(ADD_PLAYER_IN_CLUB.getMessage()));
    }

    @GetMapping("/{club_id}/join-requests")
    public ResponseEntity<ApiResponse<ClubJoinRequestListResponseDto>> getClubJoinRequests(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                           @PathVariable(value = "club_id") UUID clubId) {
        ClubJoinRequestListResponseDto clubJoinRequestListResponseDto = clubService.getClubJoinRequests(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(GET_CLUB_JOIN_REQUESTS.getHttpStatus())
                .body(ApiResponse.success(GET_CLUB_JOIN_REQUESTS.getMessage(), clubJoinRequestListResponseDto));
    }

    @GetMapping("/{club_id}/members")
    public ResponseEntity<ApiResponse<ClubMembersListResponseDto>> getClubMembers(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                  @PathVariable(value = "club_id") UUID clubId) {
        ClubMembersListResponseDto clubMemberResponseDto = clubService.getClubMembers(userDetails.getMember(), clubId);
        return ResponseEntity
                .status(GET_CLUB_MEMBERS.getHttpStatus())
                .body(ApiResponse.success(GET_CLUB_MEMBERS.getMessage(), clubMemberResponseDto));
    }

    @PostMapping("/{club_id}/players/transfers")
    public ResponseEntity<ApiResponse<Void>> transferAffiliationRecord(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @PathVariable(value = "club_id") UUID clubId,
                                                                       @Valid @RequestBody TransferPlayerRecordRequestDto requestDto) {
        clubService.transferPlayerRecord(userDetails.getMember(), clubId, requestDto);
        return ResponseEntity
                .status(TRANSFER_PLAYER_RECORD.getHttpStatus())
                .body(ApiResponse.success(TRANSFER_PLAYER_RECORD.getMessage()));
    }
}
