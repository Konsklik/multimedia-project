javac -cp "./;./lib/javafx-sdk-17.0.2/lib/*;./lib/javax.json.jar" --module-path="./lib/javafx-sdk-17.0.2/lib" --add-modules="javafx.base,javafx.controls,javafx.fxml,javafx.media,javafx.graphics,javafx.swing,javafx.web"  game.java dictionary.java hangman.java 
java -cp "./;./lib/javafx-sdk-17.0.2/lib/*;./lib/javax.json.jar" --module-path="./lib/javafx-sdk-17.0.2/lib" --add-modules="javafx.base,javafx.controls,javafx.fxml,javafx.media,javafx.graphics,javafx.swing,javafx.web"  hangman
del *.class