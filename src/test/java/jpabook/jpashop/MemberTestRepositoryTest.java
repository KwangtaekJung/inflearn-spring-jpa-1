package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberTestRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void testMember() throws Exception {
        //given
        MemberTest memberTest = new MemberTest();
        memberTest.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(memberTest);
        MemberTest findMemberTest = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMemberTest.getId()).isEqualTo(memberTest.getId());
        Assertions.assertThat(findMemberTest.getUsername()).isEqualTo(memberTest.getUsername());
        System.out.println("breakpoint");
    }
}