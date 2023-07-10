package hello.jdbc.service;

import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * 기본 동작, 트랜잭션이 없어서 문제가 발생
 * */
class MemberServiceV1Test {

    public static final String MEMEBER_A = "memberA";
    public static final String MEMEBER_B = "memberB";
    public static final String MEMEBER_EX = "ex";

    private MemberRepositoryV1 memberRepository;
    private MemberServiceV1 memberService;

    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MEMEBER_A);
        memberRepository.delete(MEMEBER_B);
        memberRepository.delete(MEMEBER_EX);
    }

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV1(dataSource);
        memberService = new MemberServiceV1(memberRepository);
    }
    @Test
    @DisplayName("정상 이체")
    void accountTransfer_test() throws SQLException {
        // given
        Member memberA = new Member(MEMEBER_A, 10000);
        Member memberB = new Member(MEMEBER_B, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        // when
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        // then
        Member findA = memberRepository.findById(MEMEBER_A);
        Member findB = memberRepository.findById(MEMEBER_B);
        assertThat(findA.getMoney()).isEqualTo(8000);
        assertThat(findB.getMoney()).isEqualTo(12000);
    }
    @Test
    @DisplayName("이체중 예외 발생")
    void accountTransferEx_test() throws SQLException {
        // given
        Member memberA = new Member(MEMEBER_A, 10000);
        Member memberEX = new Member(MEMEBER_EX, 10000);
        memberRepository.save(memberA);
        memberRepository.save(memberEX);

        // when
        assertThatThrownBy(() ->
                memberService.accountTransfer(memberA.getMemberId(), memberEX.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        // then
        Member findA = memberRepository.findById(MEMEBER_A);
        Member findEX = memberRepository.findById(MEMEBER_EX);
        assertThat(findA.getMoney()).isEqualTo(8000);
        assertThat(findEX.getMoney()).isEqualTo(10000);
    }
}