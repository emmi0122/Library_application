package se.yrgo.libraryapp.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UsernameTest {
    @ParameterizedTest
    @ValueSource(strings = {"Anna", "@Lena", ".Lennart", "Ulla_Britt", "Anna-Lena", "OLGA", "fiona", "123alma"})
    void correctUsername(String validName) {
        assertTrue(Username.validate(validName));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"Ola", "Håkan", "Märta", "Börje", "Laban😂", "<>#'+?=,^", "| \t\n\r ", " "})
    void incorrectUsername(String invalidName) {
        assertFalse(Username.validate(invalidName));
    }
}
