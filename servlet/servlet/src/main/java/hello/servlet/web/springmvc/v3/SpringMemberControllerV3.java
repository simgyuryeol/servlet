package hello.servlet.web.springmvc.v3;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/springmvc/v3/members") //중복 제거 가능
public class SpringMemberControllerV3 {
    private MemberRepository memberRepository = MemberRepository.getInstance();

    //@RequestMapping(value = "/new-form",method = RequestMethod.GET) //GET만 가능
    @GetMapping("/new-form")
    public String newForm(){ //애노테이션 기반의 컨트롤러는 유연하게 문자로 반환도 가능하다.
        return "new-form";
    }

    //@RequestMapping(value = "/save",method = RequestMethod.POST)
    @PostMapping("/save")
    public String save(@RequestParam("username") String username,
                             @RequestParam("age") int age,
                             Model model) {

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.addAttribute("member",member);
        return "save-result";
    }

    //@RequestMapping(method = RequestMethod.GET)
    @GetMapping
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();

        model.addAttribute("members",members);

        return "members";
    }
}