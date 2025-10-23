package com.project.Karman.dto.response;

import com.project.Karman.domain.enums.GoalAssist;

import java.util.UUID;

public record GoalAssistPlayer(
        UUID affiliationId,
        GoalAssist type
) {
}
