import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.VLineTo;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class window extends Application {
    int score = 0;
    double ratio = 0.0;
    static game play;
    public static void go(game p) {
        play = p;
        launch("");
    }
    public static void main(String[] args) {
        
    }
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Background blanchedalmont = new Background(new BackgroundFill(Color.BLANCHEDALMOND, CornerRadii.EMPTY, Insets.EMPTY));
        Background burlywood = new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY));
        Background chocolate = new Background(new BackgroundFill(Color.CHOCOLATE, CornerRadii.EMPTY, Insets.EMPTY));
        Background brown = new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY));
        
        primaryStage.setTitle("MediaLab Hangman");


        Label words_in_dictionairy = new Label("word count: " + play.words.size());
        Pane wd_pane = new Pane();
        wd_pane.setBackground(burlywood);
        wd_pane.getChildren().add(words_in_dictionairy);
        Label points = new Label("score: " + score);
        Pane p_pane = new Pane();
        p_pane.setBackground(blanchedalmont);
        p_pane.getChildren().add(points);
        Label correct_ratio = new Label("correct ratio: " + ratio + "%");
        Pane cr_pane = new Pane();
        cr_pane.setBackground(burlywood);
        cr_pane.getChildren().add(correct_ratio);
        VBox top = new VBox(wd_pane, p_pane, cr_pane);
        top.setBackground(chocolate);


        Image ina = new Image("images/health-6.png");
        ImageView a0 = new ImageView(ina);
        Label a1 = new Label(play.print_found());
        a1.setFont(new Font("Arial", 30));
        VBox left_pane = new VBox(a0,a1);
        left_pane.setAlignment(Pos.CENTER);
        
        Label[][] propabilities = new Label[26][play.word.length()];
        HBox[] prob_Box = new HBox[26];
        VBox right_pane = new VBox(new Separator(Orientation.HORIZONTAL));
        right_pane.setBackground(blanchedalmont);
        for (int i = 0; i < 26; i++) {
            prob_Box[i] = new HBox(10, new Separator(Orientation.VERTICAL));
            for (int j = 0; j < play.word.length(); j++) {
                propabilities[i][j] = new Label();
                propabilities[i][j].setText((char)(i + 65) + ": " + play.propabilities[i][j]);
                propabilities[i][j].setFont(new Font("Courier New", 20));
                prob_Box[i].getChildren().add(propabilities[i][j]);
                prob_Box[i].getChildren().add(new Separator(Orientation.VERTICAL));
            }
            right_pane.getChildren().add(prob_Box[i]);
            right_pane.getChildren().add(new Separator(Orientation.HORIZONTAL));
        }

        HBox mid = new HBox (10, left_pane, new Separator(Orientation.VERTICAL), right_pane);
        mid.setAlignment(Pos.CENTER);
        mid.setBackground(chocolate);
        

        ComboBox<Integer> pos_choice = new ComboBox<>();
        for (int i = 0; i < play.word.length(); i++) {
            pos_choice.getItems().add(i);
        }
        Label pos_label = new Label("choose position:");
        ComboBox<Character> char_choice = new ComboBox<>();
        Label char_label = new Label("choose character:");
        HBox bot = new HBox(5, pos_label, pos_choice, new Separator(Orientation.VERTICAL), char_label, char_choice);

        pos_choice.setOnAction((e)->{
            int selected = pos_choice.getValue();
            char_choice.getItems().clear();
            for (int i = 0; i < 26; i++) {
                if (play.propabilities[i][selected] != 0){
                    char_choice.getItems().add((char)(i + 65));
                }                
            }
        });


        VBox vbox = new VBox(5, top, new Separator(Orientation.HORIZONTAL), mid, new Separator(Orientation.HORIZONTAL), bot);
        vbox.setBackground(brown);

        
        StackPane root = new StackPane();
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root, 1600, 900));
        primaryStage.show();
    }
}