package org.example.account.user.model;

import lombok.Builder;
import lombok.Getter;
import org.example.account.config.AuthenticationManagerConfig;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;

public class UserDto {

    @Getter
    public static class SignupReq {
        private String email;
        private String name;
        private String password;

        public User toEntity() {
            return User.builder()
                    .email(this.email)
                    .name(this.name)
                    .password(this.password)
                    .build();
        }
    }

    @Getter
    public static class LoginReq {
        private String email;
        private String password;
    }

    @Builder
    @Getter
    public static class LoginRes {
        private Long idx;
        private String email;
        private String name;

        public static LoginRes from(User entity) {
            return LoginRes.builder()
                    .idx(entity.getIdx())
                    .email(entity.getEmail())
                    .name(entity.getName())
                    .build();
        }
    }

}