package church.lowlow.user_api.admin;

import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.security.domain.dto.AccountDto;
import church.lowlow.security.domain.dto.ResourcesDto;
import church.lowlow.security.domain.dto.RoleDto;
import church.lowlow.security.domain.entity.Account;
import church.lowlow.security.domain.entity.Resources;
import church.lowlow.security.domain.entity.Role;
import church.lowlow.security.domain.validation.AccountValidation;
import church.lowlow.security.domain.validation.ResourceValidation;
import church.lowlow.security.domain.validation.RoleValidation;
import church.lowlow.security.service.AccountService;
import church.lowlow.security.service.ResourcesService;
import church.lowlow.security.service.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@Log4j2
@RequestMapping("/admin/security")
@SessionAttributes({"roleList",     "rolePagingData", "roleListSub",
                    "resourceList", "resourcePagingData",
                    "accountList",  "accountPagingData", "nowTab"})
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

    @Autowired
    private RoleValidation roleValidation;

    @Autowired
    private ResourceValidation resourceValidation;


    /*********************************************
     *                   LIST VIEW
     *********************************************/
    @GetMapping
    public String getList(Model model, @RequestParam(required = false) String tab) {

        listInitialize(model);

        if(tab != null)
            model.addAttribute("nowTab", "tab"+tab);

        model.addAttribute("userFragment", "userList");
        model.addAttribute("roleFragment", "roleList");
        model.addAttribute("resourceFragment", "resourceList");

        return "admin/security/index";
    }




    /*********************************************
     *                  COMMON
     *********************************************/

    @ModelAttribute("nowTab")
    public String nowTab(String tab){
        return tab;
    }

    // ================ List 초기화 함수 ==============
    public void listInitialize (Model model) {
        if(session.getAttribute("accountList") == null){
            Page <Account> page = accountService.getAccountWithPage(0);
            model.addAttribute("accountList",  page.getContent());
            model.addAttribute("accountPagingData", new PagingDto(1, page.getTotalPages()));
        }
        else {
            model.addAttribute("accountList", (List<Account>) session.getAttribute("accountList"));
            model.addAttribute("accountPageData", (PagingDto) session.getAttribute("accountPagingData"));
        }
        if(session.getAttribute("roleList") == null) {
            Page <Role> page = roleService.getRoleWithPage(0);
            model.addAttribute("roleList", page.getContent());
            model.addAttribute("rolePagingData", new PagingDto(1, page.getTotalPages()));
        }
        else{
            model.addAttribute("roleList", (List<Account>) session.getAttribute("roleList"));
            model.addAttribute("rolePagingData", (PagingDto) session.getAttribute("rolePagingData"));
        }
        if(session.getAttribute("roleListSub") == null) {
            model.addAttribute("roleListSub", roleService.getRoleList());
        }
        else{
            model.addAttribute("roleListSub", (List<Account>) session.getAttribute("roleListSub"));
        }
        if(session.getAttribute("resourceList") == null) {
            Page<Resources> page = resourcesService.getResourceWithPage(0);
            model.addAttribute("resourceList", page.getContent());
            model.addAttribute("resourcePagingData", new PagingDto(1, page.getTotalPages()));
        }
        else{
            model.addAttribute("resourceList", (List<Resources>) session.getAttribute("resourceList"));
            model.addAttribute("resourcePagingData", (PagingDto) session.getAttribute("resourcePagingData"));
        }
    }


    // ============ 페이징 리로드 함수  ==========
    @GetMapping("/pagingReload")
    public String pagingReload(int num, String dbName, Model model) {
        String tab = "";
        switch(dbName){
            case "account" :
                Page <Account> accountPage = accountService.getAccountWithPage(num -1);
                model.addAttribute("accountList",  accountPage.getContent());
                model.addAttribute("accountPagingData", new PagingDto(num, accountPage.getTotalPages()));
                tab = "1";
                break;
            case "role" :
                Page <Role> rolePage = roleService.getRoleWithPage(num -1);
                model.addAttribute("roleList",  rolePage.getContent());
                model.addAttribute("rolePagingData", new PagingDto(num, rolePage.getTotalPages()));
                tab = "2";
                break;
            case "resource" :
                Page <Resources> resourcePage = resourcesService.getResourceWithPage(num -1);
                model.addAttribute("resourceList",  resourcePage.getContent());
                model.addAttribute("resourcePagingData", new PagingDto(num, resourcePage.getTotalPages()));
                tab = "3";
                break;
        }
        return "redirect:/admin/security?tab="+tab;
    }








    /*********************************************
     *                  ACCOUNT
     *********************************************/
    // ================ CREATE ==============
    @GetMapping("/account")
    public String accountCreateView(Model model) {

        model.addAttribute("account", new AccountDto());
        model.addAttribute("nowTab", "tab1");
        listInitialize(model);

        model.addAttribute("userFragment", "userCreate");
        model.addAttribute("roleFragment", "roleList");
        model.addAttribute("resourceFragment", "resourceList");

        return "admin/security/index";
    }

    @ResponseBody
    @PostMapping("/account")
    public String accountCreateProcess(@RequestBody AccountDto accountDto,
                                       SessionStatus sessionStatus, Errors errors)  {

        String returnMsg = "success";
        accountValidation.createValidate(accountDto, errors);

        if(errors.hasErrors()){
            returnMsg = errors.getAllErrors().get(0).getDefaultMessage();
            log.info ("[ SECURITY USER INSERT ERROR ]");
            log.info ("[ERROR CODE] "+ errors.getAllErrors().get(0).getCode());
            log.info ("[ERROR MSG] " + returnMsg);
        }
        else {
            accountService.createUser(accountDto);
            sessionStatus.setComplete();
        }
        return returnMsg;
    }

    // ================ UPDATE ==============
    @GetMapping("/account/{id}")
    public String accountDetailView(@PathVariable("id") Long id, Model model) {
        model.addAttribute("account", accountService.getUser(id));
        model.addAttribute("nowTab", "tab1");
        listInitialize(model);

        model.addAttribute("userFragment", "userDetail");
        model.addAttribute("roleFragment", "roleList");
        model.addAttribute("resourceFragment", "resourceList");

        return "admin/security/index";

    }

    @ResponseBody
    @PutMapping("/account/{id}")
    public String accountUpdateProcess(@RequestBody AccountDto accountDto,
                                       @PathVariable("id") Long id, Errors errors,
                                       SessionStatus sessionStatus){

        String returnMsg = "success";
        accountValidation.updateValidate(accountDto, errors);

        if(errors.hasErrors()){
            returnMsg = errors.getAllErrors().get(0).getDefaultMessage();
            log.info ("[ SECURITY USER INSERT ERROR ]");
            log.info ("[ERROR CODE] "+ errors.getAllErrors().get(0).getCode());
            log.info ("[ERROR MSG] " + returnMsg);
        }
        else {
            accountService.updateUser(id, accountDto);
            sessionStatus.setComplete();
        }
        return returnMsg;
    }

    // ================ DELETE ==============
    @ResponseBody
    @DeleteMapping("/account/{id}")
    public String accountDeleteProcess(@PathVariable("id") Long id, SessionStatus sessionStatus)  {

        String returnMsg = "success";
        accountService.delete(id);
        sessionStatus.setComplete();

        return returnMsg;
    }







    /*********************************************
     *                    ROLE
     *********************************************/
    // ================ CREATE ==============
    @GetMapping("/role")
    public String roleCreateView(Model model) {

        model.addAttribute("role", new RoleDto());
        model.addAttribute("nowTab", "tab2");

        listInitialize(model);

        model.addAttribute("userFragment", "userList");
        model.addAttribute("roleFragment", "roleCreate");
        model.addAttribute("resourceFragment", "resourceList");

        return "admin/security/index";
    }
    @ResponseBody
    @PostMapping("/role")
    public String roleCreateProcess(@RequestBody RoleDto roleDto,
                                    SessionStatus sessionStatus, Errors errors, Model model)  {

        String returnMsg = "success";
        roleValidation.createValidate(roleDto, errors);

        if(errors.hasErrors()){
            returnMsg = errors.getAllErrors().get(0).getDefaultMessage();
            log.info ("[ SECURITY USER INSERT ERROR ]");
            log.info ("[ERROR CODE] "+ errors.getAllErrors().get(0).getCode());
            log.info ("[ERROR MSG] " + returnMsg);
        }
        else {
            roleService.createRole(roleDto);
            sessionStatus.setComplete();
            model.addAttribute("nowTab", "tab2");
        }
        return returnMsg;
    }

    // ================ UPDATE ==============
    @GetMapping("/role/{id}")
    public String roleDetailView(@PathVariable("id") Long id, Model model) {

        model.addAttribute("nowTab", "tab2");
        model.addAttribute("role", roleService.getRole(id));
        listInitialize(model);

        model.addAttribute("userFragment", "userList");
        model.addAttribute("roleFragment", "roleDetail");
        model.addAttribute("resourceFragment", "resourceList");

        return "admin/security/index";
    }

    @ResponseBody
    @PutMapping("/role/{id}")
    public String roleUpdateProcess(@RequestBody RoleDto roleDto,
                                    @PathVariable("id") Long id, Errors errors,
                                    SessionStatus sessionStatus){

        String returnMsg = "success";
        roleValidation.updateValidate(roleDto, id, errors);

        if(errors.hasErrors()){
            returnMsg = errors.getAllErrors().get(0).getDefaultMessage();
            log.info ("[ SECURITY USER INSERT ERROR ]");
            log.info ("[ERROR CODE] "+ errors.getAllErrors().get(0).getCode());
            log.info ("[ERROR MSG] " + returnMsg);
        }
        else {
            roleService.updateRole(id, roleDto);
            sessionStatus.setComplete();
        }
        return returnMsg;
    }

    // ================ DELETE ==============
    @ResponseBody
    @DeleteMapping("/role/{id}")
    public String roleDeleteProcess(@PathVariable("id") Long id, SessionStatus sessionStatus)  {

        String returnMsg = "success";
        roleService.delete(id);
        sessionStatus.setComplete();

        return returnMsg;
    }







    /*********************************************
     *                  ACCOUNT
     *********************************************/
    // ================ CREATE ==============
    @GetMapping("/resource")
    public String createResources(Model model) {

        model.addAttribute("nowTab", "tab3");
        model.addAttribute("res", new Resources());
        listInitialize(model);

        model.addAttribute("userFragment", "userList");
        model.addAttribute("roleFragment", "roleList");
        model.addAttribute("resourceFragment", "resourceCreate");

        return "admin/security/index";
    }

    @ResponseBody
    @PostMapping("/resource")
    public String resourceCreateProcess(@RequestBody ResourcesDto resourcesDto,
                                       SessionStatus sessionStatus, Errors errors, Model model)  {

        String returnMsg = "success";
        resourceValidation.createValidate(resourcesDto, errors);

        if(errors.hasErrors()){
            returnMsg = errors.getAllErrors().get(0).getDefaultMessage();
            log.info ("[ SECURITY USER INSERT ERROR ]");
            log.info ("[ERROR CODE] "+ errors.getAllErrors().get(0).getCode());
            log.info ("[ERROR MSG] " + returnMsg);
        }
        else {
            resourcesService.createResource(resourcesDto);
            sessionStatus.setComplete();
            model.addAttribute("nowTab", "tab3");
        }
        return returnMsg;
    }

    // ================ UPDATE ==============
    @GetMapping("/resource/{id}")
    public String resourceDetailView(@PathVariable("id") Long id, Model model) {

        model.addAttribute("nowTab", "tab3");
        model.addAttribute("res", resourcesService.getResource(id));
        listInitialize(model);

        model.addAttribute("userFragment", "userList");
        model.addAttribute("roleFragment", "roleList");
        model.addAttribute("resourceFragment", "resourceDetail");

        return "admin/security/index";
    }

    @ResponseBody
    @PutMapping("/resource/{id}")
    public String resourceUpdateProcess(@RequestBody ResourcesDto resourcesDto,
                                        @PathVariable("id") Long id, Errors errors,
                                        SessionStatus sessionStatus){

        String returnMsg = "success";
        resourceValidation.updateValidate(resourcesDto, id, errors);

        if(errors.hasErrors()){
            returnMsg = errors.getAllErrors().get(0).getDefaultMessage();
            log.info ("[ SECURITY USER INSERT ERROR ]");
            log.info ("[ERROR CODE] "+ errors.getAllErrors().get(0).getCode());
            log.info ("[ERROR MSG] " + returnMsg);
        }
        else {
            resourcesService.updateResources(id, resourcesDto);
            sessionStatus.setComplete();
        }
        return returnMsg;
    }

    // ================ DELETE ==============
    @ResponseBody
    @DeleteMapping("/resource/{id}")
    public String resourceDeleteProcess(@PathVariable("id") Long id, SessionStatus sessionStatus)  {

        String returnMsg = "success";
        resourcesService.delete(id);
        sessionStatus.setComplete();

        return returnMsg;
    }
}
