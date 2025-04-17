package compiler;

import compiler.lexer.Lexer;
import compiler.lexer.Token;
import compiler.reader.FileManager;

import java.io.FileNotFoundException;
import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        FileManager fileManager = new FileManager(".\\prueba.txt");
        try {
            Token token = new Token();
            Lexer lexer = new Lexer(fileManager);
            while ((token = lexer.getToken()) != null){
                System.out.println(token.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}