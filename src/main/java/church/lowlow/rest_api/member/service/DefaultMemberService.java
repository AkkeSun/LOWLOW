package church.lowlow.rest_api.member.service;

import church.lowlow.rest_api.common.entity.FileDto;
import church.lowlow.rest_api.common.entity.PagingDto;
import church.lowlow.rest_api.common.entity.SearchDto;
import church.lowlow.rest_api.common.fileProcess.service.CommonFileService;
import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.member.db.MemberDto;
import church.lowlow.rest_api.member.db.MemberValidation;
import church.lowlow.rest_api.member.repository.MemberRepository;
import church.lowlow.rest_api.member.resource.MemberErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.badRequest;

@Service
public class DefaultMemberService implements MemberService {

    @Autowired
    private MemberRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CommonFileService fileService;

    @Autowired
    private MemberValidation validation;

    @Override
    @Transactional
    public Object createMember(MemberDto dto, Errors errors) {
        validation.createValidate(dto, errors);

        if(errors.hasErrors())
            return badRequest().body(new MemberErrorsResource(errors));

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
    public Member getMember(Integer id) {
        Optional<Member> optional = repository.findById(id);
        return optional.orElseThrow(ArithmeticException::new);
    }


    @Override
    @Transactional
    public Member updateMember(Integer id, MemberDto dto, Errors errors) {

        Member member = repository.findById(id).orElseThrow(ArithmeticException::new);
        Member updateMember = modelMapper.map(dto, Member.class);
        updateMember.setId(id);

        fileUpdate(member.getImage(), updateMember.getImage());
        return repository.save(member);
    }

    @Override
    @Transactional
    public Object deleteMember(Integer id) {

        Optional<Member> optional = repository.findById(id);
        if(optional.isEmpty())
            return ResponseEntity.notFound().build();

        Member member = optional.get();
        member.setBlock(true);

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
