package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest  //Spring 위에서 테스트
@Transactional  //기본적으로 데이터를 insert 하지 않고 트랜젼션 롤백함.
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(value = false)  // insert 수행하고 트랜잭션 commit 함.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush();  // insert 수행하고 트랜젼선 롤백함.
        assertEquals(member, memberRepository.findOne(saveId));  // 동일 트랜잰션에서의 PK가 같은 엔티티는 동일한 것으로 관리됨.
    }

//    @Test(expected = IllegalStateException.class)  //Junit4에서만 지원
    @Test
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        // try-catch 방식: 복잡하다.
//        try {
//            memberService.join(member2);  //예외가 발생해야 한다.
//        } catch (IllegalStateException e) {
//            return;
//        }
        //then
//        fail("예외가 발생해야 한다.");

        // Junit5 방식: assertThrows
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }
}