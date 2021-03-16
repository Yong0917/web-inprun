package hello.hellospring.controller;

import java.util.List;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class MemberController {

    private final MemberService memberService;			// @Autowired 필드 주입도 가능

    @Autowired			//연결시켜줘야되는데 service랑 연결시켜주는거  DI
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";		//redirect: 이거 다음에 /  을 입력햇기떄문에 home으로 돌아감
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();			//findMembers하면 모든멤버를 다 가지고올수 있음
        model.addAttribute("members", members);			// html에 있는 members에 members 매칭
        return "members/memberList";
    }
}