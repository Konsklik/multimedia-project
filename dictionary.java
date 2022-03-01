import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class dictionary {
    String[] words = {};
    int[][] propabilities = {};
    public dictionary(String dic_ID){
        File dic = new File(dic_ID);
        if(!f.isFile()){
            url_handler(f, "example.com/" + dic_ID);
        }
        Scanner reader = new Scanner(f);
        int i = 0
        while (reader.hasNextLine()){
            words[i] = reader.nextLine();
            i++;
        }
    };
    public String get_word(){
        return Random(words);
    };
    public void calc_propabilities(){

    };

    private void url_handler(File f, String url){
        //download
        tokenize(data)
    };

    private String[] tokenize(String data){

    };
}
