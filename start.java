import java.io.IOException;
import java.util.Scanner;

public class start{
    public static void main(String[] args) throws IOException{
        //start up window
        //loop? class? function w/ return dict?
        
        //game setup
        Scanner scanner = new Scanner(System.in);
        String dict_name = scanner.nextLine();
        try {
            dictionary dic = new dictionary(dict_name);
            game play = new game(dic);
            //game window
            //class
            window.go(play);
            /*while (!play.victory() && play.life > 0){
                play.print_found();
                char letter = scanner.next().charAt(0);
                int pos = scanner.nextInt();
                if(play.make_move(letter, pos)){
                    System.out.println("hit");
                }
                else{
                    System.out.println("miss");
                }
            }
            scanner.close();
            if (play.victory()){
                System.out.println("success");
            }
            else{
                System.out.println("defeat");
            }*/
        } catch (InvalidCountException | UndersizeException | UnbalncedException | InvalidRangeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
