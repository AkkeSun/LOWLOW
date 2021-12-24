package church.lowlow.rest_api.accounting.controller;

import church.lowlow.rest_api.accounting.db.*;
import church.lowlow.rest_api.accounting.repository.AccountingRepository;
import church.lowlow.rest_api.accounting.resource.AccountingErrorsResource;
import church.lowlow.rest_api.accounting.resource.AccountingResource;
import church.lowlow.rest_api.accounting.searchDsl.AccountingSearchValidation;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.repository.MemberRepository;
import com.querydsl.core.Tuple;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.text.ParseException;
import java.util.*;

import static church.lowlow.rest_api.common.util.WriterUtil.getWriter;
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
    private AccountingValidation accountingValidation;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AccountingSearchValidation serchBoxValidation;


    /**
     * CREATE API
     */
    @PostMapping
    public ResponseEntity createAccounting(@RequestBody AccountingDto dto,
                                           Errors errors){

        // check
        accountingValidation.validate(dto, errors);
        if (errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));

        // save
        Accounting accounting = modelMapper.map(dto, Accounting.class);
        if(dto.getMemberId() == -1)
            accounting.setMember(null);
        else
            accounting.setMember(memberRepository.findById(dto.getMemberId()).get());
        accounting.setWriter(getWriter());

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
    public ResponseEntity getAccountingPage(SearchDto searchDto, PagingDto pagingDto, Errors errors,
                                            PagedResourcesAssembler<Accounting> assembler) throws ParseException {

        // check
        serchBoxValidation.dateValidate(searchDto, errors);
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));


        // -------------- 헌금 종류별 카운트 함수 ---------------
        List<Tuple> offeringMoneyCount = accountingRepository.getOfferingMoneyCount(searchDto);
        List<AccountingDto> list = new ArrayList<>();
        int allCount = 0;

        for (Tuple tuple : offeringMoneyCount){
            AccountingDto dto = new AccountingDto();

            Object [] offeringData = tuple.toArray();
            for(Object data : offeringData){

                if(data instanceof OfferingKind)
                    dto.setOfferingKind( (OfferingKind) data);
                else {
                    allCount += (Integer) data;
                    dto.setMoney( (Integer) data );
                }
            }

            list.add(dto);
        }

        list.add(AccountingDto.builder().offeringKind(OfferingKind.valueOf("TOTAL")).money(allCount).build());
        System.out.println(list);

        // -------------- 헌금 종류별 카운트 함수 ---------------





        // load
        Page<Accounting> page = accountingRepository.getAccountingPage(searchDto, pagingDto);


        // return
        var pagedResources = assembler.toResource(page, e -> new AccountingResource(e));
        return ResponseEntity.ok(pagedResources);
    }



    @GetMapping("{id}")
    public ResponseEntity getAccounting(@PathVariable Integer id){
        Optional<Accounting> optional = accountingRepository.findById(id);
        Accounting accounting = optional.orElseThrow(ArithmeticException::new);

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

        // 유효성 검사
        Member member = memberRepository.findById(dto.getMemberId()).get();
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
    public StatisticsWithOfferingKind getStatisticsWithOfferingKind (List<Accounting> list) {

        int TOTAL        = 0;
        int SUNDAY       = 0;
        int TITHE        = 0;
        int THANKSGIVING = 0;
        int BUILDING     = 0;
        int SPECIAL      = 0;
        int MISSION      = 0;
        int UNKNOWN      = 0;

        for(Accounting accounting : list){

            switch (accounting.getOfferingKind()) {
                case SUNDAY      : SUNDAY       += accounting.getMoney(); break;
                case TITHE       : TITHE        += accounting.getMoney(); break;
                case THANKSGIVING: THANKSGIVING += accounting.getMoney(); break;
                case BUILDING    : BUILDING     += accounting.getMoney(); break;
                case SPECIAL     : SPECIAL      += accounting.getMoney(); break;
                case MISSION     : MISSION      += accounting.getMoney(); break;
                case UNKNOWN     : UNKNOWN      += accounting.getMoney(); break;
            }
        }

        TOTAL = SUNDAY + TITHE + THANKSGIVING + BUILDING + SPECIAL + MISSION + UNKNOWN;

        return StatisticsWithOfferingKind.builder().SUNDAY(SUNDAY).TITHE(TITHE).THANKSGIVING(THANKSGIVING).BUILDING(BUILDING)
                .SPECIAL(SPECIAL).MISSION(MISSION).UNKNOWN(UNKNOWN).TOTAL(TOTAL).build();
    }

    public List<Map<String, Integer>> getStatisticsWithName (List<Accounting> list) {

        List<Map<String, Integer>> returnList = new ArrayList<>();

        for(Accounting accounting : list){

            Map<String, Integer> nameAndMoney = new HashMap<>();
            Member member = accounting.getMember();


        }


        return null;
    }

}
