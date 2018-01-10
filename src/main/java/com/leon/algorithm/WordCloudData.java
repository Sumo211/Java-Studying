package com.leon.algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @see <a href="https://www.interviewcake.com/question/java/word-cloud">Source</a>
 * When are words that 'should be' lowercase not? What are the pros and cons for each one?
 * None of them are perfect. They all have tradeoffs, and it is very difficult to write a highly accurate algorithm
 * There were lots of heuristics and approaches we could have used to solve problems, each with their own strengths and weaknesses
 * Challenge yourself to keep thinking even after you have a first solution. Come up with several solutions, weigh them carefully, and choose the best solution for the given context.
 * O(n) time and O(n) space
 */
// TODO: 1/10/2018 Write unit tests
public class WordCloudData {

    private HashMap<String, Integer> wordsToCounts = new HashMap<>();

    public WordCloudData(String input) {
        populateWordsToCounts(input);
    }

    private void populateWordsToCounts(String input) {
        int currentWordStartIndex = 0;
        int currentWordLength = 0;

        for (int i = 0; i < input.length(); i++) {
            // iterates over each character in the input string, splitting words and passing them to addWordToHashMap()
            char character = input.charAt(i);

            // if we reached the end of the string we check if the last character is a letter and add the last word to our hash map
            if (i == input.length() - 1) {
                if (Character.isLetter(character)) {
                    currentWordLength++;
                }

                if (currentWordLength > 0) {
                    String currentWord = input.substring(currentWordStartIndex, currentWordStartIndex + currentWordLength);
                    addWordToHashMap(currentWord);
                }

            // if we reach a space or emdash we know we're at the end of a word so we add it to our hash map and reset our current word
            } else if (character == ' ' || character == '\u2014') {
                if (currentWordLength > 0) {
                    String currentWord = input.substring(currentWordStartIndex, currentWordStartIndex + currentWordLength);
                    addWordToHashMap(currentWord);
                    currentWordLength = 0;
                }

            // we want to make sure we split on ellipses so if we get two periods in a row we add the current word to our hash map and reset our current word
            } else if (character == '.') {
                if (i < (input.length() - 1) && input.charAt(i + 1) == '.') {
                    if (currentWordLength > 0) {
                        String currentWord = input.substring(currentWordStartIndex, currentWordStartIndex + currentWordLength);
                        addWordToHashMap(currentWord);
                        currentWordLength = 0;
                    }
                }

            // if the character is a letter or an apostrophe, we add it to our current word
            } else if (Character.isLetter(character) || character == '\'') {
                if (currentWordLength == 0) {
                    currentWordStartIndex = i;
                }
                currentWordLength++;

            // if the character is a hyphen, we want to check if it's surrounded by letters. If it is, we add it to our current word
            } else if (character == '-') {
                if (i > 0 && Character.isLetter(input.charAt(i - 1)) && Character.isLetter(input.charAt(i + 1))) {
                    if (currentWordLength == 0) {
                        currentWordStartIndex = i;
                    }
                    currentWordLength++;
                } else {
                    if (currentWordLength > 0) {
                        String currentWord = input.substring(currentWordStartIndex, currentWordStartIndex + currentWordLength);
                        addWordToHashMap(currentWord);
                        currentWordLength = 0;
                    }
                }
            }
        }
    }

    private void addWordToHashMap(String word) {
        // if the word is already in the hash map we increment its count
        if (wordsToCounts.containsKey(word)) {
            wordsToCounts.put(word, wordsToCounts.get(word) + 1);

        // if a lowercase version is in the hash map, we know our input word must be uppercase
        // but we only include uppercase words if they're always uppercase so we just increment the lowercase version's count
        } else if (wordsToCounts.containsKey(word.toLowerCase())) {
            int newCount = wordsToCounts.get(word.toLowerCase()) + 1;
            wordsToCounts.put(word.toLowerCase(), newCount);

        // if an uppercase version is in the hash map, we know our input word must be lowercase
        // since we only include uppercase words if they're always uppercase, we add the lowercase version and give it the uppercase version's count
        } else if (wordsToCounts.containsKey(capitalize(word))) {
            int newCount = wordsToCounts.get(capitalize(word)) + 1;
            wordsToCounts.put(word, newCount);
            wordsToCounts.remove(capitalize(word));

        // otherwise, the word is not in the hash map at all, lowercase or uppercase so we add it to the hash map
        } else {
            wordsToCounts.put(word, 1);
        }
    }

    private String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public Map<String, Integer> getWordsToCounts() {
        return Collections.unmodifiableMap(wordsToCounts);
    }

}
