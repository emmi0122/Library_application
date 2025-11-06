package se.yrgo.libraryapp.validators;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RealNameTest {
    @ParameterizedTest
    @ValueSource(strings = {"balderdash", "blimey", "bullspit", "damn", "darn", "drat", "frack", "frick", "heck", "shiet"})
    void checkBadWords(String badWord) {
        assertTrue(RealName.validate(badWord));
    }
}

// Skriv ett eller flera tester som kontrollerar att alla orden i txt läses in korrekt med hjälp av validate()
// Fundera på om det behövs ytterligare tester för validate()
// Vi ska inte hitta fel i Utils, utan i validate()

// This validator checks that the real names match our high standard for proper names. I.e. no bad
// words.

// Metod validate():
// Validates if the given name is a valid and proper name.