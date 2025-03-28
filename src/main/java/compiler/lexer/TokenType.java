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
    STRING_CONSTANT,
    BOOLEAN,

    SPECIAL_SYMBOL_LCB, //Left curly brace {
    SPECIAL_SYMBOL_RCB, //Right curly brace }
    SPECIAL_SYMBOL_PO, //Parenthesis open (
    SPECIAL_SYMBOL_PC, //Parenthesis close )
    SPECIAL_SYMBOL_SCO, //Simple comment open //
    SPECIAL_SYMBOL_MCO, //Multiple comment open /*
    SPECIAL_SYMBOL_MCC, //Multiple comment close */
    SPECIAL_SYMBOL_S, //Semicolon ;
    SPECIAL_SYMBOL_C, //Comma ,
    SPECIAL_SYMBOL_P, //Period .

    OPERATOR_E, //Equals =
    OPERATOR_EC, //Equals comparator ==
}
