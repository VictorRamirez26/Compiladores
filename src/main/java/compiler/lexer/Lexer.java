package compiler.lexer;

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

    private static final Set<Character> SPECIAL_SYMBOLS = Set.of(
            '+', '-', '*', '/', '=', '<', '>', '{', '}', '(', ')', ';', ','
    );

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
    public Token getToken() throws IOException {

        // Este ciclo seguirá hasta que no haya más líneas
        while (reader.hasMoreLines() || currentData != null) {
            // Si hay una línea actual y aún quedan caracteres por procesar
            if (currentData != null && currentPosition < currentData.length()) {
                while (currentPosition < currentData.length()) {
                    // Saltar espacios en blanco en la línea actual
                    if (Character.isWhitespace(currentData.charAt(currentPosition))) {
                        currentPosition++;
                    } else if (Character.isLowerCase(currentData.charAt(currentPosition))) {
                        return classifyKeywordOrIdentifier();
                    } else if (SPECIAL_SYMBOLS.contains(currentData.charAt(currentPosition))) {
                        return classifySpecialSymbol();
                        
                    } else {
                        throw new RuntimeException("No se reconoce el símbolo: " + currentData.charAt(currentPosition));
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


    private Token classifyKeywordOrIdentifier(){

        StringBuffer buffer = new StringBuffer();

        if (Character.isLowerCase(currentData.charAt(currentPosition))) {
            char vistazo;
            do {

                buffer.append(currentData.charAt(currentPosition));
                ++currentPosition;

                if (currentPosition < currentData.length()){
                    vistazo = currentData.charAt(currentPosition);
                }else {
                    vistazo = ' ';
                }

            }while (Character.isLetterOrDigit(vistazo));

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

        return null;
    }

    private Token classifySpecialSymbol() {

        StringBuffer buffer = new StringBuffer();

        if (SPECIAL_SYMBOLS.contains(currentData.charAt(currentPosition))) {
            char vistazo;
            do {

                buffer.append(currentData.charAt(currentPosition));
                ++currentPosition;

                if (currentPosition < currentData.length()){
                    vistazo = currentData.charAt(currentPosition);
                }else {
                    vistazo = ' ';
                }

            }while (SPECIAL_SYMBOLS.contains(vistazo));

            String palabra = buffer.toString();
            Optional<TokenType> tokenType = Optional.ofNullable(tokenClassifier.getSpecialSymbols().get(palabra));
            Token token = new Token();

            if (tokenType.isPresent()){
                token.setTokenType(tokenType.get());
            }else {
                tokenType = Optional.ofNullable(tokenClassifier.getOperators().get(palabra));
                if (tokenType.isPresent()) {
                    token.setTokenType(tokenType.get());
                }
                token.setTokenType(TokenType.IDENTIFIER_OBJECT);
            }

            Lexeme lexeme = new Lexeme();
            lexeme.setData(palabra);
            lexeme.setColumnIndex(currentPosition - (palabra.length() - 1));
            lexeme.setLineIndex(textLine.getLineIndex());

            token.setLexeme(lexeme);
            return token;
        }

        return null;

    }

    /**
     * Procesa un lexema simple, que es cualquier secuencia de caracteres no blanca en la línea actual.
     *
     * @return Un objeto {@code Lexeme} con el lexema procesado o {@code null} si no se ha procesado un lexema.
     *
     * @author Joaquin Ruiz
     */
    private Lexeme processLexeme() {
        StringBuilder lexemeData = new StringBuilder();

        // Añadir caracteres a medida que sea parte de un lexema
        while (currentPosition < currentData.length() &&
                !Character.isWhitespace(currentData.charAt(currentPosition))) {
            lexemeData.append(currentData.charAt(currentPosition));
            currentPosition++;
        }

        // Si no hemos construido un lexema válido, no lo devolvemos
        if (lexemeData.length() == 0) {
            return null;
        }

        // Creamos el lexema con la data, línea y columna
        Lexeme lexeme = new Lexeme();
        lexeme.setData(lexemeData.toString());
        lexeme.setLineIndex(textLine.getLineIndex());
        lexeme.setColumnIndex(currentPosition - lexemeData.length() + 1);

        return lexeme;
    }
}
