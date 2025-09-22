package com.project.Karman.controller;

import com.project.Karman.dto.ClubRequestDto;
import com.project.Karman.dto.PlayersInfoResponse;
import com.project.Karman.dto.SearchClubResponseDto;
import com.project.Karman.service.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> createClub(@RequestHeader UUID memberId,
                                             @RequestBody ClubRequestDto request) {
        clubService.createClub(memberId, request);
        return new ResponseEntity<>("클럽 생성 완료", HttpStatus.OK);
    }

    @PatchMapping("/{club_id}")
    public ResponseEntity<String> modifyClub(@PathVariable(value = "club_id") UUID clubId,
                                             @RequestHeader UUID memberId,
                                             @RequestBody ClubRequestDto request) {
        clubService.modifyClubInfo(clubId, memberId, request);
        return new ResponseEntity<>("클럽 수정 완료", HttpStatus.OK);
    }

    @DeleteMapping("/{club_id}")
    public ResponseEntity<String> deleteClub(@PathVariable(value = "club_id") UUID clubId,
                                             @RequestHeader UUID memberId) {
        clubService.deleteClub(clubId, memberId);
        return new ResponseEntity<>("클럽 삭제 완료", HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchClubResponseDto>> searchClub(@RequestParam(name = "clubName") String param) {
        List<SearchClubResponseDto> searchClubDtoList = clubService.searchClub(param);
        return new ResponseEntity<>(searchClubDtoList, HttpStatus.OK);
    }

    // 클럽 가입

    // 클럽 탈퇴
}
