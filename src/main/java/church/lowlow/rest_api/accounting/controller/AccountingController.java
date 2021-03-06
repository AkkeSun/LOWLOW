package church.lowlow.rest_api.accounting.controller;

import church.lowlow.rest_api.accounting.db.*;
import church.lowlow.rest_api.accounting.repository.AccountingRepository;
import church.lowlow.rest_api.accounting.resource.AccountingErrorsResource;
import church.lowlow.rest_api.accounting.resource.AccountingResource;
import church.lowlow.rest_api.accounting.db.AccountingSearchValidation;
import church.lowlow.rest_api.common.aop.LogComponent;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.repository.MemberRepository;
import com.querydsl.core.Tuple;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
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
    private AccountingSearchValidation accountingSearchValidation;

    @Autowired
    private LogComponent logComponent;




    /*******************************************
     *                  CREATE API
     ********************************************/
    @PostMapping
    @ApiOperation(value = "?????? ?????? ??????", notes = "?????? ????????? ???????????????", response = Accounting.class)
    public ResponseEntity createAccounting(@RequestBody AccountingDto dto, Errors errors){

        // request Logging
        logComponent.accountingDtoLogging(dto);

        // check
        accountingValidation.validate(dto, errors);
        if (errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));

        // save
        Accounting accounting = modelMapper.map(dto, Accounting.class);
        if(dto.getMemberId() == -1)
            accounting.setMember(memberRepository.findByName("??????"));
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



    /*******************************************
     *                  READ API
     ********************************************/
    // ======================== paging data ========================
    @GetMapping
    @ApiOperation(value = "?????? ?????? ?????????", notes = "?????? ?????? ???????????? ???????????????", response = Accounting.class)
    public ResponseEntity getAccountingPage(SearchDto searchDto, PagingDto pagingDto, Errors errors,
                                            PagedResourcesAssembler<Accounting> assembler) {

        // request Logging
        logComponent.searchDtoLogging(searchDto);
        logComponent.pagingDtoLogging(pagingDto);

        // check
        accountingSearchValidation.dateValidate(searchDto, errors);
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));

        // load
        Page<Accounting> page = accountingRepository.getAccountingPage(searchDto, pagingDto);

        // return
        var pagedResources = assembler.toResource(page, e -> new AccountingResource(e));
        return ResponseEntity.ok(pagedResources);
    }


    // ======================== one data ========================
    @GetMapping("/{id}")
    @ApiOperation(value = "?????? ??????", notes = "??? ?????? ?????? ????????? ???????????????", response = Accounting.class)
    public ResponseEntity getAccounting(@PathVariable Integer id){

        Optional<Accounting> optional = accountingRepository.findById(id);
        Accounting accounting = optional.orElseThrow(ArithmeticException::new);

        AccountingResource resource = new AccountingResource(accounting);
        return ResponseEntity.ok(resource);
    }



    // ======================== ?????? ?????? ?????? ========================
    @GetMapping("/statistics")
    @ApiIgnore // swagger ??????
    public ResponseEntity getStatisticsMap(SearchDto searchDto, Errors errors) {

        // request Logging
        logComponent.searchDtoLogging(searchDto);

        // check
        accountingSearchValidation.dateValidate(searchDto, errors);
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));

        Map<String, Object> returnMap = getStatisticsMap(searchDto);

        return ResponseEntity.status(HttpStatus.OK).body(returnMap);
    }




    /*******************************************
     *                UPDATE API
     ********************************************/
    @PutMapping("/{id}")
    @ApiOperation(value = "?????? ?????? ??????", notes = "?????? ????????? ???????????????", response = Accounting.class)
    public ResponseEntity updateAccounting(@RequestBody AccountingDto dto,
                                           @PathVariable Integer id,
                                           Errors errors){

        // request Logging
        logComponent.accountingDtoLogging(dto);

        // check
        accountingValidation.validate(dto, errors);
        if(errors.hasErrors())
            return badRequest().body(new AccountingErrorsResource(errors));

        // update
        Accounting accounting = modelMapper.map(dto, Accounting.class);
        Member member = memberRepository.findById(dto.getMemberId()).get();
        accounting.setMember(member);
        accounting.setId(id);
        accounting.setWriter(getWriter());
        Accounting updateAccounting = accountingRepository.save(accounting);


        // return
        AccountingResource resource = new AccountingResource(updateAccounting);
        resource.add(linkTo(AccountingController.class).slash(updateAccounting.getId()).withRel("delete-Accounting"));

        return ResponseEntity.ok(resource);
    }



    /*******************************************
     *                 DELETE API
     ********************************************/
    @DeleteMapping("/{id}")
    @ApiOperation(value = "?????? ?????? ??????", notes = "?????? ????????? ???????????????", response = Accounting.class)
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




    /*******************************************
     *    ?????? ????????? ???????????? Map ?????? ???????????? ??????
     ********************************************/
    @ApiIgnore // swagger ??????
    public Map<String, Object> getStatisticsMap(SearchDto searchDto){

        // data load
        List<Tuple> offeringStatistics = accountingRepository.getAccountingStatistics(searchDto, "offeringKind"); // ?????? ????????? ??????
        List<Tuple> memberStatistics = accountingRepository.getAccountingStatistics(searchDto, "member");         // ????????? ??????


        // data List
        List<AccountingDto> offeringStatisticsList = new ArrayList<>();
        List<AccountingDto> memberStatisticsList = new ArrayList<>();


        // data convert (offeringStatistics)
        int allMoney = 0;
        for (Tuple tuple : offeringStatistics){
            AccountingDto dto = new AccountingDto();

            Object [] offeringData = tuple.toArray();
            for(Object data : offeringData){

                if(data instanceof OfferingKind)
                    dto.setOfferingKind( (OfferingKind) data);
                else {
                    allMoney += (Integer) data;
                    dto.setMoney( (Integer) data );
                }
            }
            offeringStatisticsList.add(dto);
        }
        offeringStatisticsList.add(AccountingDto.builder().offeringKind(OfferingKind.valueOf("TOTAL")).money(allMoney).build());


        // data convert (memberStatistics)
        allMoney = 0;
        for (Tuple tuple : memberStatistics){
            AccountingDto dto = new AccountingDto();

            Object [] memberData = tuple.toArray();
            for(Object data : memberData){

                if(data instanceof Member)
                    dto.setName( ((Member) data).getName() );
                else {
                    allMoney += (Integer) data;
                    dto.setMoney( (Integer) data );
                }
            }
            memberStatisticsList.add(dto);
        }
        memberStatisticsList.add(AccountingDto.builder().name("TOTAL").money(allMoney).build());



        // return
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("offeringKind", offeringStatisticsList);
        returnMap.put("member", memberStatisticsList);
        returnMap.put("_self", "/api/accounting/statistics");

        return returnMap;
    }
}
