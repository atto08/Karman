package com.project.Karman.dto.request;

import java.util.List;
import java.util.UUID;

public record AttendPlayerRequest(
        UUID clubId,
        List<UUID> attendPlayers) {
}
