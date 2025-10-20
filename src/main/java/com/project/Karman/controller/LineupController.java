package com.project.Karman.controller;

import com.project.Karman.config.security.CustomUserDetails;
import com.project.Karman.dto.request.AttendPlayerRequestDto;
import com.project.Karman.service.LineupService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
public class LineupController {

    private final LineupService lineupService;

    public LineupController(LineupService lineupService) {
        this.lineupService = lineupService;
    }

    @PostMapping("/clubs/{club_id}/lineup/recommend")
    public Map<String, String> recommendLineup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable(value = "club_id") UUID clubId,
                                               @RequestBody AttendPlayerRequestDto requestDto) {
        String message = lineupService.recommendLineup(clubId, requestDto.attendPlayers());
        return Map.of("ai-response", message);
    }
}
