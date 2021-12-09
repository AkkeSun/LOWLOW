package church.lowlow.user_api.admin;

import church.lowlow.rest_api.member.resource.MemberErrorsResource;
import church.lowlow.security.domain.dto.AccountDto;
import church.lowlow.security.domain.entity.Account;
import church.lowlow.security.domain.entity.Resources;
import church.lowlow.security.domain.entity.Role;
import church.lowlow.security.domain.validation.AccountValidation;
import church.lowlow.security.service.AccountService;
import church.lowlow.security.service.ResourcesService;
import church.lowlow.security.service.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@Log4j2
@RequestMapping("/admin/security")
@SessionAttributes({"roleList", "resourceList", "accountList"})
public class SecurityController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private HttpSession session;

    @Autowired
    private AccountValidation accountValidation;


    // ============ 시큐리티 인가 관련 전체 리스트 조회 ==========
    @GetMapping
    public String getList(Model model) {

        makeSessionList(model);

        return "admin/security/list";
    }


    // =================== ACCOUNT ===================
    @GetMapping("/account")
    public String accountCreateView(Model model) {

        makeSessionList(model);
        model.addAttribute("account", new AccountDto());

        return "admin/security/user/create";
    }


    @GetMapping("/account/{id}")
    public String accountDetail(@PathVariable("id") Long id, Model model) {

        makeSessionList(model);
        model.addAttribute("account", accountService.getUser(id));

        return "admin/security/user/detail";
    }


    @ResponseBody
    @PostMapping("/account")
    public String accountCreateProcess(@RequestBody AccountDto accountDto, SessionStatus sessionStatus, Errors errors)  {

        String returnMsg = "success";
        accountValidation.createValidate(accountDto, errors);

        if(errors.hasErrors()){

            errors.getAllErrors().forEach( e -> {
                log.info ("[ SECURITY USER INSERT ERROR ]");
                log.info ("[ERROR CODE] "+ e.getCode());
                log.info ("[ERROR MSG] " + e.getDefaultMessage());
            });
            returnMsg = errors.getAllErrors().get(0).getDefaultMessage();

        } else {

            accountService.createUser(accountDto);
            sessionStatus.setComplete();

        }
        return returnMsg;
    }



    @ResponseBody
    @PutMapping("/account/{id}")
    public String accountUpdateProcess(@RequestBody AccountDto accountDto, @PathVariable("id") Long id, SessionStatus sessionStatus, Errors errors)  {

        String returnMsg = "success";
        accountValidation.updateValidate(accountDto, errors);

        if(errors.hasErrors()){

            errors.getAllErrors().forEach( e -> {
                log.info ("[ SECURITY USER INSERT ERROR ]");
                log.info ("[ERROR CODE] "+ e.getCode());
                log.info ("[ERROR MSG] " + e.getDefaultMessage());
            });
            returnMsg = errors.getAllErrors().get(0).getDefaultMessage();

        } else {

            accountService.updateUser(id, accountDto);
            sessionStatus.setComplete();

        }
        return returnMsg;
    }

    @ResponseBody
    @DeleteMapping("/account/{id}")
    public String accountDeleteProcess(@PathVariable("id") Long id, SessionStatus sessionStatus)  {

        String returnMsg = "success";

        accountService.delete(id);
        sessionStatus.setComplete();

        return returnMsg;
    }






    //====================== ROLE ======================

    @GetMapping("/role/{id}")
    public String roles(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("role", roleService.getRole(id));
        return "admin/role/roleDetail";
    }

    @GetMapping("/role")
    public String createRoles(Model model) throws Exception {
        model.addAttribute("role", new Role());
        return "admin/role/roleCreate";
    }

    //====================== RESOURCES ======================

    @GetMapping("/resource")
    public String createResources(Model model) throws Exception {
        model.addAttribute("roleList", roleService.getRoles());
        model.addAttribute("res", new Resources());
        return "admin/resources/resourceCreate";
    }

    @GetMapping("/resource/{id}")
    public String resources(@PathVariable("id") Long id, Model model) throws Exception {
        model.addAttribute("roleList", roleService.getRoles());
        model.addAttribute("res", resourcesService.getResource(id));
        return "admin/resources/resourceDetail";
    }

    @GetMapping("/session")
    public void deleteSession() {

    }



    // ================= Session 이 존재하지 않을 때만 DB 에서 데이터를 가져온다 ==============
    public void makeSessionList (Model model) {

        if(session.getAttribute("roleList") == null)
            model.addAttribute("roleList", roleService.getRoles());
        else
            model.addAttribute("roleList", (List<Role>) session.getAttribute("roleList"));
        if(session.getAttribute("accountList") == null)
            model.addAttribute("accountList", accountService.findAll());
        else
            model.addAttribute("accountList", (List<Account>) session.getAttribute("accountList"));
        if(session.getAttribute("resourceList") == null)
            model.addAttribute("resourceList", resourcesService.getResources());
        else
            model.addAttribute("resourceList", (List<Resources>) session.getAttribute("resourceList"));

    }


}
