package com.codecool.zsuzsi.puzzlesbackend.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CipherMakerTest {

    private CipherMaker cipherMaker = new CipherMaker();

    @Test
    public void testShiftCipherWithNullString() {
        assertThrows(NullPointerException.class, () -> cipherMaker.createShiftCipher(null, 1));
    }

    @Test
    public void testShiftCipherWithEmptyString() {
        String inputString = "";

        String result = cipherMaker.createShiftCipher(inputString, 1);

        assertEquals(inputString, result);
    }

    @Test
    public void testShiftCipherWithZeroShift() {
        String inputString = "árvíztűrő tükörfúrógép";

        String result = cipherMaker.createShiftCipher(inputString, 0);

        assertEquals(inputString, result);
    }

    @Test
    public void testShiftCipherWithFewShifts() {
        String inputString = "árvíztűrő tükörfúrógép";
        String expected = "duylbüxur üwnquivupíhs";

        String result = cipherMaker.createShiftCipher(inputString, 3);

        assertEquals(expected, result);
    }

    @Test
    public void testShiftCipherWithManyShifts() {
        String inputString = "árvíztűrő tükörfúrógép";
        String expected = "pgmwöilge ikydgújgcüué";

        String result = cipherMaker.createShiftCipher(inputString, 20);

        assertEquals(expected, result);
    }

    @Test
    public void testShiftRandomWithNullString() {
        assertThrows(NullPointerException.class, () -> cipherMaker.createRandomCipher(null, 3));
    }

    @Test
    public void testRandomCipherWithEmptyString() {
        Map<String, Map<String, String>> result = cipherMaker.createRandomCipher("", 3);

        assertEquals(Set.of(""), result.keySet());
    }

    @Test
    public void testRandomCipherLetterHelp() {
        String input = "Elemér elmegy elemért.";
        Map<String, String> expected = new HashMap<>();
        expected.put("e", "");
        expected.put("l", "");
        expected.put("m", "");

        Map<String, Map<String, String>> result = cipherMaker.createRandomCipher(input, 3);

        Map<String, String> letterHelp = new HashMap<>();
        for (String item : result.keySet()) {
            letterHelp = result.get(item);
        }

        assertIterableEquals(expected.keySet(), letterHelp.keySet());
    }

    @Test
    public void testRandomCipherSameLetters() {
        String input = "Elemér elmegy elemért.";

        Map<String, Map<String, String>> result = cipherMaker.createRandomCipher(input, 3);

        String encryptedSentence = "";
        for (String item : result.keySet()) {
            encryptedSentence = item;
        }

        // all indexes of "e" hold the same character
        assertEquals(encryptedSentence.charAt(0), encryptedSentence.charAt(2));
        assertEquals(encryptedSentence.charAt(2), encryptedSentence.charAt(7));
        assertEquals(encryptedSentence.charAt(7), encryptedSentence.charAt(10));
        assertEquals(encryptedSentence.charAt(10), encryptedSentence.charAt(14));
        assertEquals(encryptedSentence.charAt(14), encryptedSentence.charAt(16));

        // all indexes of "m" hold the same character
        assertEquals(encryptedSentence.charAt(3), encryptedSentence.charAt(9));
        assertEquals(encryptedSentence.charAt(9), encryptedSentence.charAt(17));
    }

    @Test
    public void testRandomCipherDifferentLetters() {
        String input = "Elemér elmegy elemért.";

        Map<String, Map<String, String>> result = cipherMaker.createRandomCipher(input, 3);

        String encryptedSentence = "";
        for (String item : result.keySet()) {
            encryptedSentence = item;
        }

        assertNotEquals(encryptedSentence.charAt(0), encryptedSentence.charAt(1));
        assertNotEquals(encryptedSentence.charAt(0), encryptedSentence.charAt(11));
        assertNotEquals(encryptedSentence.charAt(3), encryptedSentence.charAt(4));
        assertNotEquals(encryptedSentence.charAt(4), encryptedSentence.charAt(10));
        assertNotEquals(encryptedSentence.charAt(11), encryptedSentence.charAt(12));
        assertNotEquals(encryptedSentence.charAt(19), encryptedSentence.charAt(20));
    }
}
