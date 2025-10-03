package com.project.Karman.controller;

import com.project.Karman.config.security.CustomUserDetails;
import com.project.Karman.dto.*;
import com.project.Karman.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/{club_id}/player-list")
    public ResponseEntity<List<PlayersInfoResponse>> getPlayersByClub(@PathVariable(value = "club_id") UUID clubId) {
        List<PlayersInfoResponse> playersInfo = clubService.findPlayersInfoByClub(clubId);
        return new ResponseEntity<>(playersInfo, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createClub(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Valid @RequestBody ClubCreateRequestDto request) {
        clubService.createClub(userDetails.getmember(), request);
        return new ResponseEntity<>("클럽 생성 완료", HttpStatus.OK);
    }

    @PatchMapping("/{club_id}")
    public ResponseEntity<String> modifyClub(@PathVariable(value = "club_id") UUID clubId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails,
                                             @Valid @RequestBody ClubUpdateRequestDto request) {
        clubService.modifyClubInfo(clubId, userDetails.getmember(), request);
        return new ResponseEntity<>("클럽 수정 완료", HttpStatus.OK);
    }

    @DeleteMapping("/{club_id}")
    public ResponseEntity<String> deleteClub(@PathVariable(value = "club_id") UUID clubId,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        clubService.deleteClub(clubId, userDetails.getmember());
        return new ResponseEntity<>("클럽 삭제 완료", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchClubResponseDto>> searchClub(@RequestParam(name = "clubName") String param) {
        List<SearchClubResponseDto> searchClubDtoList = clubService.searchClub(param);
        return new ResponseEntity<>(searchClubDtoList, HttpStatus.OK);
    }

    @GetMapping("/{club_id}")
    public ResponseEntity<ClubInfoResponseDto> getClubInfo(@PathVariable(value = "club_id") UUID clubId,
                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        ClubInfoResponseDto clubInfoDto = clubService.getClubInfo(clubId, userDetails.getmember());
        return new ResponseEntity<>(clubInfoDto, HttpStatus.OK);
    }

    @PostMapping("/{club_id}/join")
    public ResponseEntity<String> joinClub(@PathVariable(value = "club_id") UUID clubId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        clubService.requestJoinClub(clubId, userDetails.getmember());
        return new ResponseEntity<>("가입 신청 완료", HttpStatus.OK);
    }

    @PatchMapping("/{club_id}/players/{player_id}/update")
    public ResponseEntity<String> updateClubJoinStatus(@PathVariable(value = "club_id") UUID clubId,
                                                       @PathVariable(value = "player_id") UUID playerId,
                                                       @RequestBody JoinStatusUpdateRequestDto request,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {

        String message = clubService.updateClubJoinStatus(clubId, playerId, request, userDetails.getmember());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/{club_id}/withdraw")
    public ResponseEntity<String> withdrawClub(@PathVariable(value = "club_id") UUID clubId,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        clubService.withdrawClub(clubId, userDetails.getmember());
        return new ResponseEntity<>("클럽 탈퇴 완료", HttpStatus.OK);
    }
}
