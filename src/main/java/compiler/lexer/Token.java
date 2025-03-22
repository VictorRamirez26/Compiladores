package compiler.lexer;

public class Token {
    private TokenType tokenType;
    private String value;
    private int line;
    private int column;

    public Token() {
    }

    public Token(TokenType tokenType, String value, int line, int column) {
        this.tokenType = tokenType;
        this.value = value;
        this.line = line;
        this.column = column;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

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
