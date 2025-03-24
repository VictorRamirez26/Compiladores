import compiler.lexer.Lexeme;
import compiler.lexer.Token;
import compiler.lexer.TokenClassifier;
import compiler.lexer.TokenType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TokenClassifierTest {
    private TokenClassifier tokenClassifier;

    @BeforeEach
    void setUp() {
        tokenClassifier = new TokenClassifier();
    }

    @Test
    void testKeywordClassification() {
        Lexeme lexeme = new Lexeme();
        lexeme.setData("if");
        lexeme.setColumnIndex(1);
        lexeme.setLineIndex(5);
        Token token = tokenClassifier.classifyToken(lexeme);

        assertNotNull(token);
        assertEquals(TokenType.KW_IF, token.getTokenType());
        assertEquals("if", token.getValue());
    }

    @Test
    void testIdentifierObject() {
        Lexeme lexeme = new Lexeme();
        lexeme.setData("variable");
        lexeme.setColumnIndex(2);
        lexeme.setLineIndex(3);
        Token token = tokenClassifier.classifyToken(lexeme);

        assertNotNull(token);
        assertEquals(TokenType.IDENTIFIER_OBJECT, token.getTokenType());
        assertEquals("variable", token.getValue());
    }

    @Test
    void testClassIdentifier() {
        Lexeme lexeme = new Lexeme();
        lexeme.setData("MyClass");
        lexeme.setColumnIndex(3);
        lexeme.setLineIndex(4);
        Token token = tokenClassifier.classifyToken(lexeme);

        assertNotNull(token);
        assertEquals(TokenType.IDENTIFIER_CLASS, token.getTokenType());
        assertEquals("MyClass", token.getValue());
    }

    @Test
    void testIntegerConstant() {
        Lexeme lexeme = new Lexeme();
        lexeme.setData("1234");
        lexeme.setColumnIndex(4);
        lexeme.setLineIndex(1);
        Token token = tokenClassifier.classifyToken(lexeme);

        assertNotNull(token);
        assertEquals(TokenType.INT_CONSTANT, token.getTokenType());
        assertEquals("1234", token.getValue());
    }

    @Test
    void testDoubleConstant() {
        Lexeme lexeme = new Lexeme();
        lexeme.setData("3.14");
        lexeme.setColumnIndex(5);
        lexeme.setLineIndex(2);
        Token token = tokenClassifier.classifyToken(lexeme);

        assertNotNull(token);
        assertEquals(TokenType.DOUBLE_CONSTANT, token.getTokenType());
        assertEquals("3.14", token.getValue());
    }

    @Test
    void testEmptyLexeme() {
        Lexeme lexeme = new Lexeme();
        lexeme.setData("");
        lexeme.setColumnIndex(6);
        lexeme.setLineIndex(1);
        Token token = tokenClassifier.classifyToken(lexeme);

        assertNull(token);
    }

}
