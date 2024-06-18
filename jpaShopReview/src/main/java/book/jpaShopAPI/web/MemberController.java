package book.jpaShopAPI.web;

import book.jpaShopAPI.domain.Address;
import book.jpaShopAPI.domain.Member;
import book.jpaShopAPI.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

// It can handle HTTP requests.
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // URL /members/new 에 GET 요청을 보낼 때 이 메서드가 실행됨
    // Model 객체는 컨트롤러와 뷰 사이에서 데이터를 전달하는 역할을 합니다.
    @GetMapping(value ="/members/new")
    public String createForm(Model model) {

        // Model 객체에 memberForm이라는 이름으로 새로운 MemberForm 객체를 추가합니다.
        // 이 데이터는 HTML 템플릿에서 사용할 수 있습니다. Form의 th:object="${memberForm}" 바인딩은 MemberForm 객체를 폼의 모델로 사용하게 합니다.
        model.addAttribute("memberForm", new MemberForm());

        // members/createForm.html을 반환하여 렌더링
        return "members/createMemberForm";
    }


    // 이 어노테이션은 이 메서드가 /members/new URL로 오는 POST 요청을 처리함
    @PostMapping(value ="/members/new")
    // @Valid: 객체의 유효성을 검증하는 데 사용됩니다. 이 어노테이션은 컨트롤러 메서드의 파라미터에 적용되어, 해당 객체가 유효한지 검증합니다.
    // BindingResult 객체는 유효성 검증의 결과를 담는 역할을 합니다. @Valid 어노테이션이 붙은 객체 바로 다음에 위치해야 하며, 검증 오류가 발생했을 때 관련된 정보를 제공합니다.
    public String create(@Valid MemberForm form, BindingResult result) {

        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(),form.getStreet(),form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        // 회원 가입이 완료되면 루트 페이지(홈 페이지)로 리다이렉트합니다.
        return "redirect:/";
    }

    // 회원 목록 컨트롤러
    @GetMapping(value = "/members")
    public String list(Model model){
        // Model 객체는 뷰와 컨트롤러 사이의 데이터 전달에 쓰인다.
        List<Member> members = memberService.findMembers();
        // Model에 members라는 이름으로 members 객체를 추가한다.
        model.addAttribute("members",members);
        return "members/memberList";
    }
}
