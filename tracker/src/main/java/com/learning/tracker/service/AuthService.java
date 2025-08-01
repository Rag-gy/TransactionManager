package com.learning.tracker.service;

import com.learning.tracker.config.properties.JwtProperties;
import com.learning.tracker.dto.auth.AuthResponseDTO;
import com.learning.tracker.dto.auth.LoginRequestDTO;
import com.learning.tracker.utils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProperties jwtProperties;

//    @Transactional
//    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
//        log.debug("User tried to login: {}", loginRequest.emailAddress());
//
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.emailAddress(),
//                            loginRequest.password()
//                    )
//            );
//
//        }
//    }
//    TODO: NEED TO FINISH THE AUTH SERVICE COMPLETELY
//     AND ALSO TO REWRITE VALIDATE AUTH TOKEN LOGIC SUCH THAT THE USER IS CHECKED IF ARCHIVED OR PRESENT IN THE DATABASE
//     AND STORE THE USER ID IN THE REDIS WITH ARCHIVED BLACKLIST AND WITH A TTL OF 2 DAYS.
//     AND THEN CHECK AND REMOVE THE REDIS KEY IF A USER IS SUCCESSFULLY UNARCHIVED.
}
