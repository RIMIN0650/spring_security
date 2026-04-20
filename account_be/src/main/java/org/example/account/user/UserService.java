package org.example.account.user;

import lombok.RequiredArgsConstructor;
import org.example.account.user.model.AuthUserDetails;
import org.example.account.user.model.User;
import org.example.account.user.model.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public void signup(UserDto.SignupReq dto) {
        User user = dto.toEntity();
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }




    // 5번 실행
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserService 실행됨");
        
        // 6번
        User user = userRepository.findByEmail(username).orElseThrow();

        // 7번
        return AuthUserDetails.from(user);
    }
}
