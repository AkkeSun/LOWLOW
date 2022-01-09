package church.lowlow.rest_api.member.repository;

import church.lowlow.rest_api.member.queryDsl.MemberDsl;
import church.lowlow.rest_api.member.db.ChurchOfficer;
import church.lowlow.rest_api.member.db.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Integer>, MemberDsl {

    Member findByName(String name);
    Member findByNameAndPhoneNumber(String name, String phoneNumber);
    Page<Member> findAll(Pageable pageable);
}
