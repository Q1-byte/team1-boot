package com.example.jpa.domain.user.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    //추가
    @Test
    public void insertTest(){

        Member member = Member.builder()
                .name("길동")
                .age(2)
                .phone("111")
                .address("강원도")
                .build();

    memberRepository.save(member);
    }

    //수정
    @Test
    public void updateTest() {
        Optional<Member> optMember = memberRepository.findById(3);
        Member member = optMember.get();

        member.setName("뽀양");
        member.setAge(3);
        member.setAddress("카츠동");

        memberRepository.save(member);
    }

    //삭제
    @Test
    public void deleteTest(){

        memberRepository.deleteById(2);
    }

    //전체데이타 조회
    @Test
    public  void  selectAll(){
        List<Member> memberList = memberRepository.findAll();

        memberList.forEach(member -> log.info(member));
    }

    //조회
    @Test
    public void selectTest(){

        List<Member> memberList =
                memberRepository.findByAgeGreaterThanEqual(13);

        memberList.forEach(member -> log.info(member));
        log.info(memberList);
    }

    @Test
    public void likeTest(){
        List<Member> memberList = memberRepository.findByAddressLike("인천%");
        memberList.forEach(member -> log.info(member));
    }

    @Test
    public void orderAge(){
        // List<Member> memberList = memberRepository.findByAddressOrderByAgeAsc("인천");
        List<Member> memberList = memberRepository.findByAgeOrderByDesc(13);
        memberList.forEach(member -> log.info(member));
    }

}
