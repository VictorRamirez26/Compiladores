package compiler.lexer;

import java.util.HashMap;
import java.util.Map;

public class TokenClassifier {

    private final Map<String, TokenType> keywords = new HashMap<>();
    private final Map<String, TokenType> specialSymbols = new HashMap<>();

    private void initKeywords() {
        keywords.put("class", TokenType.KW_CLASS);
        keywords.put("if", TokenType.KW_IF);
        keywords.put("impl", TokenType.KW_IMPL);
        keywords.put("else", TokenType.KW_ELSE);
        keywords.put("false", TokenType.KW_FALSE);
        keywords.put("fn", TokenType.KW_FN);
        keywords.put("ret", TokenType.KW_RET);
        keywords.put("while", TokenType.KW_WHILE);
        keywords.put("true", TokenType.KW_TRUE);
        keywords.put("new", TokenType.KW_NEW);
        keywords.put("nil", TokenType.KW_NIL);
        keywords.put("st", TokenType.KW_ST);
        keywords.put("self", TokenType.KW_SELF);
        keywords.put("pub", TokenType.KW_PUB);
        keywords.put("div", TokenType.KW_DIV);
    }

    /*private void initSpecialSymbols {
        //specialSymbols.put("{", TokenType.)
    }*/

    public TokenClassifier() {
        initKeywords();
    }

    public Token classifyToken(Lexeme lexeme){

        String data = lexeme.getData();

        if (data.isEmpty()){
            return null;
        }

        char c = data.charAt(0);

        if (Character.isLowerCase(c)) {
            return classifyKeywordOrIdentifier(lexeme);
        } else if (Character.isUpperCase(c)) {
            return classifyClassIdentifier(lexeme);
        } else if (Character.isDigit(c)) {
            return classifyIntOrDouble(lexeme);
        }

        return null;
    }

    private Token classifyIntOrDouble(Lexeme lexeme){
        Token token = setValues(new Token() , lexeme);
        token.setTokenType(TokenType.INT_CONSTANT); // Por default es INT
        String data = lexeme.getData();
        int size = data.length();

        for (int i=0 ; i < size; i++){
            if (i+1 < size) {
                if (data.charAt(i) == '.' && Character.isDigit(data.charAt(i+1))) { // Pasa a ser DOUBLE
                    token.setTokenType(TokenType.DOUBLE_CONSTANT);
                    break;
                }
            }
        }

        return token;
    }
    private Token classifyClassIdentifier(Lexeme lexeme){
        Token token = setValues(new Token() , lexeme);
        token.setTokenType(TokenType.IDENTIFIER_CLASS);
        return token;
    }
    private Token classifyKeywordOrIdentifier(Lexeme lexeme){
        Token token = setValues(new Token() , lexeme);
        token.setTokenType(keywords.getOrDefault(lexeme.getData(), TokenType.IDENTIFIER_OBJECT));
        return token;
    }

    private Token setValues(Token token, Lexeme lexeme){
        token.setValue(lexeme.getData());
        token.setLine(lexeme.getLineIndex());
        token.setColumn(lexeme.getColumnIndex());
        return token;
    }

}
