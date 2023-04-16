package com.ishwar.school.management.security.exceptionhanler;

import com.ishwar.school.management.security.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
public class Error {
    private final ErrorType errorType;
    private final Optional<Throwable> throwable;
    private final Optional<String> errorMessage;


    public Error(ErrorType errorType, Throwable throwable) {
        this.errorType = errorType;
        this.throwable = Optional.of(throwable);
        this.errorMessage = Optional.ofNullable(throwable.getMessage());
    }

    public Error(ErrorType errorType) {
        this.errorType = errorType;
        this.throwable = Optional.empty();
        this.errorMessage = Optional.empty();
    }
}
