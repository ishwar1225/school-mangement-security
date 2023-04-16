package com.ishwar.school.management.security.controller;

import com.ishwar.school.management.security.entity.User;
import com.ishwar.school.management.security.exceptionhanler.Error;
import com.ishwar.school.management.security.exceptionhanler.ErrorResponse;
import com.ishwar.school.management.security.model.UserAuthRequest;
import com.ishwar.school.management.security.service.UserService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ishwar.school.management.security.exceptionhanler.ErrorType.INTERNAL_ERROR;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user-auth")
public class UserAuthController {

    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody UserAuthRequest userAuthRequest){

        return Try.of(()->userService.authentication(userAuthRequest)).toEither().
                peekLeft(err->log.error("Unable to authenticate",err)).
                mapLeft(error-> new Error(INTERNAL_ERROR)).flatMap(success->success).fold(ErrorResponse::resposeError, response->ResponseEntity.ok((response)));
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        return Try.of(()->userService.registerUser(user)).toEither().
                peekLeft(err->log.error("Unable to authenticate",err)).
                mapLeft(error-> new Error(INTERNAL_ERROR)).flatMap(success->success).fold(ErrorResponse::resposeError, response->ResponseEntity.ok((response)));
    }
}
