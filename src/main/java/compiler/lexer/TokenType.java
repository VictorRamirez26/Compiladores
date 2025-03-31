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
    KW_START,
    KW_SELF,
    KW_PUB,
    KW_DIV,


    IDENTIFIER_CLASS,
    IDENTIFIER_OBJECT,

    INT_CONSTANT,
    DOUBLE_CONSTANT,
    STRING_CONSTANT,
    BOOLEAN,

    SPECIAL_SYMBOL_LCB, //Left curly brace {
    SPECIAL_SYMBOL_RCB, //Right curly brace }
    SPECIAL_SYMBOL_PO, //Parenthesis open (
    SPECIAL_SYMBOL_PC, //Parenthesis close )
    SPECIAL_SYMBOL_S, //Semicolon ;
    SPECIAL_SYMBOL_C, //Comma ,
    SPECIAL_SYMBOL_P, //Punto .


    OPERATOR_ASSIGN,
    OPERATOR_ADD,
    OPERATOR_SUB,
    OPERATOR_MUL,
    OPERATOR_DIV,
    OPERATOR_MOD,
    OPERATOR_EQ,
    OPERATOR_NEQ,
    OPERATOR_LT,
    OPERATOR_GT,
    OPERATOR_LTE,
    OPERATOR_GTE,
    OPERATOR_AND,
    OPERATOR_OR,
    OPERATOR_NOT,
    OPERATOR_INC,
    OPERATOR_DEC
}
