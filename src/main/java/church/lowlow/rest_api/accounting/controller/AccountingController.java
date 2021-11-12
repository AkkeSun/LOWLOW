package church.lowlow.rest_api.accounting.controller;

import church.lowlow.rest_api.accounting.db.Accounting;
import church.lowlow.rest_api.accounting.db.AccountingDto;
import church.lowlow.rest_api.accounting.db.AccountingValidation;
import church.lowlow.rest_api.accounting.repository.AccountingRepository;
import church.lowlow.rest_api.accounting.resource.AccountingErrorsResource;
import church.lowlow.rest_api.accounting.resource.AccountingResource;
import church.lowlow.rest_api.accounting.searchBox.MoneyBox;
import church.lowlow.rest_api.accounting.searchBox.SearchBox;
import church.lowlow.rest_api.accounting.searchBox.SerchBoxValidation;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping(value = "/api/accounting", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class AccountingController {

    @Autowired
    private AccountingRepository accountingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    AccountingValidation accountingValidation;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SerchBoxValidation serchBoxValidation;


    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createAccounting(@RequestBody @Valid AccountingDto dto,
                                           Errors errors){

        // check
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));
        accountingValidation.validate(dto, errors);
        if (errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));

        // save
        Accounting accounting = modelMapper.map(dto, Accounting.class);
        Member findMember = memberRepository.findByName(dto.getName());
        accounting.setMember(findMember);
        Accounting newAccounting = accountingRepository.save(accounting);
        URI createdUri = linkTo(AccountingController.class).slash(newAccounting.getId()).toUri();

        // return
        AccountingResource resource = new AccountingResource(newAccounting);
        resource.add(linkTo(AccountingController.class).slash(newAccounting.getId()).withRel("update-accounting"));
        resource.add(linkTo(AccountingController.class).slash(newAccounting.getId()).withRel("delete-accounting"));

        return ResponseEntity.created(createdUri).body(resource);
    }



    /**
     * READ API
     */
    @GetMapping
    public ResponseEntity getAccounting(@RequestBody SearchBox searchBox, Errors errors,
                                        Pageable pageable,
                                        PagedResourcesAssembler<Accounting> assembler) throws ParseException {

        // searchBox 검증
        if(searchBox != null ){
            serchBoxValidation.validate(searchBox, errors);
            if(errors.hasErrors()) return badRequest().body(new AccountingErrorsResource(errors));
        }

        // 데이터 로드
        Page<Accounting> page = accountingRepository.searchBox(searchBox, pageable);
        
        // 종류별 금액 출력하는 함수 사용하기
        // 로그인 유무 체크 후 로그인 했으면 update, delete url 넣어주기
        var pagedResources = assembler.toResource(page, e -> new AccountingResource(e));
        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("{id}")
    public ResponseEntity getAccounting(@PathVariable Integer id){
        Optional<Accounting> optional = accountingRepository.findById(id);
        Accounting accounting = optional.orElseThrow(ArithmeticException::new);

        // 로그인 유무 체크 후 로그인 했으면 update, delete url 넣어주기
        AccountingResource resource = new AccountingResource(accounting);
        return ResponseEntity.ok(resource);
    }




    /**
     * UPDATE API
     */
    @PutMapping("/{id}")
    public ResponseEntity updateAccounting(@RequestBody @Valid AccountingDto dto,
                                           @PathVariable Integer id,
                                           Errors errors){

        // check
        Optional<Accounting> optional = accountingRepository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));
        accountingValidation.validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));

        // update
        Accounting accounting = modelMapper.map(dto, Accounting.class);
        Member member = memberRepository.findByName(dto.getName());
        accounting.setMember(member);
        accounting.setId(id);
        Accounting updateAccounting = accountingRepository.save(accounting);


        // return
        AccountingResource resource = new AccountingResource(updateAccounting);
        resource.add(linkTo(AccountingController.class).slash(updateAccounting.getId()).withRel("delete-Accounting"));

        return ResponseEntity.ok(resource);
    }



    /**
     * DELETE API
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteWorshipVideo(@PathVariable Integer id, Resource resource){

        // check
        Optional<Accounting> optional = accountingRepository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        // delete
        accountingRepository.deleteById(id);

        // return
        resource.add(linkTo(AccountingController.class).withRel("index"));
        return ResponseEntity.ok(resource);
    }


    /**
     * 헌금 종류별로 금액을 카운트하는 함수
     */
    public MoneyBox moneyBox(List<Accounting> list){

        int TOTAL        = 0;
        int SUNDAY       = 0;
        int TITHE        = 0;
        int THANKSGIVING = 0;
        int BUILDING     = 0;
        int SPECIAL      = 0;
        int MISSION      = 0;
        int UNKNOWN      = 0;

        for(int i=0; i<list.size(); i++){

            switch (list.get(i).getOfferingKind()) {
                case SUNDAY      : SUNDAY       += list.get(i).getMoney(); break;
                case TITHE       : TITHE        += list.get(i).getMoney(); break;
                case THANKSGIVING: THANKSGIVING += list.get(i).getMoney(); break;
                case BUILDING    : BUILDING     += list.get(i).getMoney(); break;
                case SPECIAL     : SPECIAL      += list.get(i).getMoney(); break;
                case MISSION     : MISSION      += list.get(i).getMoney(); break;
                case UNKNOWN     : UNKNOWN      += list.get(i).getMoney(); break;
            }
        }

        TOTAL = SUNDAY + TITHE + THANKSGIVING + BUILDING + SPECIAL + MISSION + UNKNOWN;

        return MoneyBox.builder().SUNDAY(SUNDAY).TITHE(TITHE).THANKSGIVING(THANKSGIVING).BUILDING(BUILDING)
                .SPECIAL(SPECIAL).MISSION(MISSION).UNKNOWN(UNKNOWN).TOTAL(TOTAL).build();
    }


}
