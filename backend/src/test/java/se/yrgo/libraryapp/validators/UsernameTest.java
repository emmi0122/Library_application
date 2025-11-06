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

// Vad ska vara med? Javadoc
// no funny characters or whitespace and at least four characters long.
// A username should be at least four characters long and only
// contain ASCII letters, numbers and the characters @, ., _ and -.
// It should not contain any other letters, not even whitespace.


// Testa det som din specifikation säger
// Testa alltid enkla grundfall
// Testa specialfall och randvärden

// Username är en boolean vill troligen returnera både true och false
// Inga undantag skall kastas
// Få med det som står i dokumenationen, bokstäver, siffror och vissa specialtecken
// Undvik att teta användarnamn som består av enbart bokstäver

// Vissa specialfall för strängar brukar vara null, tomsträng och kanske en med bara blanktecken
// Randvärden på gränsen för heltal skulle kunna vara max och min värden för Integer
// I fallet med användarnamn är dem precis på gränsen till giltiga i längd, tre och fyra tecken långa
// Bör dem ha separata tester eller kan de gå in i enkla grundfall?

// @ValueSource
// @EmptySource
