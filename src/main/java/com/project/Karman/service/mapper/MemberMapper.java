package com.project.Karman.service.mapper;

import com.project.Karman.domain.entity.Member;
import com.project.Karman.dto.request.SignupRequestDto;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {

    public Member toEntity(SignupRequestDto request, String hashedPassword) {

        return Member.of(
                request.email(),
                hashedPassword,
                request.name(),
                request.age(),
                request.weight(),
                request.height()
        );
    }

}
