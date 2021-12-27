package church.lowlow.rest_api.member.repository;

import church.lowlow.rest_api.member.queryDsl.MemberDsl;
import church.lowlow.rest_api.member.db.ChurchOfficer;
import church.lowlow.rest_api.member.db.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer>, MemberDsl {

    Member findByName(String name);
    Member findByNameAndPhoneNumber(String name, String phoneNumber);

    Page<Member> findAllByBelong(String belong, Pageable pageable);
    Page<Member> findAllByChurchOfficer(ChurchOfficer churchOfficer, Pageable pageable);
    Page<Member> findAllByName(String name, Pageable pageable);
    Page<Member> findAll(Pageable pageable);

}
