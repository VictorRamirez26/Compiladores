package compiler.lexer;

/**
 * Representa un token en el proceso de análisis léxico.
 * Un token es una unidad léxica con un tipo específico, un valor asociado y
 * una ubicación en el código fuente (línea y columna).
 * <p>
 * Este objeto es utilizado por el lexer para identificar los componentes
 * de un programa fuente y clasificarlos según su tipo.
 * </p>
 *
 * @author Victor Ramirez
 * @author Joaquin Ruiz
 */
public class Token {
    /** Tipo del token (palabra clave, identificador, número, etc.). */
    private TokenType tokenType;

    /** Valor del token (ejemplo: "if", "variableName", "123"). */
    private String value;

    /** Número de línea donde se encuentra el token en el código fuente. */
    private int line;

    /** Número de columna donde inicia el token en la línea. */
    private int column;

    /**
     * Constructor vacío de la clase Token.
     * Crea un token sin inicializar sus atributos.
     */
    public Token() {
    }

    /**
     * Constructor de la clase Token.
     *
     * @param tokenType Tipo del token.
     * @param value Valor del token.
     * @param line Línea en la que aparece el token.
     * @param column Columna en la que comienza el token.
     */
    public Token(TokenType tokenType, String value, int line, int column) {
        this.tokenType = tokenType;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    /**
     * Obtiene el tipo del token.
     *
     * @return Tipo del token.
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Establece el tipo del token.
     *
     * @param tokenType Nuevo tipo del token.
     */
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Obtiene el valor del token.
     *
     * @return Valor del token como cadena de texto.
     */
    public String getValue() {
        return value;
    }

    /**
     * Establece el valor del token.
     *
     * @param value Nuevo valor del token.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Obtiene la línea donde se encuentra el token.
     *
     * @return Número de línea donde aparece el token.
     */
    public int getLine() {
        return line;
    }

    /**
     * Establece la línea donde se encuentra el token.
     *
     * @param line Nueva línea donde se ubica el token.
     */
    public void setLine(int line) {
        this.line = line;
    }

    /**
     * Obtiene la columna donde comienza el token.
     *
     * @return Número de columna donde inicia el token.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Establece la columna donde comienza el token.
     *
     * @param column Nueva columna de inicio del token.
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Representación en cadena del token, mostrando su tipo, valor, línea y columna.
     *
     * @return Cadena que representa el token.
     */
    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", value='" + value + '\'' +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
