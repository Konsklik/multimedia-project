#include<stdlib.h>

int main(){
    system("javac -cp \"./;./lib/javax.json.jar\" game.java dictionary.java start.java");
    system("java -cp \"./;./lib/javax.json.jar\" start");
    system("pause");
}