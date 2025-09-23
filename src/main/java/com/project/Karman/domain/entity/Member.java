package com.project.Karman.domain.entity;

import com.project.Karman.domain.enums.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "member")
@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID memberId;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 10)
    @Enumerated(value = EnumType.STRING)
    private MemberRole role;

    @Column(nullable = false, length = 10)
    private Integer age;

    @Column(nullable = false, length = 10)
    private BigDecimal weight;

    @Column(nullable = false, length = 10)
    private BigDecimal height;

    // 엔터티 생성 사용
    public static Member of(String email, String hashedPassword, String name, Integer age, BigDecimal weight, BigDecimal height) {

        return Member.builder()
                .email(email)
                .password(hashedPassword)
                .name(name)
                .role(MemberRole.USER)
                .age(age)
                .weight(weight)
                .height(height)
                .build();
    }
}
