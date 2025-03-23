package compiler.reader;

/**
 * La clase {@code Text} representa una línea de texto con datos y el índice de la
 * línea en donde se encuentra ubicada en el archivo.
 *
 * @author Joaquin Ruiz
 */
public class Text {

    /**
     * Los datos de texto asociados a esta línea.
     */
    private String data;

    /**
     * El índice de la línea en el archivo.
     */
    private int lineIndex;

    /**
     * Constructor por defecto que inicializa la información de la línea como {@code null}.
     */
    public Text() {
        this.data = null;
    }

    /**
     * Obtiene los datos de la línea de texto.
     *
     * @return el contenido de la línea de texto.
     */
    public String getData() {
        return data;
    }

    /**
     * Establece los datos de la línea de texto.
     *
     * @param data el nuevo contenido de la línea de texto.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Obtiene el índice de la línea en el texto original.
     *
     * @return el índice de la línea.
     */
    public int getLineIndex() {
        return lineIndex;
    }

    /**
     * Establece el índice de la línea en el texto original.
     *
     * @param lineIndex el nuevo índice de la línea.
     */
    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public Character nextCharacter() {
        return null;
    }

    @Override
    public String toString() {
        return "TextLine{" +
                "data='" + data + '\'' +
                ", lineIndex=" + lineIndex +
                '}';
    }
}
