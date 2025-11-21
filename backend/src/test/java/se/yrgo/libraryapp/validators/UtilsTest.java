package se.yrgo.libraryapp.validators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class UtilsTest {
    @ParameterizedTest
    @CsvSource({
        "l0va, lva",
        "@boo, boo",
        "!donkey, donkey",
        "bla <>blus, bla blus",
        "Laban😂, Laban",
        "#'+?=^b,  b",
    })
    void onlyLettersAndWhitespace(String input, String expected) {
        assertEquals(Utils.onlyLettersAndWhitespace(input), Utils.onlyLettersAndWhitespace(expected));
    }

    @ParameterizedTest
    @CsvSource({ //leetspeak 1337 = leet
        "l0va, lova",
        "@boo, boo",
        "!<>blus bla, blus bla"
    })
    void cleanAndUnLeet(String input, String expected) {
        assertEquals(Utils.cleanAndUnLeet(input), Utils.cleanAndUnLeet(expected));
    }
}

// Metoden: onlyLettersAndWhitespace
// Remove any non-alphabetic letters from a string, but keep any whitespace.
// Will return the string as all lowecase.

// Metoden: cleanAndUnLeet
// Converts any "leet speak" letters into their alphabetic equivalent (i.e. 4 to a etc.) and 
// then removes any letters that are not alphabetic (but not whitespace). Will return the string
// as all lowecase.

// Två metoder behöver testas
// Dessa metoder är package-private, inga problem så länge man använder rätt paketstruktur

// Tänk igenom grundfall, randvillkor och specialfall
// Använd @ValueSource med flera