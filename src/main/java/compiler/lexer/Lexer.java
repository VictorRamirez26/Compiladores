package compiler.lexer;

import compiler.reader.FileManager;
import compiler.reader.Text;

import java.io.FileNotFoundException;
import java.io.IOException;

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

    /**
     * La columna actual en la que nos encontramos dentro de la línea.
     */
    private int currentColumn;

    /**
     * Constructor que inicializa el objeto {@code Lexer} y configura la lectura del archivo fuente.
     *
     * @throws FileNotFoundException Si no se encuentra el archivo.
     */
    public Lexer() throws FileNotFoundException {
        this.currentPosition = 0;
        this.currentColumn = 0; // Empezamos desde la columna 0
        reader = new FileManager("C:\\Users\\joaqu\\OneDrive\\Facultad\\Cuarto año\\Séptimo semestre\\Compiladores\\Compilador\\prueba.txt");
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
    public Lexeme extractLexeme() throws IOException {
        // Este ciclo seguirá hasta que no haya más líneas
        while (reader.hasMoreLines() || currentData != null) {
            // Si hay una línea actual y aún quedan caracteres por procesar
            if (currentData != null && currentPosition < currentData.length()) {
                // Saltar espacios en blanco en la línea actual
                while (currentPosition < currentData.length() &&
                        Character.isWhitespace(currentData.charAt(currentPosition))) {
                    currentPosition++;
                    currentColumn++;
                }
                // Si después de saltar espacios aún quedan caracteres, procesamos el lexema
                if (currentPosition < currentData.length()) {
                    return processLexeme();
                }
            }

            // Cargar la siguiente línea del archivo
            textLine = reader.readNextLine();
            currentData = textLine.getData();
            currentPosition = 0;
            currentColumn = 0;

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
            currentColumn++;
        }

        // Si no hemos construido un lexema válido, no lo devolvemos
        if (lexemeData.length() == 0) {
            return null;
        }

        // Creamos el lexema con la data, línea y columna
        Lexeme lexeme = new Lexeme();
        lexeme.setData(lexemeData.toString());
        lexeme.setLineIndex(textLine.getLineIndex());
        lexeme.setColumnIndex(currentColumn - lexemeData.length() + 1);

        return lexeme;
    }
}
