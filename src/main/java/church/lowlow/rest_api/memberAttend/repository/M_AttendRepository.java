package church.lowlow.rest_api.memberAttend.repository;

import church.lowlow.rest_api.member.db.Member;
import church.lowlow.rest_api.memberAttend.db.MemberAttend;
import church.lowlow.rest_api.memberAttend.queryDsl.M_AttendDsl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface M_AttendRepository extends JpaRepository<MemberAttend, Integer>, M_AttendDsl {


    Optional<MemberAttend> findByMemberAndCheckDate(Member member, LocalDate checkDate);
}
