import compiler.exceptions.LexerException;
import compiler.lexer.Lexer;
import compiler.lexer.Token;
import compiler.lexer.TokenType;
import compiler.reader.FileManager;
import org.junit.jupiter.api.Test;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    private Token token;
    private Lexer lexer;
    private FileManager fileManager;


    public void  iniciarArchivo(String path) throws FileNotFoundException {
        token = new Token();
        fileManager = new FileManager(path);
        lexer = new Lexer(fileManager);
    }

    @Test
    public void testAssignment() throws LexerException, FileNotFoundException {

        iniciarArchivo(".\\tests\\asignacion.txt");

        List<Token> tokenList = lexer.getAllTokens();

        assertTrue(tokenList.size() >= 4, "Se esperaban al menos 4 tokens");

        assertEquals(TokenType.IDENTIFIER_OBJECT, tokenList.get(0).getTokenType());
        assertEquals("a", tokenList.get(0).getLexeme().getData());
        assertEquals(1, tokenList.get(0).getLexeme().getLineIndex());
        assertEquals(1, tokenList.get(0).getLexeme().getColumnIndex());

        assertEquals(TokenType.OPERATOR_ASSIGN, tokenList.get(1).getTokenType());
        assertEquals("=", tokenList.get(1).getLexeme().getData());
        assertEquals(1, tokenList.get(1).getLexeme().getLineIndex());
        assertEquals(3, tokenList.get(1).getLexeme().getColumnIndex());

        assertEquals(TokenType.STRING_CONSTANT, tokenList.get(2).getTokenType());
        assertEquals("Hola", tokenList.get(2).getLexeme().getData());
        assertEquals(1, tokenList.get(2).getLexeme().getLineIndex());
        assertEquals(5, tokenList.get(2).getLexeme().getColumnIndex());

        assertEquals(TokenType.SPECIAL_SYMBOL_S, tokenList.get(3).getTokenType());
        assertEquals(";", tokenList.get(3).getLexeme().getData());
        assertEquals(1, tokenList.get(3).getLexeme().getLineIndex());
        assertEquals(11, tokenList.get(3).getLexeme().getColumnIndex());

    }

    @Test
    public void testNumbers() throws LexerException, FileNotFoundException {
        iniciarArchivo(".\\tests\\numeros.txt");
        List<Token> tokenList = lexer.getAllTokens();

        assertTrue(tokenList.size() >= 6, "Se esperaban al menos 6 tokens");

        assertEquals(TokenType.INT_CONSTANT, tokenList.get(0).getTokenType());
        assertEquals("3", tokenList.get(0).getLexeme().getData());

        assertEquals(TokenType.DOUBLE_CONSTANT, tokenList.get(1).getTokenType());
        assertEquals("3.3", tokenList.get(1).getLexeme().getData());

        assertEquals(TokenType.UNDEFINED, tokenList.get(2).getTokenType());
        assertEquals("3.3e", tokenList.get(2).getLexeme().getData());

        assertEquals(TokenType.DOUBLE_CONSTANT, tokenList.get(3).getTokenType());
        assertEquals("3.3e10", tokenList.get(3).getLexeme().getData());

        assertEquals(TokenType.DOUBLE_CONSTANT, tokenList.get(4).getTokenType());
        assertEquals("3.3e-10", tokenList.get(4).getLexeme().getData());

        assertEquals(TokenType.UNDEFINED, tokenList.get(5).getTokenType());
        assertEquals("3.3e-", tokenList.get(5).getLexeme().getData());

    }

    @Test
    public void testComments() throws LexerException, FileNotFoundException {
        iniciarArchivo(".\\tests\\comentarios.txt");
        List<Token> tokenList = lexer.getAllTokens();

        assertTrue(tokenList.isEmpty(), "No se esperaba ningún Token.");

    }

    @Test
    public void testKeywords() throws LexerException, IOException {
        iniciarArchivo(".\\tests\\keywords.txt");

        List<Token> tokenList = lexer.getAllTokens();

        List<TokenType> expectedTypes = List.of(
                TokenType.KW_CLASS, TokenType.KW_IF, TokenType.KW_IMPL, TokenType.KW_ELSE,
                TokenType.KW_FALSE, TokenType.KW_FN, TokenType.KW_RET, TokenType.KW_WHILE,
                TokenType.KW_TRUE, TokenType.KW_NEW, TokenType.KW_NIL, TokenType.KW_ST,
                TokenType.KW_START, TokenType.KW_SELF, TokenType.KW_PUB, TokenType.KW_DIV
        );

        List<String> expectedLexemes = List.of(
                "class", "if", "impl", "else", "false", "fn", "ret", "while",
                "true", "new", "nil", "st", "start", "self", "pub", "div"
        );

        assertEquals(expectedTypes.size(), tokenList.size(), "Cantidad de tokens inesperada");

        for (int i = 0; i < expectedTypes.size(); i++) {
            assertEquals(expectedTypes.get(i), tokenList.get(i).getTokenType(), "Token type en posición " + i + " incorrecto");
            assertEquals(expectedLexemes.get(i), tokenList.get(i).getLexeme().getData(), "Lexema en posición " + i + " incorrecto");
        }
    }

}
