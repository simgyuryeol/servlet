package hello.servlet.domain.member;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance(); //싱글톤이므로 new를 쓰면 안딘다

    @AfterEach //하나의 테스트가 끝나면 초기화 됨
    void afterEach(){
        memberRepository.clearStore();
    }

    @Test
    void save(){
        //given
        Member member1 = new Member("sim",25);

        //when
        Member save1 = memberRepository.save(member1);
        //then
        Member findmember = memberRepository.findById(save1.getId());
        Assertions.assertThat(findmember).isEqualTo(save1);
    }

    @Test
    void findAll(){
        //given
        Member member = new Member("sim",25);
        Member member2 = new Member("kim",35);

        memberRepository.save(member);
        memberRepository.save(member2);
        //when
        List<Member> findmember = memberRepository.findAll();
        //then
        Assertions.assertThat(findmember.size()).isEqualTo(2);
        Assertions.assertThat(findmember).contains(member,member2);


    }
}
