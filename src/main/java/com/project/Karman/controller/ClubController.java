package com.project.Karman.controller;

import com.project.Karman.dto.PlayersInfoResponse;
import com.project.Karman.service.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
