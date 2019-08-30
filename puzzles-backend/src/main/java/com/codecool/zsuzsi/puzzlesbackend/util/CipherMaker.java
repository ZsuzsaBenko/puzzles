package com.codecool.zsuzsi.puzzlesbackend.util;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CipherMaker {

    private final List<String> ALPHABET = Arrays.asList("a", "á", "b", "c", "d",
            "e", "é", "f", "g", "h", "i", "í", "j", "k", "l", "m", "n", "o", "ó", "ö",
            "ő", "p", "q", "r", "s", "t", "u", "ú", "ü", "ű", "v", "w", "x", "y", "z");


    public String createShiftCipher(String text, int difference) {
        text = text.toLowerCase();
        String result = "";

        for (int i = 0; i < text.length(); i++) {
            int letterIndex = ALPHABET.indexOf(String.valueOf(text.charAt(i)));
            if (letterIndex != -1) {
                if (difference < ALPHABET.size() - letterIndex) {
                    result = result.concat(ALPHABET.get(letterIndex + difference));
                } else {
                    int index = letterIndex + difference - ALPHABET.size();
                    result = result.concat(ALPHABET.get(index));
                }
            } else {
                result = result.concat(String.valueOf(text.charAt(i)));
            }

        }
        return result;
    }

    public Map<String, Map<String, String>> createRandomCipher(String text, int helperLetterNumber) {
        text = text.toLowerCase();
        Map<String, String> encryptionKey = createEncryptionKey();
        String encryptedText = encryptText(text, encryptionKey);

        List<String> mostFrequentLetters = getMostFrequentLetters(text, helperLetterNumber);
        Map<String, String> helper = new HashMap<>();
        for (String letter : mostFrequentLetters) {
            helper.put(letter, encryptionKey.get(letter));
        }

        Map<String, Map<String, String>> result = new HashMap<>();
        result.put(encryptedText, helper);
        return result;
    }

    private String encryptText(String text, Map<String, String> encryptionKey) {
        text = text.toLowerCase();
        String result = "";

        for (int i = 0; i < text.length(); i++) {
            if (ALPHABET.indexOf(String.valueOf(text.charAt(i))) > -1) {
                result = result.concat(encryptionKey.get(String.valueOf(text.charAt(i))));
            } else {
                result = result.concat(String.valueOf(text.charAt(i)));
            }
        }
        return result;
    }

    private Map<String, String> createEncryptionKey() {
        Map<String, String> encryptionKey = new HashMap<>();
        Random random = new Random();

        List<String> alphabetCopy = new ArrayList<>(ALPHABET);

        for (String letter : ALPHABET) {
            int randomIndex = random.nextInt(alphabetCopy.size());
            encryptionKey.put(letter, alphabetCopy.get(randomIndex));
            alphabetCopy.remove(randomIndex);
        }
        return encryptionKey;
    }

   private Map<String, Integer> countLetterFrequency(String text) {
       Map<String, Integer> letterFrequencies = new HashMap<>();
       for (int i = 0; i < text.length(); i++) {
           String letter = String.valueOf(text.charAt(i));
           letterFrequencies.merge(letter, 1, Integer::sum);
       }
       return  letterFrequencies;
   }

   private List<String> getMostFrequentLetters(String text, int helperLetterNumber) {
       Map<String, Integer> letterFrequencies = countLetterFrequency(text);
       List<String> mostFrequentLetters = new ArrayList<>();
       for (int i = 0; i < helperLetterNumber; i++) {
           String mostFreq = "";
           int freq = 0;
           for (String letter : letterFrequencies.keySet()) {
               if (letterFrequencies.get(letter) > freq && ALPHABET.contains(letter)) {
                   mostFreq = letter;
                   freq = letterFrequencies.get(letter);
               }
           }
           letterFrequencies.remove(mostFreq);
           mostFrequentLetters.add(mostFreq);
       }
       return mostFrequentLetters;
   }
}
