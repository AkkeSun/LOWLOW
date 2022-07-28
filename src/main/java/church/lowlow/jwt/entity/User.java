package church.lowlow.jwt.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
/**
 * [ 관리자 정보 ]
 *
 * @Column
 * id 고유 식별자
 * email 이메일
 * password 비밀번호
 * refreshToken 리프레시 토큰
 * roles 권한 ( DEV > ADMIN > UPLOADER )
 *
 * UPLOADER : calendar, gallery, notice, weekly, worshipVideo
 * ADMIN : accounting, basicInfo, member, memberAttend
 * DEV : security
 *
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
@ToString @EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    private String refreshToken;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    // Role 설정 셋팅
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    // Security username 설정
    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
