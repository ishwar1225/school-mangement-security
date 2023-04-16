package com.ishwar.school.management.security.service;

import com.ishwar.school.management.security.entity.User;
import com.ishwar.school.management.security.exceptionhanler.Error;
import com.ishwar.school.management.security.model.Success;
import com.ishwar.school.management.security.model.UserAuthRequest;
import com.ishwar.school.management.security.repository.UserRepository;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ishwar.school.management.security.exceptionhanler.ErrorType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;


    public Either<Error, Optional<User>> getUser(String userId){
        return Try.of(()->userRepository.findById(userId)).toEither().
                peekLeft(error->log.error("Error :: ", error)).mapLeft(error-> new Error(NO_RECORD_FOUND, error));
    }

    public Either<Error, String> authentication(UserAuthRequest userAuthRequest){
       return  Try.of(()->{
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAuthRequest.userName(),
                   userAuthRequest.password()));
           var user = getUser(userAuthRequest.userName());
           if(user.isLeft())
               throw new Exception("User not found");
           return jwtService.generateToken(user.get().get());


        }).toEither().peekLeft(error->log.error("Error :: ", error)).mapLeft(error->new Error(INVALID_USER, error));
    }

    public Either<Error, Success> registerUser(User user){
       return  Try.of(()->{
           user.setPassword(passwordEncoder.encode(user.getPassword()));
               userRepository.save(user);
               return new Success();
       }).toEither().mapLeft(err->new Error(UNABLE_TO_CREATE_UPDATE, err));
    }

}
