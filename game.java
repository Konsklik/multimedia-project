import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class game {
    String word;
    Set<String> words;
    int[][] propabilities;
    int life = 5;
    boolean[] hit;
    public game(dictionary dic){
        words = dic.words;
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
        recalculate_probabilities(guess_char, guess_spot);
        boolean result = (guess_char == word.charAt(guess_spot));
        if (result){
            hit[guess_spot] = true;
        }
        else{
            life--;
        }
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

    private void recalculate_probabilities(char guess_char, int guess_spot){ 
        for (Iterator<String> itterator = words.iterator(); itterator.hasNext();) {
            String itter_word = itterator.next();
            if ((itter_word.charAt(guess_spot) == guess_char) == (word.charAt(guess_spot) == guess_char)){
                for (int j = 0; j < word.length(); j++) {
                    propabilities[itter_word.charAt(j)- 65][j]--;
                }
                itterator.remove();
            }
        }
    }
}