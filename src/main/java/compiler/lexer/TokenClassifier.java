package compiler.lexer;

import java.util.HashMap;
import java.util.Map;

public class TokenClassifier {

    private final Map<String, TokenType> keywords = new HashMap<>();
    private final Map<String, TokenType> specialSymbols = new HashMap<>();
    private final Map<String, TokenType> operators = new HashMap<>();

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
        keywords.put("start", TokenType.KW_START);
        keywords.put("self", TokenType.KW_SELF);
        keywords.put("pub", TokenType.KW_PUB);
        keywords.put("div", TokenType.KW_DIV);
    }

    private void initSpecialSymbols() {
        specialSymbols.put("{", TokenType.SPECIAL_SYMBOL_LCB);
        specialSymbols.put("}", TokenType.SPECIAL_SYMBOL_RCB);
        specialSymbols.put("(", TokenType.SPECIAL_SYMBOL_PO);
        specialSymbols.put(")", TokenType.SPECIAL_SYMBOL_PC);
        specialSymbols.put("/", TokenType.SPECIAL_SYMBOL_SCO);
        specialSymbols.put("//", TokenType.SPECIAL_SYMBOL_SCO);
        specialSymbols.put("/*", TokenType.SPECIAL_SYMBOL_MCO);
        specialSymbols.put("*/", TokenType.SPECIAL_SYMBOL_MCC);
        specialSymbols.put(";", TokenType.SPECIAL_SYMBOL_S);
        specialSymbols.put(",", TokenType.SPECIAL_SYMBOL_C);
        specialSymbols.put(".", TokenType.SPECIAL_SYMBOL_P);
    }

    private void initOperators() {
        operators.put("=", TokenType.OPERATOR_E);
    }

    public TokenClassifier() {
        initKeywords();
        initSpecialSymbols();
    }

    public Map<String, TokenType> getKeywords() {
        return keywords;
    }

    public Map<String, TokenType> getSpecialSymbols() {

        return specialSymbols;
    }

    public Map<String, TokenType> getOperators() {
        return operators;
    }
}
