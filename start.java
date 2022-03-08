import java.util.Scanner;

public class start{
    public static String get_dict(){
        Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine();
        scanner.close();
        return result;
    }
    public static void main(String[] args){
        //start up window
        //loop? class? function w/ return dict?

        //game setup
        String dict_name = get_dict();
        try {
            dictionary dic = new dictionary(dict_name);
        } catch (InvalidCountException | UndersizeException | UnbalncedException | InvalidRangeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //choose_random(dict_list);
        //int letter = calc_propabilities();

        //game window
        //class
    }
}
