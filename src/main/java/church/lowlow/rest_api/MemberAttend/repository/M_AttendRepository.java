package church.lowlow.rest_api.MemberAttend.repository;

import church.lowlow.rest_api.MemberAttend.db.MemberAttend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface M_AttendRepository extends JpaRepository<MemberAttend, Integer> {
}
