package org.example.account.user;

import lombok.RequiredArgsConstructor;
import org.example.account.user.model.AuthUserDetails;
import org.example.account.user.model.UserDto;
import org.example.account.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserDto.SignupReq dto) {
        userService.signup(dto);
        return ResponseEntity.ok("성공");
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserDto.LoginReq dto) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword(), null);

        Authentication authentication = authenticationManager.authenticate(token);
        AuthUserDetails user = (AuthUserDetails) authentication.getPrincipal();

        // 로그인 성공 처리
        if (user != null) {
            String jwt = JwtUtil.createToken(user.getIdx(), user.getUsername(), user.getRole());
            return ResponseEntity.ok().header("Set-Cookie", "ATOKEN=" + jwt + "; Path=/").build();
        }

        return ResponseEntity.ok("로그인 실패");

    }

//    @PostMapping("/login")
//    public ResponseEntity login(@RequestBody UserDto.LoginReq dto) {
//        System.out.println("로그인 시도 이메일: " + dto.getEmail()); // 1번 확인
//
//        try {
//            UsernamePasswordAuthenticationToken token =
//                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword(), null);
//
//            Authentication authentication = authenticationManager.authenticate(token);
//            System.out.println("인증 객체 생성 완료: " + authentication.getName()); // 2번 확인
//
//            AuthUserDetails user = (AuthUserDetails) authentication.getPrincipal();
//            System.out.println("사용자 정보 로드 완료: " + user.getUsername()); // 3번 확인
//
//            if (user != null) {
//                String jwt = JwtUtil.createToken(user.getIdx(), user.getUsername(), user.getRole());
//                return ResponseEntity.ok().header("Set-Cookie", "ATOKEN=" + jwt + "; Path=/").build();
//            }
//        } catch (Exception e) {
//            System.out.println("인증 중 에러 발생: " + e.getMessage()); // 에러 내용 확인
//        }
//
//        return ResponseEntity.ok("로그인 실패");
//    }

}
