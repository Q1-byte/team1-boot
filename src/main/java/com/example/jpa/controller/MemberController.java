package com.example.jpa.controller;

import com.example.jpa.domain.Member;
import com.example.jpa.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/list")
    public void getList(@PageableDefault(size = 3, sort = "memberId",
            direction = Sort.Direction.DESC) Pageable pageable, Model model){

        Page<Member> memberPage = memberService.findByAll(pageable);

        // 1. 실제 데이타 리스트
        model.addAttribute("memberList", memberPage.getContent());

        log.info("memberPage.hasPrevious() : " + memberPage.hasPrevious());
        log.info("memberPage.hasNext() : " + memberPage.hasNext());
        log.info("memberPage.getNumber() : " + memberPage.getNumber());

        // 2. 페이지 정보(HTML에서 버튼 만들 때 사용)
        model.addAttribute("page", memberPage);

    }

    // @GetMapping("/list")
    public void getList(Model model) {
        List<Member> memberList = memberService.findByAll();
        model.addAttribute("memberList", memberList);
    }

    @GetMapping("/new")
    public void getNew() {

    }

    @PostMapping("/new")
    public String postNew(Member member) {
        memberService.insert(member);
        return "redirect:/members/list";
    }

    @GetMapping("/delete/{memberId}")
    public String delete(@PathVariable("memberId") int memberId) {
        memberService.delete(memberId);
        return "redirect:/members/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") int memberId, Model model) {
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "members/edit";
    }

    @PostMapping("/edit/{id}")
    public String deitPost(@PathVariable("id") int memberId, Member member) {
        Member oldMember = memberService.findById(memberId);

        oldMember.setName(member.getName());
        oldMember.setAddress(member.getAddress());
        oldMember.setPhone(member.getPhone());
        oldMember.setAge(member.getAge());

        memberService.update(oldMember);

        return "redirect:/members/list";
    }

}
