package compiler.exceptions;

public class ParserException extends Exception{
    public ParserException(){
        super();
    }

    public ParserException(String mensaje) {
        super("Error: "+ mensaje);
    }
}
