package com.project.Karman.controller;

import com.project.Karman.dto.AskTacticsRequest;
import com.project.Karman.service.TacticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TacticsController {

    private final TacticsService tacticsService;

    @PostMapping("/tactics/index")
    public ResponseEntity<String> indexTactics(@RequestBody String request) {
        tacticsService.indexTactics(request);
        return ResponseEntity.ok("인덱싱 성공");
    }

    @PostMapping("/tactics/ask")
    public ResponseEntity<String> askTacticalCoach(@RequestBody AskTacticsRequest request) {
        String response = tacticsService.askTacticalCoach(request);
        return ResponseEntity.ok(response);
    }
}
