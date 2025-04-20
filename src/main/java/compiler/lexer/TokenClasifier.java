package compiler.lexer;

import compiler.exceptions.LexerException;

import java.io.IOException;
import java.util.Optional;

public class TokenClasifier {

    private Lexer lexer;
    private TokenDefinitions tokenDefinitions;

    public TokenClasifier(Lexer lexer) {
        this.lexer = lexer;
        this.tokenDefinitions = new TokenDefinitions();
    }

    public Token classifyString() throws LexerException {

        int string_size = 0;
        StringBuffer buffer = new StringBuffer();
        lexer.incrementPosition(); // Consumo la primer comilla
        boolean fin_cadena = false;

        while (lexer.getCurrentPosition() < lexer.getCurrentData().length()){
            char c = lexer.getCurrentData().charAt(lexer.getCurrentPosition()); // Caracter actual
            if (c == '"'){
                fin_cadena = true;
                lexer.incrementPosition(); // Consumo la ultima comilla
                break;
            }

            // Me fijo que la cadena no tenga el null '\0'
            if (lexer.getCurrentData().charAt(lexer.getCurrentPosition()) == '\\') {
                if (lexer.getCurrentPosition() + 1 < lexer.getCurrentData().length() &&
                        lexer.getCurrentData().charAt(lexer.getCurrentPosition() + 1) == '0') {

                    throw new LexerException("La cadena contiene un carácter nulo \\0 no permitido.");
                }
            }

            if (string_size >= 1024){
                throw new LexerException("El tamaño del String debe tener menos de 1024 caracteres.");
            }

            buffer.append(c);
            lexer.incrementPosition();
            string_size++;

        }

        if (!fin_cadena){
            throw new LexerException("Se esperaba el fin de la cadena.");
        }

        return generateToken(buffer.toString(), lexer.getCurrentPosition() - 2, lexer.getTextLine().getLineIndex(), TokenType.STRING_CONSTANT);
    }

    public Token commentOrDiv() throws IOException {

        if(lexer.getCurrentPosition() + 1 < lexer.getCurrentData().length()){
            if (lexer.getCurrentData().charAt(lexer.getCurrentPosition() + 1) == '*'){
                lexer.incrementPosition();
                multiComment();
            }else if (lexer.getCurrentData().charAt(lexer.getCurrentPosition() + 1) == '/'){
                lexer.incrementPosition();
                simpleComment();
            }else {
                return classifyOperator();
            }
        }else {
            return classifyOperator();
        }
        return null;
    }

    public void simpleComment(){
        while (lexer.getCurrentPosition() < lexer.getCurrentData().length()){
            lexer.incrementPosition();
        }
    }

    public void multiComment() throws IOException {
        do {
            if (lexer.getCurrentPosition() + 1 < lexer.getCurrentData().length()){
                if (lexer.getCurrentData().charAt(lexer.getCurrentPosition()) == '*'
                        && lexer.getCurrentData().charAt(lexer.getCurrentPosition() + 1) == '/'){
                    lexer.incrementPosition();
                    lexer.incrementPosition();
                    break;
                }else {
                    lexer.incrementPosition();
                }

            } else {
                // Cargar la siguiente línea del archivo
                lexer.setTextLine(lexer.getReader().readNextLine());
                lexer.setCurrentData(lexer.getTextLine().getData());
                lexer.setCurrentPosition(0);
            }
        }while (lexer.getCurrentPosition() < lexer.getCurrentData().length());
    }

    public Token classifyClassIdentifier(){

        StringBuffer buffer = new StringBuffer();
        TokenType tokenType = TokenType.IDENTIFIER_CLASS;

        char vistazo;
        do {

            buffer.append(lexer.getCurrentData().charAt(lexer.getCurrentPosition()));
            lexer.incrementPosition();

            if (lexer.getCurrentPosition() < lexer.getCurrentData().length()){
                vistazo = lexer.getCurrentData().charAt(lexer.getCurrentPosition());
            }else {
                vistazo = ' ';
            }

        } while (Character.isLetter(vistazo));

        return generateToken(buffer.toString(), lexer.getCurrentPosition(), lexer.getTextLine().getLineIndex(), tokenType);
    }

