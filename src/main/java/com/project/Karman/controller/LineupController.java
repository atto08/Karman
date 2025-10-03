package com.project.Karman.controller;

import com.project.Karman.dto.request.AttendPlayerRequestDto;
import com.project.Karman.service.LineupService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LineupController {

    private final LineupService lineupService;

    public LineupController(LineupService lineupService) {
        this.lineupService = lineupService;
    }

    @PostMapping("/lineup/recommend")
    public Map<String, String> recommendLineup(@RequestBody AttendPlayerRequestDto request) {
        String message = lineupService.recommendLineup(request.attendPlayers(), request.clubId());
        return Map.of("ai-response", message);
    }
}
