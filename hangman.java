import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
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
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class hangman extends Application {
    static game play;
    int correct = 0;
    int turn = 0;
    dictionary dic;
    Integer p;
    
    public static void main(String[] args) {
        launch("");
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Background background = new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY));
        primaryStage.setTitle("dictionary picker");
        Label prompt = new Label("enter dictionary ID");
        Label error = new Label();
        TextField input = new TextField("OL15626917W");
        Button button = new Button("search");
        button.setOnAction((e) -> {
            String dic_name = input.getText();
            try {
                dic = new dictionary(dic_name);
                Stage s = (Stage) primaryStage.getScene().getWindow();
                s.close();
                sec();
            } catch (Exception err) {
                error.setText("an error happened\n please try again");
                err.printStackTrace();
            }
        });
        VBox box = new VBox(prompt, input, error, button);
        box.setBackground(background);
        box.setAlignment(Pos.CENTER);
        StackPane roota = new StackPane();
        roota.getChildren().add(box);
        primaryStage.setScene(new Scene(roota, 700, 300));
        primaryStage.show();
    }
    
    public void sec() {
        play = new game(dic, p);
        Stage primaryStage = new Stage();
        
        Background blanchedalmont = new Background(
            new BackgroundFill(Color.BLANCHEDALMOND, CornerRadii.EMPTY, Insets.EMPTY));
            Background burlywood = new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY));
            Background chocolate = new Background(new BackgroundFill(Color.CHOCOLATE, CornerRadii.EMPTY, Insets.EMPTY));
            Background brown = new Background(new BackgroundFill(Color.BROWN, CornerRadii.EMPTY, Insets.EMPTY));
            
            primaryStage.setTitle("MediaLab Hangman");
            
            Label words_in_dictionairy = new Label("word count: " + dic.words.size());
            Pane wd_pane = new Pane();
            wd_pane.setBackground(burlywood);
            wd_pane.getChildren().add(words_in_dictionairy);
            Label points = new Label("score: " + play.points);
            Pane p_pane = new Pane();
            p_pane.setBackground(blanchedalmont);
            p_pane.getChildren().add(points);
            Label correct_ratio = new Label("correct ratio: " + 0.0 + "%");
            Pane cr_pane = new Pane();
            cr_pane.setBackground(burlywood);
            cr_pane.getChildren().add(correct_ratio);
            VBox top = new VBox(wd_pane, p_pane, cr_pane);
            top.setBackground(chocolate);
            
            ImageView hang_image = new ImageView(new Image("images/health-" + play.life + ".png"));
            Label found_word = new Label(play.print_found());
        found_word.setFont(new Font("Arial", 30));
        VBox left_pane = new VBox(hang_image, found_word);
        left_pane.setAlignment(Pos.CENTER);
        
        Label[][] propabilities = new Label[26][play.word.length()];
        VBox[] prob_Box = new VBox[26];
        HBox right_pane = new HBox(10, new Separator(Orientation.VERTICAL));
        right_pane.setBackground(blanchedalmont);
        for (int j = 0; j < play.word.length(); j++) {
            prob_Box[j] = new VBox(new Separator(Orientation.HORIZONTAL));
            for (int i = 0; i < 26; i++) {
                propabilities[i][j] = new Label();
                propabilities[i][j]
                .setText((char) (i + 65) + ": " + play.propabilities[i][j] * 100 / play.words.size() + "%");
                propabilities[i][j].setFont(new Font("Courier New", 20));
                prob_Box[j].getChildren().add(propabilities[i][j]);
                prob_Box[j].getChildren().add(new Separator(Orientation.HORIZONTAL));
            }
            right_pane.getChildren().add(prob_Box[j]);
            right_pane.getChildren().add(new Separator(Orientation.VERTICAL));
        }
        
        HBox mid = new HBox(10, left_pane, new Separator(Orientation.VERTICAL), right_pane);
        mid.setAlignment(Pos.CENTER);
        mid.setBackground(chocolate);
        
        ComboBox<Integer> pos_choice = new ComboBox<>();
        for (int i = 0; i < play.word.length(); i++) {
            pos_choice.getItems().add(i);
        }
        Label pos_label = new Label("choose position:");
        ComboBox<Character> char_choice = new ComboBox<>();
        Label char_label = new Label("choose character:");
        Button guess = new Button("guess");
        HBox bot = new HBox(5, pos_label, pos_choice, new Separator(Orientation.VERTICAL), char_label, char_choice,
        new Separator(Orientation.VERTICAL), guess);
        bot.setBackground(blanchedalmont);
        
        guess.setOnAction((e) -> {
            Character guess_char = char_choice.getValue();
            Integer guess_spot = pos_choice.getValue();
            if (play.defeat() || play.victory() || play.hit[guess_spot]) {
                return;
            }
            if (guess_char != null && guess_spot != null) {
                if (play.make_move((char) guess_char, (int) guess_spot)) {
                    correct++;
                }
                found_word.setText(play.print_found());
                hang_image.setImage(new Image("images/health-" + play.life + ".png"));
                if (play.victory()) {
                    pop_up(primaryStage, true);
                }
                if (play.defeat()) {
                    pop_up(primaryStage, false);
                    found_word.setText(play.word);
                    found_word.setTextFill(Color.RED);
                }
                char_choice.getItems().clear();
                List<List<Integer>> temp = new ArrayList<>();
                for (int i = 0; i < 26; i++) {
                    temp.add(new ArrayList<>());
                    temp.get(i).add(play.propabilities[i][guess_spot]);
                    temp.get(i).add(i);
                }
                Collections.sort(temp, new Comparator<List<Integer>>() {
                    @Override
                    public int compare(List<Integer> o1, List<Integer> o2) {
                        return o1.get(0).compareTo(o2.get(0));
                    }
                });
                for (List<Integer> tup : temp) {
                    if (tup.get(0) != 0) {
                        char_choice.getItems().add((char) (tup.get(1) + 65));
                    }
                }
                
                for (int i = 0; i < 26; i++) {
                    for (int j = 0; j < play.word.length(); j++) {
                        propabilities[i][j].setText(
                            (char) (i + 65) + ": " + play.propabilities[i][j] * 100 / play.words.size() + "%");
                        }
                    }
                    turn++;
                    points.setText("score: " + play.points);
                    correct_ratio.setText("correct ratio: " + correct * 100 / (double) turn + "%");
                }
            });
            
            pos_choice.setOnAction((e) -> {
                int selected = pos_choice.getValue();
                char_choice.getItems().clear();
                List<List<Integer>> temp = new ArrayList<>();
                for (int i = 0; i < 26; i++) {
                    temp.add(new ArrayList<>());
                    temp.get(i).add(play.propabilities[i][selected]);
                    temp.get(i).add(i);
                }
                Collections.sort(temp, new Comparator<List<Integer>>() {
                    @Override
                    public int compare(List<Integer> o1, List<Integer> o2) {
                        return o1.get(0).compareTo(o2.get(0));
                    }
                });
                for (List<Integer> tup : temp) {
                    if (tup.get(0) != 0) {
                        char_choice.getItems().add((char) (tup.get(1) + 65));
                    }
                }
            });
            
        VBox vbox = new VBox(5, top, new Separator(Orientation.HORIZONTAL), mid, new Separator(Orientation.HORIZONTAL),
                bot);
        vbox.setBackground(brown);

        StackPane root = new StackPane();
        root.getChildren().add(vbox);
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        primaryStage.setScene(new Scene(root, screenBounds.getWidth() - 20, screenBounds.getHeight() - 80));
        primaryStage.show();
    }
    
    public void pop_up(Stage gameStage, boolean victory) {
        p = play.points;
        Background background = new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY));
        Stage primaryStage = new Stage();
        primaryStage.setTitle("you win");
        Button new_dic = new Button("new dictionary");
        new_dic.setOnAction((e) -> {
            Stage s = (Stage) primaryStage.getScene().getWindow();
            s.close();
            s = (Stage) gameStage.getScene().getWindow();
            s.close();
            try {
                start(gameStage);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });
        Button same_dic = new Button("same dictionairy");
        same_dic.setOnAction((e) -> {
            Stage s = (Stage) primaryStage.getScene().getWindow();
            s.close();
            s = (Stage) gameStage.getScene().getWindow();
            s.close();
            sec();
        });
        StackPane root = new StackPane();
        HBox h = new HBox(same_dic, new_dic);
        Label text;
        if (victory) {
            text = new Label("you win");
        } else {
            turn = 0;
            correct = 0;
            text = new Label("you lose");
        }
        VBox v = new VBox(text, h);
        v.setAlignment(Pos.CENTER);
        v.setBackground(background);
        root.getChildren().add(v);
        primaryStage.setScene(new Scene(root, 200, 100));
        primaryStage.show();
    }
}