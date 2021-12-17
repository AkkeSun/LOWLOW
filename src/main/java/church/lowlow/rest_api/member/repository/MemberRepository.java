package church.lowlow.rest_api.member.repository;

import church.lowlow.rest_api.member.db.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findByName(String name);
    Member findByNameAndPhoneNumber(String name, String phoneNumber);
}
