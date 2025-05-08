package compiler.lexer;

import compiler.exceptions.LexerException;
import compiler.reader.FileManager;
import compiler.reader.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private TokenDefinitions tokenDefinitions;
    private TokenClasifier tokenClasifier;

    /**
     * Constructor que inicializa el objeto {@code Lexer} y configura la lectura del archivo fuente.
     *
     * @throws FileNotFoundException Si no se encuentra el archivo.
     */
    public Lexer(FileManager fileManager) throws FileNotFoundException {
        this.currentPosition = 0;
        this.reader = fileManager;
        this.tokenDefinitions = new TokenDefinitions();
        this.tokenClasifier = new TokenClasifier(this);
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
                        Optional<Token> tokenOptional = Optional.ofNullable(tokenClasifier.commentOrDiv());
                        if (tokenOptional.isPresent()) {
                            return tokenOptional.get();
                        }
                    } else if (currentData.charAt(currentPosition) == '"') {
                        return tokenClasifier.classifyString();
                    } else if (Character.isLowerCase(currentData.charAt(currentPosition))) {
                        return tokenClasifier.classifyKeywordOrIdentifier();
                    } else if (Character.isUpperCase(currentData.charAt(currentPosition))) {
                        return tokenClasifier.classifyClassIdentifier();
                    } else if (tokenDefinitions.getSpecialSymbols().containsKey(currentData.charAt(currentPosition))) {
                        return tokenClasifier.classifySpecialSymbol();
                    } else if (tokenDefinitions.getOperators().containsKey(String.valueOf(currentData.charAt(currentPosition)))) {
                        return tokenClasifier.classifyOperator();
                    } else if (Character.isDigit(currentData.charAt(currentPosition))) {
                        return tokenClasifier.classifyNumbers();
                    } else {
                        throw new LexerException("No se reconoce el símbolo: " + currentData.charAt(currentPosition));
                    }
                }
            }

            // Cargar la siguiente línea del archivo
            textLine = reader.readNextLine();
            currentData = textLine.getData();
            currentPosition = 0;

        }

        // Cuando ya no haya más líneas, cerramos el archivo.
        reader.closeFile();

        // Si no hay más lexemas, devolvemos null
        return null;
    }

    public List<Token> getAllTokens() throws LexerException {
        List<Token> tokenList = new ArrayList<>();
        Token token;

        try {
            while ((token = getToken()) != null){
                tokenList.add(token);
                System.out.println(token.toString());
            }
            return tokenList;
        } catch (Exception e) {
            throw new LexerException(e.getMessage());
        }
    }

    public void incrementPosition(){
        currentPosition++;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Text getTextLine() {
        return textLine;
    }

    public void setTextLine(Text textLine) {
        this.textLine = textLine;
    }

    public String getCurrentData() {
        return currentData;
    }

    public void setCurrentData(String currentData) {
        this.currentData = currentData;
    }

    public FileManager getReader() {
        return reader;
    }

    public void setReader(FileManager reader) {
        this.reader = reader;
    }
}
