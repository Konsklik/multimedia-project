import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * class representing an active game
 */
public class game {
    /**
     * the word the player is trying to find
     */
    String word;
    /**
     * set of all probable words given what the user has found
     */
    Set<String> words;
    /**
     * array of propabilities that a letter (first index) has to appear on a specific spot in the word (second index)
     * doesn't store real probabilities but the total number of times those letters appear in those spots
     */
    int[][] propabilities;
    /**
     * players remaining life if it drops to zero the game ends in defeat
     */
    int life = 5;
    /**
     * players collected points if they drop below zero the game ends in defeat
     */
    int points;
    /**
     * array to store which letters have been guessed correctly
     */
    boolean[] hit;
    /**
     * constructs a game from dictionairy dic. A word will be chosen at random, the set words will be filled with all words of equal length and probabilities will be initialized
     * the point arg is meant to carry on points from previous games if it's negative or null points are initialized to 15
     * @param dic dictionairy to chose a word from
     * @param point points earned in previous games
     */
    public game(dictionary dic, Integer point){
        if (point != null && point > 0){
            points = point;
        }
        else{
            points = 15;
        }
        words = new HashSet<String>(dic.words);
        int target = new Random().nextInt(words.size()), i = 0;
        for (String itter_word : words) {
            if (i == target){
                word = itter_word;
                break;
            }
            i++;
        }
        propabilities = new int[26][word.length()] ;
        hit = new boolean[word.length()];
        for (Iterator<String> itterator = words.iterator(); itterator.hasNext();) {
            String itter_word = itterator.next();
            if (itter_word.length() == word.length()){
                for (int j = 0; j < word.length(); j++) {
                    propabilities[itter_word.charAt(j)- 65][j]++;
                }
            }
            else{
                itterator.remove();
            }
        }
    }

    /**
     * prints and returns the letters that have been guessed correctly with '_' filling for those that have yet to be guessed
     * @return the target word with '_' replacing letters that have not yet been guessed correctly
     */
    public String print_found(){
        String result = "";
        for (int i = 0; i < hit.length; i++) {
            if (hit[i]){
                System.out.print(word.charAt(i));
                result += word.charAt(i);
            }
            else{
                System.out.print('_');
                result += '_';
            }
        }
        System.out.println("");
        return result;
    }

    /**
     * takes a guess from the player 
     * if the guess is correct it increases the players points by 5 points if it had a greater probability to be correct 0.6, 10 if probability greater than 0.4, 15 if probability greater than 0.25 and 30 probability lesser than 0.25
     * if the guess is wrong it decreases health by one and points by 15
     * recalculates probabilities given the new information
     * returns true if the player guessed correctly and false otherwise 
     * @param guess_char the character the player guesses
     * @param guess_spot the spot in the word the player guesses the character is
     * @return true if guess is correct, false if it is incorrect
     */
    public boolean make_move(char guess_char, int guess_spot){
        boolean result = (guess_char == word.charAt(guess_spot));
        if (result){
            hit[guess_spot] = true;
            if (propabilities[guess_char - 65][guess_spot]*100/words.size() >= 60) {
                points += 5;
            } 
            else{
                if(propabilities[guess_char - 65][guess_spot]*100/words.size() >= 40){
                    points += 10;
                }
                else{
                    if (propabilities[guess_char - 65][guess_spot]*100/words.size() >= 25) {
                        points += 15;                        
                    }
                    else{
                        points += 30;
                    }
                }
            }
        }
        else{
            System.out.println(guess_char + " " + word.charAt(guess_spot));
            life--;
            points -= 15;
        }
        recalculate_probabilities(guess_char, guess_spot);
        return result;
    }

    /**
     * calculates if the game has been won
     * @return true if the whole word has been guessed correctly, false otherwise
     */
    public boolean victory(){
        for (int i = 0; i < hit.length; i++) {
            if(!hit[i]){
                return false;
            }
        }
        return true;
    }

    /**
     * calculates if the game has been won
     * @return true if life has reached 0 or points have fallen bellow 0, false otherwise
     */
    public boolean defeat(){
        return life <= 0 || points < 0;
    }

    /**
     * recalculates the probabilities based on the players guess
     * @param guess_char the character the player guesses
     * @param guess_spot the spot in the word the player guesses the character is
     */
    private void recalculate_probabilities(char guess_char, int guess_spot){ 
        for (Iterator<String> itterator = words.iterator(); itterator.hasNext();) {
            String itter_word = itterator.next();
            if (!(itter_word.charAt(guess_spot) == guess_char) == (word.charAt(guess_spot) == guess_char)){
                for (int j = 0; j < word.length(); j++) {
                    propabilities[itter_word.charAt(j)- 65][j]--;
                }
                itterator.remove();
            }
        }
    }
}