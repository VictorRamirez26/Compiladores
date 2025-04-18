package compiler.lexer;

import compiler.exceptions.LexerException;
import compiler.reader.FileManager;
import compiler.reader.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * La clase {@code Lexer} es responsable de analizar y extraer lexemas del archivo de entrada.
 *
 * @author Victor Ramirez
 * @author Joaquin Ruiz
 */
public class Lexer {

    /**
     * La posición actual en la línea que estamos procesando.
     */
    private int currentPosition;

    /**
     * El gestor de archivos utilizado para leer el archivo de entrada.
     */
    private FileManager reader;

    /**
     * El objeto que representa la línea de texto actual leída desde el archivo.
     */
    private Text textLine;

    /**
     * El texto de la línea actual que se está procesando.
     */
    private String currentData;

    private TokenClassifier tokenClassifier;

    /**
     * Constructor que inicializa el objeto {@code Lexer} y configura la lectura del archivo fuente.
     *
     * @throws FileNotFoundException Si no se encuentra el archivo.
     */
    public Lexer(FileManager fileManager) throws FileNotFoundException {
        this.currentPosition = 0;
        this.reader = fileManager;
        this.tokenClassifier = new TokenClassifier();
    }

    /**
     * Extrae el siguiente lexema del archivo de entrada.
     * Avanza por las líneas del archivo y saltará los espacios en blanco hasta encontrar un lexema.
     *
     * @return Un objeto {@code Lexeme} con la información del lexema extraído o {@code null} si no hay más lexemas.
     * @throws IOException Si ocurre un error durante la lectura del archivo.
     *
     * @author Joaquin Ruiz
     */
    public Token getToken() throws IOException, LexerException {

        // Este ciclo seguirá hasta que no haya más líneas
        while (reader.hasMoreLines() || currentData != null) {
            // Si hay una línea actual y aún quedan caracteres por procesar
            if (currentData != null && currentPosition < currentData.length()) {
                while (currentPosition < currentData.length()) {
                    // Saltar espacios en blanco en la línea actual
                    if (Character.isWhitespace(currentData.charAt(currentPosition))) {
                        currentPosition++;
                    } else if (currentData.charAt(currentPosition) == '/') {
                        Optional<Token> tokenOptional = Optional.ofNullable(commentOrDiv());
                        if (tokenOptional.isPresent()) {
                            return tokenOptional.get();
                        }
                    } else if (currentData.charAt(currentPosition) == '"') {
                        return classifyString();
                    } else if (Character.isLowerCase(currentData.charAt(currentPosition))) {
                        return classifyKeywordOrIdentifier();
                    } else if (Character.isUpperCase(currentData.charAt(currentPosition))) {
                        return classifyClassIdentifier();
                    } else if (tokenClassifier.getSpecialSymbols().containsKey(currentData.charAt(currentPosition))) {
                        return classifySpecialSymbol();
                    } else if (tokenClassifier.getOperators().containsKey(String.valueOf(currentData.charAt(currentPosition)))) {
                        return classifyOperator();
                    } else if (Character.isDigit(currentData.charAt(currentPosition))) {
                        return classifyNumbers();
                    } else {
                        throw new LexerException("No se reconoce el símbolo: " + currentData.charAt(currentPosition));
                    }
                }
            }

            // Cargar la siguiente línea del archivo
            textLine = reader.readNextLine();
            currentData = textLine.getData();
            currentPosition = 0;

            // Si la línea está vacía o tiene solo espacios, se continuará en el siguiente ciclo para cargar otra línea
            if (currentData == null || currentData.trim().isEmpty()) {
                continue;
            }
        }

        // Cuando ya no haya más líneas, cerramos el archivo.
        reader.closeFile();

        // Si no hay más lexemas, devolvemos null
        return null;
    }

    private Token classifyString() throws LexerException {

        int string_size = 0;
        StringBuffer buffer = new StringBuffer();
        currentPosition++; // Consumo la primer comilla
        boolean fin_cadena = false;

        while (currentPosition < currentData.length()){
            char c = currentData.charAt(currentPosition); // Caracter actual
            if (c == '"'){
                fin_cadena = true;
                currentPosition++; // Consumo la ultima comilla
                break;
            }

            // Me fijo que la cadena no tenga el null '\0'
            if (currentData.charAt(currentPosition) == '\\') {
                if (currentPosition + 1 < currentData.length() &&
                        currentData.charAt(currentPosition + 1) == '0') {

                    throw new LexerException("La cadena contiene un carácter nulo \\0 no permitido.");
                }
            }

            if (string_size >= 1024){
                throw new LexerException("El tamaño del String debe tener menos de 1024 caracteres.");
            }

            buffer.append(c);
            currentPosition++;
            string_size++;

        }

        if (!fin_cadena){
           throw new LexerException("Se esperaba el fin de la cadena.");
        }

        String palabra = buffer.toString();
        Token token = new Token();
        token.setTokenType(TokenType.STRING_CONSTANT);

        Lexeme lexeme = new Lexeme();
        lexeme.setData(palabra);
        lexeme.setColumnIndex(currentPosition - (palabra.length() - 1));
        lexeme.setLineIndex(textLine.getLineIndex());

        token.setLexeme(lexeme);
        return token;

    }



