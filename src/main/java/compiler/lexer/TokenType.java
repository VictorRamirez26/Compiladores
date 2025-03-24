package compiler.lexer;

public enum TokenType {

    KW_CLASS,
    KW_IF,
    KW_IMPL,
    KW_ELSE,
    KW_FALSE,
    KW_FN,
    KW_RET,
    KW_WHILE,
    KW_TRUE,
    KW_NEW,
    KW_NIL,
    KW_ST,
    KW_SELF,
    KW_PUB,
    KW_DIV,


    IDENTIFIER_CLASS,
    IDENTIFIER_OBJECT,

    INT_CONSTANT,
    DOUBLE_CONSTANT,
    OPERATOR,
    SPECIAL_SYMBOL,
    STRING_CONSTANT,
    BOOLEAN
}
