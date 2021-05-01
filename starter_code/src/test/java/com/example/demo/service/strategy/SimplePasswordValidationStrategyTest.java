package com.example.demo.service.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
public class SimplePasswordValidationStrategyTest {

    private PasswordValidationStrategy cut = new SimplePasswordValidationStrategy();

    @Test
    public void passwordWithSevenLettersIsValid() {
        assertTrue(cut.isValid("sevenps", "sevenps"));
    }

    @Test
    public void passwordLongerThanSixLettersAndWithTheSameCounterpartIsValid() {
        assertTrue(cut.isValid("validpassword", "validpassword"));
    }

    @Test
    public void passwordLongerThanSixLettersAndWithDifferentCounterpartIsValid() {
        assertFalse(cut.isValid("validpassword", "anothervalidpassword"));
    }

    @Test
    public void passwordShorterThanSevenLettersIsInvalid() {
        assertFalse(cut.isValid("sixsix", "sixsix"));
    }

    @ParameterizedTest
    @MethodSource("nullValues")
    public void nullPasswordIsInvalid(String password, String confirmPassword) {
        assertFalse(cut.isValid(password, confirmPassword));
    }

    private static List<Arguments> nullValues() {
        return List.of(
                Arguments.of(null, "confirmPassword"),
                Arguments.of("password", null),
                Arguments.of(null, null)
        );
    }
    
}