    private Token commentOrDiv() throws IOException {

        if(currentPosition + 1 < currentData.length()){
            if (currentData.charAt(currentPosition + 1) == '*'){
                currentPosition++;
                multiComment();
            }else if (currentData.charAt(currentPosition + 1) == '/'){
                currentPosition++;
                simpleComment();
            }else {
                return classifyOperator();
            }
        }else {
            return classifyOperator();
        }
        return null;
    }

    private void simpleComment(){
        while (currentPosition < currentData.length()){
            currentPosition++;
        }
    }

    private void multiComment() throws IOException {
        do {
            if (currentPosition + 1 < currentData.length()){
                if (currentData.charAt(currentPosition) == '*'
                        && currentData.charAt(currentPosition + 1) == '/'){
                    currentPosition = currentPosition + 2;
                    break;
                }else {
                    currentPosition++;
                }

            } else {
                // Cargar la siguiente línea del archivo
                textLine = reader.readNextLine();
                currentData = textLine.getData();
                currentPosition = 0;
            }
        }while (currentPosition < currentData.length());
    }

    private Token classifyClassIdentifier(){

        StringBuffer buffer = new StringBuffer();
        TokenType tokenType = TokenType.IDENTIFIER_CLASS;

        char vistazo;
        do {

            buffer.append(currentData.charAt(currentPosition));
            ++currentPosition;

            if (currentPosition < currentData.length()){
                vistazo = currentData.charAt(currentPosition);
            }else {
                vistazo = ' ';
            }

        } while (Character.isLetter(vistazo));

        String palabra = buffer.toString();
        Token token = new Token();
        token.setTokenType(tokenType);

        Lexeme lexeme = new Lexeme();
        lexeme.setData(palabra);
        lexeme.setColumnIndex(currentPosition - (palabra.length() - 1));
        lexeme.setLineIndex(textLine.getLineIndex());

        token.setLexeme(lexeme);
        return token;
    }

    private Token classifyNumbers(){

        StringBuffer buffer = new StringBuffer();
        TokenType tokenType = TokenType.INT_CONSTANT;
        int state = 0; // State 0: int, State 1: int + . , State 2: int + . + int, State 3: double + e ,
                        // State 4: double + e + {+,-} , State 5: double + e + {+,-} + int
        if(Character.isDigit(currentData.charAt(currentPosition))){
            char vistazo;
            do {
                buffer.append(currentData.charAt(currentPosition));
                ++currentPosition;

                if (currentPosition < currentData.length()){
                    vistazo = currentData.charAt(currentPosition);

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
            String number = buffer.toString();
            Token token = new Token();
            token.setTokenType(tokenType);

            Lexeme lexeme = new Lexeme();
            lexeme.setData(number);
            lexeme.setColumnIndex(currentPosition - (number.length() - 1));
            lexeme.setLineIndex(textLine.getLineIndex());

            token.setLexeme(lexeme);
            return token;

        }
        return null;
    }

    private Token classifyKeywordOrIdentifier(){

        StringBuffer buffer = new StringBuffer();

        char vistazo;
        do {

            buffer.append(currentData.charAt(currentPosition));
            ++currentPosition;

            if (currentPosition < currentData.length()){
                vistazo = currentData.charAt(currentPosition);
            }else {
                vistazo = ' ';
            }

        } while (Character.isLetterOrDigit(vistazo) || vistazo == '_');

        String palabra = buffer.toString();
        Optional<TokenType> tokenType = Optional.ofNullable(tokenClassifier.getKeywords().get(palabra));
        Token token = new Token();

        if (tokenType.isPresent()){
            token.setTokenType(tokenType.get());
        }else {
            token.setTokenType(TokenType.IDENTIFIER_OBJECT);
        }

        Lexeme lexeme = new Lexeme();
        lexeme.setData(palabra);
        lexeme.setColumnIndex(currentPosition - (palabra.length() - 1));
        lexeme.setLineIndex(textLine.getLineIndex());

        token.setLexeme(lexeme);
        return token;

    }

    private Token classifySpecialSymbol() {

        String palabra = String.valueOf(currentData.charAt(currentPosition));

        Optional<TokenType> tokenType = Optional
                .ofNullable(tokenClassifier.getSpecialSymbols()
                        .get(currentData.charAt(currentPosition))); // Me fijo si es un simbolo especial

        return generateToken(palabra, ++currentPosition, textLine.getLineIndex(), tokenType.orElse(TokenType.IDENTIFIER_OBJECT));
    }

    private Token classifyOperator(){

        String operator = String.valueOf(currentData.charAt(currentPosition));
        TokenType tokenType = null;
        //Token token = new Token();

        if (tokenClassifier.getOperators().containsKey(operator)) {
            if (currentPosition < currentData.length()) {

                if (currentPosition + 1 < currentData.length()) {
                    char vistazo = currentData.charAt(currentPosition + 1);
                    if (tokenClassifier.getOperators().containsKey(operator + vistazo)) {
                        tokenType = tokenClassifier.getOperators().get(operator + vistazo);
                        operator = operator + vistazo;
                        ++currentPosition;
                    }else {
                        tokenType = tokenClassifier.getOperators().get(operator);
                    }
                } else {
                    tokenType = tokenClassifier.getOperators().get(operator);
                }


            }
        }

        return generateToken(operator, ++currentPosition, textLine.getLineIndex(), tokenType);

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
