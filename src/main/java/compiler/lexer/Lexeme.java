package compiler.lexer;

import compiler.reader.Text;

/**
 * La clase {@code Lexeme} representa un lexema en el análisis léxico.
 * Extiende la funcionalidad de la clase {@code Text} y añade
 * el índice de columna donde se encuentra el lexema.
 *
 * @author Joaquin Ruiz
 */
public class Lexeme extends Text {

    /**
     * Índice de la columna donde se encuentra el lexema.
     */
    private int columnIndex;

    /**
     * Constructor por defecto que inicializa un lexema vacío.
     */
    public Lexeme() {
        super();
    }

    /**
     * Obtiene el índice de la columna del lexema.
     *
     * @return el índice de la columna.
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /**
     * Establece el índice de la columna del lexema.
     *
     * @param columnIndex el nuevo índice de la columna.
     */
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    @Override
    public String toString() {
        return "Lexeme{" +
                "data='" + getData() + '\'' +
                ", lineIndex=" + getLineIndex() +
                ", columnIndex=" + columnIndex +
                '}';
    }
}
