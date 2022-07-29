package church.lowlow.rest_api.member.service;

import church.lowlow.jwt.entity.ErrorDto;
import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.common.fileProcess.service.CommonFileService;
import church.lowlow.rest_api.member.controller.MemberController;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.MemberDto;
import church.lowlow.rest_api.member.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.ResponseEntity.badRequest;

@Service
public class DefaultMemberService implements MemberService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommonFileService fileService;


    @Override
    @Transactional
    public Member createMember(MemberDto dto, Errors errors) {
        Member member = modelMapper.map(dto, Member.class);
        return repository.save(member);
    }


    @Override
    @Transactional
    public Page<Member> getMemberPage(SearchDto searchDto, PagingDto pagingDto, String is_MAttend) {
        return repository.getMemberPage(searchDto, pagingDto, is_MAttend);
    }


    @Override
    @Transactional
    public Object getMember(Integer id) {

        // check
        Optional<Member> optional = repository.findById(id);
        if(optional.isEmpty())
        {
            Link link = linkTo(MemberController.class).slash(id).withSelfRel();
            ErrorDto dto = ErrorDto.builder().errCode("getMemberErr").errMsg("성도를 찾을 수 없습니다").build();
            return badRequest().body(new Resource(dto, link));
        }

        return optional.get();
    }


    @Override
    @Transactional
    public Object updateMember(Integer id, MemberDto dto) {

        // check
        Optional<Member> optional = repository.findById(id);
        if(optional.isEmpty())
        {
            Link link = linkTo(MemberController.class).slash(id).withSelfRel();
            ErrorDto errDto = ErrorDto.builder().errCode("updateMemberErr").errMsg("성도를 찾을 수 없습니다").build();
            return badRequest().body(new Resource(errDto, link));
        }

        Member member = optional.get();
        Member updateMember = modelMapper.map(dto, Member.class);
        updateMember.setId(id);

        fileUpdate(member.getImage(), updateMember.getImage());
        return repository.save(member);
    }

    @Override
    @Transactional
    public Object deleteMember(Integer id) {

        // check
        Optional<Member> optional = repository.findById(id);
        if(optional.isEmpty())
        {
            Link link = linkTo(MemberController.class).slash(id).withSelfRel();
            ErrorDto dto = ErrorDto.builder().errCode("deleteMemberErr").errMsg("성도를 찾을 수 없습니다").build();
            return badRequest().body(new Resource(dto, link));
        }

        // delete
        Member member = optional.get();
        member.setBlock(true);

        // 이미지 삭제
        if(member.getImage() != null)
            fileService.deleteFile(member.getImage().getUploadName(), "member");
        return repository.save(member);
    }


    // 파일 업데이트 시 파일이 변경되면 이전 파일을 삭제한다
    private void fileUpdate(FileDto preFile, FileDto postFile) {

        if(preFile != null)
        {
            if(postFile != null)
                fileService.deleteFile(preFile.getUploadName(), "member");
            else
            {
                if(!postFile.getUploadName().equals(preFile.getUploadName()))
                    fileService.deleteFile(preFile.getUploadName(), "member");
            }
        }
    }
}
