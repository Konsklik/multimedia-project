import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class game {
    String word;
    Set<String> words;
    int[][] propabilities;
    int life = 5, points;
    boolean[] hit;
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

    public boolean victory(){
        for (int i = 0; i < hit.length; i++) {
            if(!hit[i]){
                return false;
            }
        }
        return true;
    }

    public boolean defeat(){
        return life <= 0 || points < 0;
    }

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