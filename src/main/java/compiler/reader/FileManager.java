package compiler.reader;

import java.io.BufferedReader; //https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * La clase {@code FileManager} se encarga de manejar la lectura de archivos de texto.
 * Proporciona métodos para leer líneas del archivo y verificar si hay más líneas disponibles.
 *
 * @author Joaquin Ruiz
 */
public class FileManager {

    /**
     * El lector de archivos utilizado para leer las líneas del archivo.
     */
    private BufferedReader reader;

    /**
     * La ruta del archivo que se está leyendo.
     */
    private String filePath;

    /**
     * El índice de la línea actual en el archivo.
     */
    private int lineIndex;

    /**
     * Constructor de la clase {@code FileManager}, que inicializa el archivo con la ruta especificada.
     *
     * @param filePath La ruta del archivo a leer.
     * @throws FileNotFoundException Si el archivo no se encuentra en la ubicación especificada.
     */
    public FileManager(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        reader = new BufferedReader(new java.io.FileReader(filePath));
        this.lineIndex = 0;
    }

    /**
     * Lee la siguiente línea del archivo y la devuelve como un objeto {@code Text}.
     * Incrementa el índice de la línea.
     *
     * @return Un objeto {@code Text} con la línea leída y su índice de línea.
     * @throws IOException Si ocurre un error durante la lectura del archivo.
     */
    public Text readNextLine() throws IOException {
        Text text = new Text();
        text.setLineIndex(++lineIndex);
        text.setData(reader.readLine());
        return text;
    }

    /**
     * Verifica si hay más líneas disponibles para leer en el archivo.
     * Si no hay más líneas, cierra el lector de archivos.
     *
     * @return {@code true} si hay más líneas disponibles, de lo contrario {@code false}.
     * @throws IOException Si ocurre un error durante la verificación de las líneas.
     */
    public boolean hasMoreLines() throws IOException {
        return reader.ready();
    }

    public void closeFile() throws IOException {
        reader.close();
    }
}
