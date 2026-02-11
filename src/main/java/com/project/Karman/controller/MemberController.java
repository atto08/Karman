package com.project.Karman.controller;

import com.project.Karman.config.security.CustomUserDetails;
import com.project.Karman.dto.response.ApiResponse;
import com.project.Karman.dto.response.MyClubListResponseDto;
import com.project.Karman.exception.SuccessMessage;
import com.project.Karman.service.ClubService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final ClubService clubService;

    public MemberController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/members/me/clubs")
    ResponseEntity<ApiResponse<MyClubListResponseDto>> getMyClubList(@AuthenticationPrincipal CustomUserDetails userDetails) {

        MyClubListResponseDto clubIdListResponseDto = clubService.getMyClubList(userDetails.getMember());
        return ResponseEntity
                .status(SuccessMessage.GET_MY_CLUBS.getHttpStatus())
                .body(ApiResponse.success(SuccessMessage.GET_MY_CLUBS.getMessage(), clubIdListResponseDto));
    }
}
