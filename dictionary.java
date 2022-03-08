import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;



class InvalidCountException extends Exception{}
class UndersizeException extends Exception{}
class InvalidRangeException extends Exception{
	public InvalidRangeException(){
		super("word under 6 letters");
	}
}
class UnbalncedException extends Exception{
	public UnbalncedException(String errorMessage){
		super(errorMessage);
	}
}


public class dictionary {
    Set<String>  words = new HashSet<String>();
    public dictionary(String dic_ID) throws InvalidCountException, UndersizeException, UnbalncedException, InvalidRangeException{
        String directory = new File("").getAbsolutePath();
        File dic = new File(directory + "\\multimedia\\hangman_" + dic_ID + ".txt");
        if(!dic.isFile()){
            url_handler(dic, "https://openlibrary.org/works/" + dic_ID + ".json");
        }
        else{
            file_handler(dic);
        }
    };
    
    private void url_handler(File dic, String url){
        try {
            InputStream is = new URL(url).openStream();
            JsonReader rdr = Json.createReader(is);
            JsonObject obj = rdr.readObject();
            JsonString jtext = get_desc_value(obj);
            String text = jtext.getString();
            write_dic(dic, text);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
    
    private  JsonString get_desc_value(JsonObject obj){
        JsonString result;
        try{
            JsonObject step = obj.getJsonObject("description");
            result = step.getJsonString("value");
        }catch(ClassCastException e){
            result = obj.getJsonString("description");
        }
        return result;
    };

    private  void write_dic(File dic, String data) throws IOException{
        words = tokenize(data);
        dic.getParentFile().mkdirs();
        dic.createNewFile();
        FileWriter wr = new FileWriter(dic);
        for (String word : words){
            wr.write(word + '\n');
        }
        wr.close();
    }

	private  Set<String> tokenize(String data){
        String[] words_arr = data.split("[^a-zA-Z]+",0);
		Set<String> result = new HashSet<String>();
        for (int i = 0; i < words_arr.length; i++) {
            if (words_arr[i].length() >= 6){
                String to_add = words_arr[i].toUpperCase();
				result.add(to_add);
			}
        }
		if (result.size() >= 20){
	        return result;
		}
		return null;
    };

    private void file_handler(File dic) throws InvalidCountException, UndersizeException, UnbalncedException, InvalidRangeException {
        try {
            BufferedReader rd = new BufferedReader(new FileReader(dic));
            String line = rd.readLine();
            int line_num = 0, over_9 = 0;
            while (line != null) {
                if (words.contains(line)) {
                    rd.close();
                    throw new InvalidCountException();
                }
                if (line.length() < 6) {
                    rd.close();
                    throw new InvalidRangeException();
                }
                words.add(line);
                if (line.length() >= 9) {
                    over_9++;
                }
                line_num++;
                line = rd.readLine();
            }
            if (over_9 < (int) (line_num * 0.2)) {
                rd.close();
                throw new UnbalncedException("minimum number of words with length greater than 9: "
                        + (int) (line_num * 0.2) + "\n number of words with length greater than 9: " + over_9);
            }
            if (line_num < 20) {
                rd.close();
                throw new UndersizeException();
            }
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
