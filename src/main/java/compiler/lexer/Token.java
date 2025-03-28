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

    private Lexeme lexeme;

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

     */
    public Token(TokenType tokenType, Lexeme lexeme) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Lexeme getLexeme() {
        return lexeme;
    }

    public void setLexeme(Lexeme lexeme) {
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return "Token{" +
                "lexeme=" + lexeme.toString() +
                ", tokenType=" + tokenType +
                '}';
    }
}
