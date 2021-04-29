// Michał Pieniądz - Zrobiłem wszystko wraz z dodatkowymi :)
package com.e.michal_pieniadz_cz_08_00

import android.graphics.Path
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.math.sqrt

class Calculator: CalculatorInterface {
    enum class Operators(val op: Char){
        MULT('*'),
        DIV('/'),
        ADD('+'),
        SUB('-'),
        PERCENT('%'),
        SQRT('√'),
        OPENBRACKET('('),
        CLOSEBRACKET(')'),
        DOT('.')
    }
    private fun deleteUselessBrackets(equation_: String): String {
        var equation = equation_
        val nOp = equation.count { it == Operators.OPENBRACKET.op }
        if (nOp > 0) {
            val opInds = (equation.withIndex().filter { it.value == Operators.OPENBRACKET.op }.map { it.index }).toMutableList()
            val clInds = (equation.withIndex().filter { it.value == Operators.CLOSEBRACKET.op }.map { it.index }).toMutableList()

            for (opInd in opInds.asReversed()) {
                var noBracket = true
                for (clInd in clInds) {
                    if (opInd < clInd) {
                        clInds.remove(clInd)
                        noBracket = false
                        break
                    }
                }
                if (noBracket) {
                    equation = equation.replaceRange(opInd, opInd + 1, "")
                }
            }
        }
        return equation
    }


    private fun isThereBracketEquation(equation_: String): Boolean {
        return equation_.count { it == Operators.OPENBRACKET.op } > 0
    }


    override fun evaluate(equation_: String): String {
        var equation = equation_
        try {
            equation = deleteUselessBrackets(equation)

            while (isThereBracketEquation(equation)) {
                val opbInd = equation.lastIndexOf(Operators.OPENBRACKET.op)
                var clbInd = opbInd + 1
                while (equation[clbInd] != Operators.CLOSEBRACKET.op) {
                    clbInd++
                }
                if (opbInd < clbInd) {
                    val toReplace = equation.substring(opbInd + 1, clbInd)
                    val replaced = evaluateSimple(toReplace)
                    equation = equation.replace(Operators.OPENBRACKET.op +toReplace + Operators.CLOSEBRACKET.op, replaced)
                }
            }
            equation = evaluateSimple(equation)
        } catch (e: Exception) {
            equation = "BŁĄD"
        }
        return equation
    }

    override fun evaluateSimple(equation_: String): String {
//        val signs = "√%*/-+"
        val signs = StringBuilder().append(Operators.SQRT.op).append(Operators.PERCENT.op).
        append(Operators.MULT.op).append(Operators.DIV.op).
        append(Operators.SUB.op).append(Operators.ADD.op).toString()
        val sqrtSign = '\u221a'
        var equation = equation_

        for (s in signs.indices) {
            while (equation.count { it == signs[s] } > 0) {
                if (signs[s] != sqrtSign) {
                    if (signs[s] == Operators.SUB.op && equation.count { it == signs[s] } == 1 && equation.indexOf(signs[s]) == 0) {
                        break
                    }
                    equation = evaluateSimpleBinary(equation, signs[s], signs)
                } else {
                    equation = evaluateSimpleSingle(equation, signs[s], signs)
                }
            }
        }

        return equation
    }

    override fun evaluateSimpleSingle(equation: String, op: Char, ops: String): String {
        val middleInd = equation.indexOf(op)
        var rightInd = middleInd + 1

        while ((!ops.contains(equation[rightInd])
                        || (rightInd == middleInd + 1
                        && equation[rightInd] == Operators.SUB.op)) && rightInd + 1 < equation.length) {
            rightInd++
        }
        if (rightInd == equation.length - 1) {
            rightInd++
        }

        var result = 0.0f
        val rightSide = equation.substring(middleInd + 1, rightInd).toFloat()
        when (op) {
            Operators.SQRT.op -> result = sqrt(rightSide)
        }

        return equation.replaceRange(middleInd, rightInd, result.toString())
    }

    override fun evaluateSimpleBinary(equation: String, op_: Char, ops: String): String {
        var op = op_
        var middleInd = equation.indexOf(op)
        if (middleInd == 0) { // '-' case
            middleInd++
            while (equation[middleInd] != Operators.SUB.op && middleInd + 1 < equation.length) {
                middleInd++
            }
        }
        var leftInd = middleInd - 1
        var rightInd = middleInd + 1

        while ((!ops.contains(equation[leftInd])
                        || (leftInd == 0 && equation[leftInd] == Operators.SUB.op)
                        || (equation[leftInd] == Operators.ADD.op && equation[leftInd + 1] == Operators.SUB.op))
                && leftInd > 0) {
            if (op == Operators.SUB.op && (equation[leftInd] == Operators.ADD.op && equation[leftInd + 1] == Operators.SUB.op)) {
                middleInd--
                op = Operators.ADD.op
            }
            leftInd--
        }
        while ((!ops.contains(equation[rightInd])
                        || (rightInd == middleInd + 1
                        && equation[rightInd] == Operators.SUB.op))
                && rightInd + 1 < equation.length) {
            rightInd++
        }
        if (leftInd == 0) {
            leftInd--
        }
        if (rightInd == equation.length - 1) {
            rightInd++
        }

        var result = 0.0f
        val leftSide = equation.substring(leftInd + 1, middleInd).toFloat()
        val rightSide = equation.substring(middleInd + 1, rightInd).toFloat()
        when (op) {
            Operators.MULT.op -> result = leftSide * rightSide
            Operators.DIV.op -> result = leftSide / rightSide
            Operators.ADD.op -> result = leftSide + rightSide
            Operators.SUB.op -> result = leftSide - rightSide
            Operators.PERCENT.op -> result = leftSide * rightSide / 100
        }

        return equation.replaceRange(leftInd + 1, rightInd, result.toString())
    }
}
