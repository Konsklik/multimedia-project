import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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



/**
 * Exception thrown when dictionary being constructed contains duplicate words
 * Will only be thrown when construsting from file. If constructing from web duplicates will be ignored
 */
class InvalidCountException extends Exception{}
/**
 * Exception thrown when dictionairy being constructed has less than 20 words
 */
class UndersizeException extends Exception{}
/**
 * Exception thrown when dictionairy being constructed contains words with length less than 6
 * Will only be thrown when construsting from file. If constructing from web short words will be ignored
 */
class InvalidRangeException extends Exception{
	public InvalidRangeException(){
		super("word under 6 letters found");
	}
}
/**
 * Exception thrown when dictionairy being constructed has less 20% of it's words be of length 9 or greater
 */
class UnbalncedException extends Exception{
	public UnbalncedException(String errorMessage){
		super(errorMessage);
	}
}

/**
 * class to create a dictionairy can be used to initiate a game class
 */
public class dictionary {
    /**
     * set of all words in the dictionairy
     */
    Set<String>  words = new HashSet<String>();
    /**
     * creates new dictionary initialized from the file .\multimedia\hangman_'dic_ID'.txt if it exists, 
     * if it does not the file is created from data collected at https://openlibrary.org/works/'dic_ID'.json
     * @param dic_ID an ID for the dictionairy
     * @throws InvalidCountException
     * @throws UndersizeException
     * @throws UnbalncedException
     * @throws InvalidRangeException
     * @throws FileNotFoundException
     */
    public dictionary(String dic_ID) throws InvalidCountException, UndersizeException, UnbalncedException, InvalidRangeException, FileNotFoundException{
        String directory = new File("").getAbsolutePath();
        File dic = new File(directory + "\\multimedia\\hangman_" + dic_ID + ".txt");
        if(!dic.isFile()){
            url_handler(dic, "https://openlibrary.org/works/" + dic_ID + ".json");
        }
        else{
            file_handler(dic);
        }
    };

    /**
     * private function that handles dictionairy constraction from a json file pointed at by the url variable
     * it expects the json to contain a field with the name "description" that is either a string or it contains a fied called "value" that is a string
     * @param dic file that the dictionairy will be stored at
     * @param url url to look up, must be to a json file
     * @throws UndersizeException
     * @throws UnbalncedException
     * @throws FileNotFoundException
     */
    private void url_handler(File dic, String url) throws UndersizeException, UnbalncedException, FileNotFoundException{
        try {
            InputStream is = new URL(url).openStream();
            JsonReader rdr = Json.createReader(is);
            JsonObject obj = rdr.readObject();
            JsonString jtext = get_desc_value(obj);
            String text = jtext.getString();
            write_dic(dic, text);
            System.out.println("dictionary initialized from web");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };
    
    /**
     * fuction that returns the string of field "value" in the field "description" of obj or if that doesn't exist returns the string of field "description"
     * @param obj json object from which to extract the information
     * @return  string from "description":"value" or "description"
     */
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

    /**
     * writes dictionairy to file and save all valid words in words variable. Will automatically the proper folders for the dictionary to be put in
     * @param dic file to write in
     * @param data  text to be proccessed
     * @throws IOException
     * @throws UndersizeException
     * @throws UnbalncedException
     */
    private  void write_dic(File dic, String data) throws IOException, UndersizeException, UnbalncedException{
        words = tokenize(data);
        dic.getParentFile().mkdirs();
        dic.createNewFile();
        FileWriter wr = new FileWriter(dic);
        for (String word : words){
            wr.write(word + '\n');
        }
        wr.close();
    }

    /**
     * breaks data to individual words and returns them in a set. Words of length under 6 get thrown out
     * @param data string to be broken down
     * @return  set of valid words
     * @throws UndersizeException
     * @throws UnbalncedException
     */
	private  Set<String> tokenize(String data) throws UndersizeException, UnbalncedException{
        String[] words_arr = data.split("[^a-zA-Z]+",0);
		Set<String> result = new HashSet<String>();
        int over_9 = 0;
        for (int i = 0; i < words_arr.length; i++) {
            if (words_arr[i].length() >= 6){
                if (words_arr[i].length() >= 9){
                    over_9++;
                }
                String to_add = words_arr[i].toUpperCase();
				result.add(to_add);
			}
        }
        if (over_9 < (int) (words.size() * 0.2)) {
            throw new UnbalncedException("minimum number of words with length greater than 9: "
                    + (int) (words.size() * 0.2) + "\n number of words with length greater than 9: " + over_9);
        }
		if (result.size() < 20){
            throw new UndersizeException();
        }
        return result;
    };

    /**
     * function that handles construction from file
     * @param dic file to be read
     * @throws InvalidCountException
     * @throws UndersizeException
     * @throws UnbalncedException
     * @throws InvalidRangeException
     */
    private void file_handler(File dic) throws InvalidCountException, UndersizeException, UnbalncedException, InvalidRangeException {
        try {
            BufferedReader rd = new BufferedReader(new FileReader(dic));
            String line = rd.readLine();
            int  over_9 = 0;
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
                line = rd.readLine();
            }
            if (over_9 < (int) (words.size() * 0.2)) {
                rd.close();
                throw new UnbalncedException("minimum number of words with length greater than 9: "
                        + (int) (words.size() * 0.2) + "\n number of words with length greater than 9: " + over_9);
            }
            if (words.size() < 20) {
                rd.close();
                throw new UndersizeException();
            }
            rd.close();
            System.out.println("dictionary initialized from file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
