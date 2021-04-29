package com.e.michal_pieniadz_cz_08_00

interface CalculatorInterface {
    public fun evaluate(equation_: String): String
    public fun evaluateSimple(equation_: String): String
    public fun evaluateSimpleSingle(equation: String, op: Char, ops: String): String
    public fun evaluateSimpleBinary(equation: String, op_: Char, ops: String): String
}