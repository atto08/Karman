package com.project.Karman.dto.request;

import java.util.List;
import java.util.UUID;

public record AttendPlayerRequestDto(
        List<UUID> attendPlayers) {
}
