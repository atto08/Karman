package com.project.Karman.service;

import com.project.Karman.config.jwt.JwtTokenProvider;
import com.project.Karman.domain.entity.Member;
import com.project.Karman.dto.request.LoginRequestDto;
import com.project.Karman.dto.request.SignupRequestDto;
import com.project.Karman.dto.response.JwtTokenDto;
import com.project.Karman.repository.MemberRepository;
import com.project.Karman.service.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        // 이메일 중복 체크
        if (memberRepository.existsByEmail(signupRequestDto.email())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        // 패스워드 일치 체크
        if (!signupRequestDto.password().equals(signupRequestDto.passwordCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 비밀번호 인코딩
        String hashedPassword = passwordEncoder.encode(signupRequestDto.password());
        // 객체 생성 및 저장
        Member member = memberMapper.toEntity(signupRequestDto, hashedPassword);
        memberRepository.save(member);
    }


    @Transactional(readOnly = true)
    public JwtTokenDto login(LoginRequestDto loginRequestDto) {
        // 유저 찾기
        Member member = memberRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new NoSuchElementException("가입하지 않는 이메일입니다."));

        // 비밀번호 일치확인
        if (!passwordEncoder.matches(loginRequestDto.password(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 생성 및 반환
        return jwtTokenProvider.createToken(loginRequestDto.email());
    }
}