    public Token classifyNumbers(){

        StringBuffer buffer = new StringBuffer();
        TokenType tokenType = TokenType.INT_CONSTANT;
        int state = 0; // State 0: int, State 1: int + . , State 2: int + . + int, State 3: double + e ,
        // State 4: double + e + {+,-} , State 5: double + e + {+,-} + int
        if(Character.isDigit(lexer.getCurrentData().charAt(lexer.getCurrentPosition()))){
            char vistazo;
            do {
                buffer.append(lexer.getCurrentData().charAt(lexer.getCurrentPosition()));
                lexer.incrementPosition();

                if (lexer.getCurrentPosition() < lexer.getCurrentData().length()){
                    vistazo = lexer.getCurrentData().charAt(lexer.getCurrentPosition());

                    if (vistazo == '.' && state == 0) {
                        state = 1;
                    } else if (Character.isDigit(vistazo) && state == 1) {
                        state = 2;
                        tokenType = TokenType.DOUBLE_CONSTANT;
                    } else if (vistazo == 'e' && state == 2) {
                        state = 3;
                    } else if ((vistazo == '+' || vistazo == '-') && state == 3) {
                        state =4;
                    } else if ((Character.isDigit(vistazo) && state == 3)) { // Caso especial 12.42e10
                        state = 5;
                    } else if (Character.isDigit(vistazo) && state == 4) {
                        state = 5;
                    }else if (!Character.isDigit(vistazo)){
                        break;
                    }

                }else {
                    vistazo = ' ';
                }

            } while (Character.isDigit(vistazo) || vistazo == '.' || vistazo == 'e'
                    || vistazo == '+' || vistazo == '-');

            if (state == 1 || state == 3 || state == 4){
                tokenType = TokenType.UNDEFINED;
            }

            return generateToken(buffer.toString(), lexer.getCurrentPosition(), lexer.getTextLine().getLineIndex(), tokenType);

        }
        return null;
    }

    public Token classifyKeywordOrIdentifier(){

        StringBuffer buffer = new StringBuffer();

        char vistazo;
        do {

            buffer.append(lexer.getCurrentData().charAt(lexer.getCurrentPosition()));
            lexer.incrementPosition();

            if (lexer.getCurrentPosition() < lexer.getCurrentData().length()){
                vistazo = lexer.getCurrentData().charAt(lexer.getCurrentPosition());
            }else {
                vistazo = ' ';
            }

        } while (Character.isLetterOrDigit(vistazo) || vistazo == '_');

        String palabra = buffer.toString();

        Optional<TokenType> tokenType = Optional.ofNullable(tokenDefinitions.getKeywords().get(palabra));

        return generateToken(palabra, lexer.getCurrentPosition(), lexer.getTextLine().getLineIndex(), tokenType.orElse(TokenType.IDENTIFIER_OBJECT));

    }

    public Token classifySpecialSymbol() {

        String palabra = String.valueOf(lexer.getCurrentData().charAt(lexer.getCurrentPosition()));

        Optional<TokenType> tokenType = Optional
                .ofNullable(tokenDefinitions.getSpecialSymbols()
                        .get(lexer.getCurrentData().charAt(lexer.getCurrentPosition()))); // Me fijo si es un simbolo especial

        lexer.incrementPosition();
        return generateToken(palabra, lexer.getCurrentPosition(), lexer.getTextLine().getLineIndex(), tokenType.orElse(TokenType.IDENTIFIER_OBJECT));
    }

    public Token classifyOperator(){

        String operator = String.valueOf(lexer.getCurrentData().charAt(lexer.getCurrentPosition()));
        TokenType tokenType = null;

        if (tokenDefinitions.getOperators().containsKey(operator)) {
            if (lexer.getCurrentPosition() < lexer.getCurrentData().length()) {

                if (lexer.getCurrentPosition() + 1 < lexer.getCurrentData().length()) {
                    char vistazo = lexer.getCurrentData().charAt(lexer.getCurrentPosition() + 1);
                    if (tokenDefinitions.getOperators().containsKey(operator + vistazo)) {
                        tokenType = tokenDefinitions.getOperators().get(operator + vistazo);
                        operator = operator + vistazo;
                        lexer.incrementPosition();
                    }else {
                        tokenType = tokenDefinitions.getOperators().get(operator);
                    }
                } else {
                    tokenType = tokenDefinitions.getOperators().get(operator);
                }


            }
        }
        lexer.incrementPosition();
        return generateToken(operator, lexer.getCurrentPosition(), lexer.getTextLine().getLineIndex(), tokenType);

    }

    public Token generateToken(String data, int currentPosition, int lineIndex, TokenType tokenType){

        Token token = new Token();
        Lexeme lexeme = new Lexeme();
        lexeme.setData(data);
        lexeme.setColumnIndex(currentPosition - (data.length() - 1));
        lexeme.setLineIndex(lineIndex);

        token.setTokenType(tokenType);
        token.setLexeme(lexeme);

        return token;
    }

}
