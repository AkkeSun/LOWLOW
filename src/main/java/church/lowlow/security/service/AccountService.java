package church.lowlow.security.service;

import church.lowlow.security.domain.dto.AccountDto;
import church.lowlow.security.domain.entity.Account;
import church.lowlow.security.domain.entity.Role;
import church.lowlow.security.repository.AccountRepo;
import church.lowlow.security.repository.RoleRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

/**
 * UserDetailsService
 * DB에 저장된 유저 정보로 시큐리티 인가심사를 하기위한 인터페이스
 */
@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // ======================= UserDetailsService @Override ========================
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepo.findByUsername(username);
        if(account == null)
            throw new UsernameNotFoundException("No user found with username: " + username);

        return new User(account.getUsername(), account.getPassword(), authorities(account.getUserRole()));
    }

    // Role 설정 : 이름만 추출해서 Set으로 저장
    private Collection<? extends GrantedAuthority> authorities(Role role) {
        Set<SimpleGrantedAuthority> roleSet = new HashSet();
        roleSet.add(new SimpleGrantedAuthority(role.getRoleName()));
        return roleSet;
    }

    // ==============================================================================


    @Transactional
    public void createUser(AccountDto dto){
        Account account = modelMapper.map(dto, Account.class);
        account.setUserRole(roleRepo.findByRoleName(dto.getRole()));
        accountRepo.save(account);
    };

    @Transactional
    public void updateUser(Long id, AccountDto dto){

        // 가입 여부 확인
        Optional<Account> optional = accountRepo.findById(id);
        optional.orElseThrow(NullPointerException::new);

        // password setting
        if(dto.getPassword().equals(""))
            dto.setPassword(dto.getPassword());
        else
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        // update
        Account account = modelMapper.map(dto, Account.class);
        account.setId(id);
        account.setUserRole(roleRepo.findByRoleName(dto.getRole()));
        accountRepo.save(account);
    };

    @Transactional
    public List<Account> findAll(){
        return accountRepo.findAll();
    };

    @Transactional
    public Account getUser(Long id){
        Optional<Account> optional = accountRepo.findById(id);
        return optional.orElseThrow(NullPointerException::new);
    };
}
