package hello.jdbc.repository;

import hello.jdbc.domain.Member;

import java.sql.SQLException;
/**
 * 특정 기술에 종속된 인터페이스
 * 사용하면 안된다.
 * */
public interface MemberRepositoryEx {

    public interface MemberRepository {
        Member save(Member member) throws SQLException;
        Member findById(String memberId) throws SQLException;
        void update(String memberId, int money) throws SQLException;
        void delete(String memberId) throws SQLException;
    }
}
