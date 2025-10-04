package com.project.Karman.controller;

import com.project.Karman.config.security.CustomUserDetails;
import com.project.Karman.dto.request.MatchCreateRequestDto;
import com.project.Karman.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
                                              @Valid @RequestBody MatchCreateRequestDto request) {
        matchService.createMatch(request, clubId, userDetails.getmember());
        return new ResponseEntity<>("경기 등록 완료", HttpStatus.OK);
    }
}
