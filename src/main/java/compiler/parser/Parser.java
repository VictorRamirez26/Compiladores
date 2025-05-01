package compiler.parser;

import compiler.exceptions.LexerException;
import compiler.exceptions.ParserException;
import compiler.lexer.Lexer;
import compiler.lexer.Token;

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
        // te fijas si viene class o impl
        // llamo a Start
    }

    /*
        ⟨Start⟩ ::= start ⟨BloqueMétodo⟩
     */

    /*
        ⟨ListaDefiniciones⟩ ::= ⟨Class⟩ ⟨ListaDefiniciones⟩ | ⟨Impl⟩ ⟨ListaDefiniciones⟩ | λ
     */
    private void listaDefiniciones() throws ParserException, IOException, LexerException {
        if(match("class")){
            //llamo a class
        } else if (match("impl")) {
            //llamo a impl
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
        if(!match("class")){
            throw new ParserException("Se esperaba class");
        }
    }

    /*
        ⟨ClassF⟩ ::= {⟨Atributo⟩} | ⟨Herencia⟩ {⟨Atributo⟩}
     */

    /*
        ⟨Impl⟩ ::= impl idClass {⟨Miembro⟩}
     */
    private void impl() throws ParserException, IOException, LexerException {
        if(!match("impl")){
            throw new ParserException("Se esperaba impl");
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



