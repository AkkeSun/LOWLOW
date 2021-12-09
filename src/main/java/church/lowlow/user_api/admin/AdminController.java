package church.lowlow.user_api.admin;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * index 이동 및 로그인 처리 컨트롤러
 */
@Controller
@RequestMapping("/admin")

public class AdminController {
    
    @GetMapping
    public String adminMainView(){
        return "admin/index";
    }
    
    @GetMapping("/login")
    public String adminLoginView(@RequestParam(required = false) String error,
                                 @RequestParam(required = false) String exception,
                                 Model model, Authentication auth){

        if(auth != null)
            return "redirect:/admin";

        if(exception != null){
            if(exception.contains("Invalid Username or Password"))
                exception = "아이디 혹은 비밀번호가 일치하지 않습니다";
        }

        model.addAttribute("error", error);
        model.addAttribute("exception",exception);

        return "admin/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {

        if(auth != null)
            new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/admin/login";
    }
    

    @ResponseBody
    @GetMapping("/denied")
    public String denied(){
        return "error";
    }

}
