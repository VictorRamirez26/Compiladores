package compiler.lexer;

import java.util.HashMap;
import java.util.Map;

public class TokenClassifier {

    private final Map<String, TokenType> keywords = new HashMap<>();
    private final Map<Character, TokenType> specialSymbols = new HashMap<>();
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
        specialSymbols.put('{', TokenType.SPECIAL_SYMBOL_LCB);
        specialSymbols.put('}', TokenType.SPECIAL_SYMBOL_RCB);
        specialSymbols.put('(', TokenType.SPECIAL_SYMBOL_PO);
        specialSymbols.put(')', TokenType.SPECIAL_SYMBOL_PC);
        specialSymbols.put(';', TokenType.SPECIAL_SYMBOL_S);
        specialSymbols.put(',', TokenType.SPECIAL_SYMBOL_C);
        specialSymbols.put('.', TokenType.SPECIAL_SYMBOL_P);
    }

    private void initOperators() {
        // Operador de asignación
        operators.put("=", TokenType.OPERATOR_ASSIGN);   // Asignación

        // Operadores aritméticos
        operators.put("+", TokenType.OPERATOR_ADD);  // Suma
        operators.put("-", TokenType.OPERATOR_SUB);  // Resta
        operators.put("*", TokenType.OPERATOR_MUL);  // Multiplicación
        operators.put("/", TokenType.OPERATOR_DIV);  // División
        operators.put("%", TokenType.OPERATOR_MOD);  // Módulo (residuo)

        // Operadores relacionales
        operators.put("==", TokenType.OPERATOR_EQ);  // Igualdad
        operators.put("!=", TokenType.OPERATOR_NEQ); // Diferente
        operators.put("<", TokenType.OPERATOR_LT);   // Menor que
        operators.put(">", TokenType.OPERATOR_GT);   // Mayor que
        operators.put("<=", TokenType.OPERATOR_LTE); // Menor o igual que
        operators.put(">=", TokenType.OPERATOR_GTE); // Mayor o igual que

        // Operadores lógicos
        operators.put("&&", TokenType.OPERATOR_AND); // AND lógico
        operators.put("||", TokenType.OPERATOR_OR);  // OR lógico
        operators.put("!", TokenType.OPERATOR_NOT);  // NOT lógico

        // Operadores de incremento y decremento
        operators.put("++", TokenType.OPERATOR_INC); // Incremento
        operators.put("--", TokenType.OPERATOR_DEC); // Decremento

    }

    public TokenClassifier() {
        initKeywords();
        initSpecialSymbols();
        initOperators();
    }

    public Map<String, TokenType> getKeywords() {
        return keywords;
    }

    public Map<Character, TokenType> getSpecialSymbols() {
        return specialSymbols;
    }

    public Map<String, TokenType> getOperators() {
        return operators;
    }
}
