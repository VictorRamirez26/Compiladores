package compiler.parser;

import compiler.exceptions.LexerException;
import compiler.exceptions.ParserException;
import compiler.lexer.Lexer;
import compiler.lexer.Token;
import compiler.lexer.TokenType;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Parser {

    private Token currentToken;
    private Lexer lexer;

    private boolean match(String... lexemas) throws IOException, LexerException {
        boolean match = check(lexemas);
        if (match) {
            nextToken();
        }
        return match;
    }

    private boolean match(TokenType... tokenType) throws IOException, LexerException {
        boolean match = check(tokenType);
        if (match) {
            nextToken();
        }
        return match;
    }

    private boolean check(String... lexemas) {
        String actual = currentToken.getLexeme().getData();
        boolean matched = false;
        for (String lexema : lexemas) {
            if (actual.equals(lexema)) {
                matched = true;
            }
        }
        return matched;
    }

    private boolean check(TokenType... tokenTypes) {
        TokenType actual = currentToken.getTokenType();
        boolean matched = false;
        for (TokenType tokenType : tokenTypes) {
            if (actual.equals(tokenType)) {
                matched = true;
            }
        }
        return matched;
    }

    private void nextToken() throws IOException, LexerException {
        currentToken = lexer.getToken();
    }

    /*
        ⟨Inicio⟩ ::= ⟨Program⟩ $
     */
    private void inicio() throws ParserException, IOException, LexerException {
        program();
        match("$");
    }

    /*
        ⟨Program⟩ ::= ⟨ListaDefiniciones⟩ ⟨Start⟩
     */
    private void program() throws ParserException, IOException, LexerException {
        listaDefiniciones();
        //start();
    }

    /*
        ⟨Start⟩ ::= start ⟨BloqueMétodo⟩
     */
    private void start() throws IOException, LexerException {
        match("start");
        //bloqueMetodo();
    }

    /*
        ⟨ListaDefiniciones⟩ ::= ⟨Class⟩ ⟨ListaDefiniciones⟩ | ⟨Impl⟩ ⟨ListaDefiniciones⟩ | λ
     */
    private void listaDefiniciones() throws ParserException, IOException, LexerException {
        if(check("class")){
            //class();
        } else if (check("impl")) {
            //impl();
        } else if (check("start")){
            // CASO LAMBDA
        } else {
            throw new ParserException("Se esperaba: {class, impl, start}" );
        }
    }

    /*
        ⟨Class⟩ ::= class idClass ⟨ClassF⟩
     */
    private void classRegla() throws ParserException, IOException, LexerException {
        match("class");
        match(TokenType.IDENTIFIER_CLASS);
        //classF();
    }

    /*
        ⟨ClassF⟩ ::= {⟨Atributo⟩} | ⟨Herencia⟩ {⟨Atributo⟩}
     */
    private void classF() throws IOException, LexerException {
        if (match("{")) {
            //atributo();
            match("}");
        }else {
            //herencia();
            match("{");
            //atributo();
            match("}");
        }

    }

    /*
        ⟨Impl⟩ ::= impl idClass {⟨Miembro⟩}
     */
    private void impl() throws ParserException, IOException, LexerException {
        match("impl");
        match(TokenType.IDENTIFIER_CLASS);
        match("{");
        //miembro();
        match("}");
    }

    /*
        ⟨Herencia⟩ ::= : ⟨Tipo⟩
     */
    private void herencia() throws ParserException, IOException, LexerException {
        if(!match(":")) {
            throw new ParserException("Se esperaba \" : \"");
        }
        tipo();
    }

    /*
        ⟨TipoMétodo⟩ ::=  ⟨Tipo⟩ | void
     */
    private void tipoMétodo() throws ParserException, IOException, LexerException {
        String[] primerosTipo = new String[] {"idClass", "Array"};
        if(check(primerosTipo)){
            tipo();
        } else if (!match("void")) {
            throw new ParserException("Se esperaba un tipo de método: " + primerosTipo.toString() +
                    " " + "void");
        }
    }

    /*
        ⟨Tipo⟩ ::= ⟨TipoPrimitivo⟩ | idClass | Array ⟨TipoPrimitivo⟩
     */
    private void tipo() throws ParserException, IOException, LexerException {
        String[] primerosTipoPrimitivo = new String[] {"Str", "Bool", "Int", "Double"};
        String[] primerosTipo = new String[] {"idClass", "Array"};
        if(check(primerosTipoPrimitivo)){
            tipoPrimitivo();
        } else if(!match(primerosTipo)) {
            throw new ParserException("Se esperaba un tipo: " + primerosTipo.toString() +
                    " " + primerosTipoPrimitivo.toString());
        }
    }

    /*
        ⟨TipoPrimitivo⟩ ::= Str | Bool | Int | Double
     */
    private void tipoPrimitivo() throws ParserException, IOException, LexerException {
        String[] primerosTipoPrimitivo = new String[] {"Str", "Bool", "Int", "Double"};
        if(!match(primerosTipoPrimitivo)){
            throw new ParserException("Se esperaba un tipo primitivo: " + primerosTipoPrimitivo.toString());
        }
    }

    /*
        ⟨OpUnario⟩ ::= ++ | + | -- | - | ! | (int)
     */
    private void opUnario() throws ParserException, IOException, LexerException {
        String[] primerosOpUnario = new String[] {"++", "+", "--", "-", "!", "("};
        if(!match(primerosOpUnario)){
            throw new ParserException("Se esperaba un operador unario: " + primerosOpUnario.toString());
        }
    }

    /*
        ⟨OpCompuesto⟩ ::= <= | < | >= | >
     */
    private void opCompuesto() throws ParserException, IOException, LexerException {
        String[] primerosOpCompuesto = new String[] {"<=", "<", ">=", ">"};
        if(!match(primerosOpCompuesto)){
            throw new ParserException("Se esperaba un operador compuesto: " + primerosOpCompuesto.toString());
        }
    }

    /*
        ⟨OpMul⟩ ::= * | / | % | div
     */
    private void opMul() throws ParserException, IOException, LexerException {
        String[] primerosOpMul = new String[] {"*", "/", "%", "div"};
        if(!match(primerosOpMul)){
            throw new ParserException("Se esperaba un operador de multiplicación: " + primerosOpMul.toString());
        }
    }

}



