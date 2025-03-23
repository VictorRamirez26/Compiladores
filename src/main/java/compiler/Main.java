package compiler;

import compiler.lexer.Token;
import compiler.lexer.TokenType;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Token token = new Token(TokenType.IDENTIFIER , "x", 1,1);
        System.out.println(token.toString());

    }
}