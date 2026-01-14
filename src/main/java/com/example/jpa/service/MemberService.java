package com.example.jpa.service;

import com.example.jpa.domain.Member;
import com.example.jpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 수정
    public void update(Member member) {
        memberRepository.save(member);
    }

    // 등록
    public void insert(Member member) {
        memberRepository.save(member);
    }

    // 삭제
    public void delete(int memberId) {
        memberRepository.deleteById(memberId);
    }

    // 조회
    public Member findById(int memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당 회원이 없습니다."));

        return member;
    }

    // 전체 데이타 조회
    public List<Member> findByAll() {
        return memberRepository.findAll();
    }

    // 전체 데이타 조회(페이징 처리 포함)
    public Page<Member> findByAll(Pageable pageable){

        log.info("_______________findByAll________________");
        log.info("pageable : " + pageable);

        Page<Member> memberPage = memberRepository.findAll(pageable);

        log.info("_______________memberPage________________");
        log.info("memberPage : " + memberPage);

        return memberPage;
    }
}
