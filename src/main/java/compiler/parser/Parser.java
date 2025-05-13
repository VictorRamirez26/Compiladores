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
            classRegla();
            listaDefiniciones();
        } else if (check("impl")) {
            impl();
            listaDefiniciones();
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
        classF();
    }

    /*
        ⟨ClassF⟩ ::= {⟨Atributo⟩} | ⟨Herencia⟩ {⟨Atributo⟩}
     */
    private void classF() throws IOException, LexerException, ParserException {
        if (match("{")) {
            atributo();
            match("}");
        }else {
            herencia();
            match("{");
            atributo();
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
        ⟨TipoMetodo⟩ ::=  ⟨Tipo⟩ | void
     */
    private void tipoMetodo() throws ParserException, IOException, LexerException {
        String[] primerosTipo = new String[] {"Array","Str", "Bool", "Int", "Double"};
        if(check(primerosTipo) || check(TokenType.IDENTIFIER_CLASS)){
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
        String[] primerosTipo = new String[] {"Array"};
        if(check(primerosTipoPrimitivo)){
            tipoPrimitivo();
        } else if(match(primerosTipo)) {
            tipoPrimitivo();
        } else if (!match(TokenType.IDENTIFIER_CLASS)) {
            throw new ParserException("Se esperaba un tipo: " + TokenType.IDENTIFIER_CLASS);
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
        ⟨Atributo⟩ ::= <Visibilidad> <Tipo> <ListaDeclaracionVariables> ; <Atributo>
                    | <Tipo> <ListaDeclaraciónVariables> ; <Atributo>
                    | λ
     */
    private void atributo() throws IOException, LexerException, ParserException {
        String[] primerosVisibilidad = new String[] {"pub"};
        String[] primerosTipo = new String[] {"Array", "Str", "Bool", "Int", "Double"};

        if (check(TokenType.SPECIAL_SYMBOL_RCB)){
            return;
        }

        if (check(primerosVisibilidad)){
            visibilidad();
            tipo();
            listaDeclaracionVariables();
            match(";");
        } else if (check(primerosTipo) || check(TokenType.IDENTIFIER_CLASS)) {
            tipo();
            listaDeclaracionVariables();
            match(";");
        }else {
            throw new ParserException("Se esperaba 'pub' o un tipo de dato");
        }

        atributo();

    }

    /*
        ⟨Visibilidad⟩ ::= pub
    */
    private void visibilidad() throws IOException, LexerException {
        match("pub");
    }

    /*
        ⟨ListaDeclaracionVariables⟩ ::= idMetAt <ListaDeclaracionVariablesF>
     */
    private void listaDeclaracionVariables() throws IOException, LexerException, ParserException {
        match(TokenType.IDENTIFIER_OBJECT);
        listaDeclaracionVariablesF();
    }

    /*
    ⟨ListaDeclaracionVariablesF⟩ ::= , <ListaDeclaraciónVariables>
                                    | λ
    */
    private void listaDeclaracionVariablesF() throws IOException, LexerException, ParserException {
        if (match(",")){
            listaDeclaracionVariables();
        } else if (check(TokenType.SPECIAL_SYMBOL_S)) {
            // Caso de lambda
        }else {
            throw new ParserException("Se esperaba una ','");
        }
    }

    /*
    ⟨Miembro⟩ ::= <Metodo> <MiembroF1>
                | <Constructor> <MiembroF1>
    */
    private void miembro() throws ParserException {
        String[] primerosMetodo = new String[] {"fn","st"};
        String[] primerosConstructor = new String[] {"."};
        if (check(primerosMetodo)){
            //metodo();
        } else if (check(primerosConstructor)) {
            //constructor();
        }else {
            throw new ParserException("Se esperaba 'fn' , 'st' o '.'");
        }
    }

    /*
   ⟨Metodo⟩ ::= fn <MetodoF1>
               | <FormaMetodo> fn <MetodoF1>
   */

    private void metodo() throws IOException, LexerException {
        if (match("fn")){
            //metodoF1();
        } else{
            //formaMetodo();
            match("fn");
            //metodoF1();
        }
    }

    /*
   ⟨MetodoF1⟩ ::=  idMetAt <ArgumentosFormales> <BloqueMetodo>
               | <TipoMetodo> idMetAt <ArgumentosFormales> <BloqueMetodo>
   */

    private void metodoF1() throws IOException, LexerException, ParserException {
        if (match(TokenType.IDENTIFIER_OBJECT)){
            argumentosFormales();
            //bloqueMetodo();
        }else {
            tipoMetodo();
            match(TokenType.IDENTIFIER_OBJECT);
            argumentosFormales();
            //bloqueMetodo();
        }
    }

    /*
   ⟨ArgumentosFormales⟩ ::=  ( <ArgumentosFormalesF>
   */
    private void argumentosFormales() throws IOException, LexerException, ParserException {
        match("(");
        argumentosFormalesF();
    }

    /*
    ⟨ArgumentosFormalesF⟩ ::= <ListaArgumentosFormales> )
                            | )
    */
    private void argumentosFormalesF() throws IOException, LexerException, ParserException {
        String[] primerosListaArgumentosFormales = new String[] {"Array", "Str", "Bool", "Int", "Double"};
        if (check(primerosListaArgumentosFormales) || check(TokenType.IDENTIFIER_CLASS)){
            listaArgumentosFormales();
        }
        match(")");
    }

    /*
        ⟨ListaArgumentosFormales⟩ ::= <ArgumentoFormal> <ListaArgumentosFormalesF>
    */
    private void listaArgumentosFormales() throws IOException, LexerException, ParserException {
        argumentoFormal();
        listaArgumentosFormalesF();
    }

    /*
    ⟨ListaArgumentosFormalesF⟩ ::= , <ListaArgumentosFormales>
                                | λ
    */
    private void listaArgumentosFormalesF() throws IOException, LexerException, ParserException {
        if (match(",")){
            listaArgumentosFormales();
        } else if (check(")")) {
            // Caso lambda
        }else {
            throw new ParserException("Se esperaba una ','");
        }
    }

    /*
        ⟨ArgumentoFormal⟩ ::= <Tipo> idMetAt
    */
    private void argumentoFormal() throws IOException, LexerException, ParserException {
        tipo();
        match(TokenType.IDENTIFIER_OBJECT);
    }

    /*
    ⟨BloqueMetodo⟩ ::= { <DeclVarLocales> <BloqueMetodoF>
                    | {<SentenciaRec>}
                    | {}
    */
    private void bloqueMetodo() throws IOException, LexerException, ParserException {
        String[] primerosDeclVarLocales = new String[] {"Array", "Str", "Bool", "Int", "Double"};
        String[] primerosSentenciaRec = new String[] {";" , "if","while","ret","self","(","{"};
        match("{");
        if (check(primerosDeclVarLocales) || check(TokenType.IDENTIFIER_CLASS)){
            declVarLocales();
            bloqueMetodoF();
        } else if (check(primerosSentenciaRec) || check(TokenType.IDENTIFIER_OBJECT)) {
            //sentenciaRec();
            match("}");
        } else if (!match("}")) {
            throw new ParserException("Se esperaba '}'");
        }
    }

    /*
    ⟨BloqueMetodoF⟩ ::= <SentenciaRec> }
                    | }
    */
    private void bloqueMetodoF() throws IOException, LexerException {
        String[] primerosSentenciaRec = new String[] {";" , "if","while","ret","self","(","{"};
        if (check(primerosSentenciaRec) || check(TokenType.IDENTIFIER_OBJECT)){
            //sentenciaRec();
        }
        match("}");
    }

    /*
    ⟨DeclVarLocales⟩ ::= <Tipo> <ListaDeclaracionVariables> ; <DeclVarLocalesF>
    */

    private void declVarLocales() throws IOException, LexerException, ParserException {
        tipo();
        listaDeclaracionVariables();
        match(";");
        declVarLocalesF();
    }

    /*
    ⟨DeclVarLocalesF⟩ ::= <DeclVarLocales>
                        | λ
    */

    private void declVarLocalesF() throws IOException, LexerException, ParserException {

        String[] primerosDeclVarLocales = new String[] {"Array", "Str", "Bool", "Int", "Double"};
        String[] siguientesDeclVarLocalesF = new String[] {";" , "if","while","ret","self","(","}"};

        if (check(primerosDeclVarLocales) || check(TokenType.IDENTIFIER_CLASS)) {
            declVarLocales();
        }else if (check(siguientesDeclVarLocalesF) || check(TokenType.IDENTIFIER_OBJECT)){
            // Caso lambda
        }else {
            throw new ParserException();
        }
    }

    /*
        ⟨ExpUn⟩ ::= ⟨OpUnario⟩ ⟨ExpUn⟩ | ⟨Operando⟩
     */
    private void expUn() throws ParserException, IOException, LexerException {
        String[] primerosOpUnario = new String[]  {"++", "+", "--", "-", "!", "("};
        String[] primerosOperando = new String[] {"nil", "true", "false", "intLiteral", "StrLiteral", "doubleLiteral", "(", "self", "id", "idclass", "new"};
        if(check(primerosOpUnario)){
            opUnario();
            expUn();
        } else if(check(primerosOperando)) {
            operando();
        } else {
            throw new ParserException("Se esperaba " + primerosOpUnario.toString() + " " + primerosOperando.toString());
        }
    }

    /*
        ⟨OpIgual⟩ ::= == | !=
     */
    private void opIgual() throws IOException, LexerException, ParserException {
        String[] primerosOpIgual = new String[] {"==", "!="};
        if(!match(primerosOpIgual)){
            throw new ParserException("Se esperaba un operador de igualdad: " + primerosOpIgual.toString());
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
        ⟨OpAd⟩ ::= + | -
     */
    private void opAd() throws IOException, LexerException, ParserException {
        String[] primerosOpAd = new String[] {"+", "-"};
        if(!match(primerosOpAd)){
            throw new ParserException("Se esperaba un operador de suma: " + primerosOpAd.toString());
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
        ⟨OpMul⟩ ::= * | / | % | div
     */
    private void opMul() throws ParserException, IOException, LexerException {
        String[] primerosOpMul = new String[] {"*", "/", "%", "div"};
        if(!match(primerosOpMul)){
            throw new ParserException("Se esperaba un operador de multiplicación: " + primerosOpMul.toString());
        }
    }

    /*
        ⟨Operando⟩ ::= ⟨Literal⟩ | ⟨Primario⟩ ⟨OperandoF⟩
     */
    private void operando() throws IOException, LexerException, ParserException {
        String[] primerosLiteral = new String[] {"nil", "true", "false"};
        TokenType[] primerosLiteralTokenType = new TokenType[] {TokenType.INT_CONSTANT , TokenType.STRING_CONSTANT , TokenType.DOUBLE_CONSTANT};
        String[] primerosPrimario = new String[] {"(", "self", "new"};

        if(check(primerosLiteral) || check(primerosLiteralTokenType)) {
            literal();
        } else if (check(primerosPrimario) || check(TokenType.IDENTIFIER_CLASS , TokenType.IDENTIFIER_OBJECT)) {
            //primario()
            //operandoF()
        } else {
            throw new ParserException("Se esperaba un operando: " + primerosPrimario.toString() + " " + primerosLiteral.toString());
        }
    }

    /*
        ⟨OperandoF⟩ ::= λ | ⟨Encadenado⟩
     */
    private void operandoF() throws IOException, LexerException, ParserException {
        String[] primerosEncadenado = new String[] {"."};
        String[] siguientesOperandoF = new String[] {"*", "/", "%", "div", "+", "-", "<=", "<", ">=", ">", "==", "!=", "&&", "||", ")" , ";" , "]", ","};
        if(check(primerosEncadenado)){
            //encadenado()
        } else if (check(siguientesOperandoF)) {
            //CASO LAMBDA
        } else {
            throw new ParserException("Se esperaba : " + primerosEncadenado.toString() + " " + siguientesOperandoF.toString());
        }
    }

    /*
        ⟨Literal⟩ ::= nil | true | false | intLiteral | StrLiteral | doubleLiteral
     */
    private void literal() throws ParserException, IOException, LexerException {
        String[] primerosLiteralString = new String[] {"nil", "true", "false"};
        TokenType[] primerosLiteralTokenType = new TokenType[] {TokenType.INT_CONSTANT , TokenType.STRING_CONSTANT , TokenType.DOUBLE_CONSTANT};
        if(!match(primerosLiteralString) || !match(primerosLiteralTokenType)){
            throw new ParserException("Se esperaba un literal: " + primerosLiteralString.toString()
                    + primerosLiteralTokenType.toString());
        }
    }

    /*
        ⟨Encadenado⟩ ::= . ⟨EncadenadoF⟩
     */
    private void encadenado() throws IOException, LexerException, ParserException {
        if(!match(".")){
            throw new ParserException("Se esperaba \".\"");
        }
        encadenadoF();

    }

    /*
        ⟨EncadenadoF⟩ ::= ⟨LlamadaMétodoEncadenado⟩ | ⟨AccesoVariableEncadenado⟩
     */
    private void encadenadoF() throws ParserException {
        String[] primerosLlamadaMetodoEncadenado = new String[] {"."};

        if(check(primerosLlamadaMetodoEncadenado)) {
            //llamadaMétodoEncadenado()
        } else if (check(currentToken.getTokenType())) {
            //accesoVariableEncadenado
        } else {
            throw new ParserException("Se esperaba \".\" o identificador de objeto");
        }

    }
}



