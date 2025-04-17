package compiler.exceptions;

public class LexerException extends Exception{
    public LexerException(){
        super();
    }

    public LexerException(String mensaje) {
        super("Error: "+ mensaje);
    }
}
