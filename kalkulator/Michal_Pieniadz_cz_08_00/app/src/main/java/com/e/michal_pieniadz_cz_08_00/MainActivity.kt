// Michał Pieniądz - Zrobiłem wszystko wraz z dodatkowymi :)
package com.e.michal_pieniadz_cz_08_00

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private var calculator: Calculator = Calculator()
    private var operators: String = buildString { for (en in Calculator.Operators.values()){this.append(en.op)}}
    private lateinit var equationTV: TextView
    private lateinit var resultTV: TextView
    private lateinit var button0: Button
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var button3: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var button6: Button
    private lateinit var button7: Button
    private lateinit var button8: Button
    private lateinit var button9: Button
    private lateinit var buttonPM: Button
    private lateinit var buttonDOT: Button
    private lateinit var buttonSUM: Button
    private lateinit var buttonADD: Button
    private lateinit var buttonSUB: Button
    private lateinit var buttonMULT: Button
    private lateinit var buttonDIV: Button
    private lateinit var buttonBCL: Button
    private lateinit var buttonBOP: Button
    private lateinit var buttonPercent: Button
    private lateinit var buttonSqrt: Button
    private lateinit var buttonBack: Button
    private lateinit var buttonClear: Button

    private var equation = "0"
        set(value){
            field = value
            equationTV.text = value
        }
        get(){
            return equationTV.text.toString()
        }
    private var result = "0"
        set(value){
            field = value
            resultTV.text = value
        }
        get(){
            return resultTV.text.toString()
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connectObjects()
        initializeListeners()
    }


    private fun connectObjects() {
        equationTV = findViewById(R.id.equation)
        resultTV = findViewById(R.id.result)
        button0 = findViewById(R.id.btn_0)
        button1 = findViewById(R.id.btn_1)
        button2 = findViewById(R.id.btn_2)
        button3 = findViewById(R.id.btn_3)
        button4 = findViewById(R.id.btn_4)
        button5 = findViewById(R.id.btn_5)
        button6 = findViewById(R.id.btn_6)
        button7 = findViewById(R.id.btn_7)
        button8 = findViewById(R.id.btn_8)
        button9 = findViewById(R.id.btn_9)
        buttonPM = findViewById(R.id.btn_pm)
        buttonDOT = findViewById(R.id.btn_dot)
        buttonSUM = findViewById(R.id.btn_sum)
        buttonADD = findViewById(R.id.btn_add)
        buttonSUB = findViewById(R.id.btn_sub)
        buttonMULT = findViewById(R.id.btn_mult)
        buttonDIV = findViewById(R.id.btn_div)
        buttonBCL = findViewById(R.id.btn_bcl)
        buttonBOP = findViewById(R.id.btn_bop)
        buttonPercent = findViewById(R.id.btn_percent)
        buttonSqrt = findViewById(R.id.btn_sqrt)
        buttonBack = findViewById(R.id.btn_backspace)
        buttonClear = findViewById(R.id.btn_clear)
    }

    private fun initializeListeners() {
        button0.setOnClickListener { addNumber("0"); calculate() }
        button1.setOnClickListener { addNumber("1"); calculate() }
        button2.setOnClickListener { addNumber("2"); calculate() }
        button3.setOnClickListener { addNumber("3"); calculate() }
        button4.setOnClickListener { addNumber("4"); calculate() }
        button5.setOnClickListener { addNumber("5"); calculate() }
        button6.setOnClickListener { addNumber("6"); calculate() }
        button7.setOnClickListener { addNumber("7"); calculate() }
        button8.setOnClickListener { addNumber("8"); calculate() }
        button9.setOnClickListener { addNumber("9"); calculate() }

        buttonPM.setOnClickListener { opposite(); calculate() }
        buttonDOT.setOnClickListener { addDot() }
        buttonSUM.setOnClickListener { calculate(); sumClicked() }
        buttonADD.setOnClickListener { addOperator(Calculator.Operators.ADD.op) }
        buttonSUB.setOnClickListener { addMinus() }
        buttonMULT.setOnClickListener { addOperator(Calculator.Operators.MULT.op) }
        buttonDIV.setOnClickListener { addOperator(Calculator.Operators.DIV.op) }
        buttonBCL.setOnClickListener { addClBracket(); calculate() }
        buttonBOP.setOnClickListener { addOpBracket() }
        buttonPercent.setOnClickListener { addOperator(Calculator.Operators.PERCENT.op) }
        buttonSqrt.setOnClickListener { addSqrt() }
        buttonBack.setOnClickListener { backClicked(); calculate() }
        buttonClear.setOnClickListener { clearClicked() }

    }

    private fun calculate() {
        var equationStr = equation
        while (equationStr.isNotEmpty() && isItOp(equationStr.last())) {
            equationStr = equationStr.dropLast(1)
        }
        result=calculator.evaluate(equationStr)
    }

    private fun addMinus() {
        val lastCharacter = equation.last()
        if (lastCharacter == Calculator.Operators.DOT.op
                || equation.takeLast(2) == StringBuilder().append(Calculator.Operators.SUB.op).append(Calculator.Operators.SUB.op).toString()) {
            val newValue = equation.dropLast(1) + Calculator.Operators.SUB.op
            equation = newValue
        } else {
            addToEquation(Calculator.Operators.SUB.op.toString())
        }
    }

    private fun clearClicked() {
        equation = "0"
        result = "0"
    }

    private fun backClicked() {
        equation = equation.dropLast(1)
        if (equation.isEmpty()) {
            equation = "0"
        }
    }

    private fun addSqrt() {
        val specialSigns = operators.replace(Calculator.Operators.DOT.op.toString(), "").
        replace(Calculator.Operators.PERCENT.op.toString(),"").
        replace(Calculator.Operators.SQRT.op.toString(),"").
        replace(Calculator.Operators.CLOSEBRACKET.op.toString(), "")
        val lastCharacter = equation.last()
        if (specialSigns.contains(lastCharacter) || (equation.length == 1 && lastCharacter == '0')) {
            addToEquation(Calculator.Operators.SQRT.op.toString())
        }
    }


    private fun addOpBracket() {
        val specialSigns = operators.replace(Calculator.Operators.DOT.op.toString(), "").
        replace(Calculator.Operators.CLOSEBRACKET.op.toString(),"").
        replace(Calculator.Operators.PERCENT.op.toString(),"")
        val lastCharacter = equation.last()
        if (specialSigns.contains(lastCharacter) || (equation.length == 1 && lastCharacter == '0')) {
            addToEquation(Calculator.Operators.OPENBRACKET.op.toString())
        }
    }

    private fun addClBracket() {
        val lastCharacter = equation.last()
        if (isItNumber(lastCharacter) || lastCharacter == Calculator.Operators.CLOSEBRACKET.op) {
            val nOp = equation.count { it == Calculator.Operators.OPENBRACKET.op }
            val nCl = equation.count { it == Calculator.Operators.CLOSEBRACKET.op }

            if (nOp > nCl) {
                addToEquation(Calculator.Operators.CLOSEBRACKET.op.toString())
            } else if (nOp == nCl) {
                val newValue = Calculator.Operators.OPENBRACKET.op + equation + Calculator.Operators.CLOSEBRACKET.op
                equation = newValue
            }

        }
    }

    private fun isItNumber(s: Char): Boolean {
        val numbers = "0123456789"
        return numbers.contains(s)
    }

    private fun addOperator(s: Char) {
        val lastCharacter = equation.last()
        if (isItOp(lastCharacter) && lastCharacter != Calculator.Operators.SUB.op) {
            val newValue = equation.dropLast(1) + s
            equation = newValue
        } else if(lastCharacter == Calculator.Operators.SUB.op){
            val secondLastCharacter = equation.takeLast(2).first()
            if(isItOp(secondLastCharacter)){
                val newValue = equation.dropLast(2) + s
                equation = newValue
            }else{
                val newValue = equation.dropLast(1) + s
                equation = newValue
            }
        } else if (isItNumber(lastCharacter) || lastCharacter == Calculator.Operators.CLOSEBRACKET.op) {
            val newValue = equation + s
            equation = newValue
        }
    }

    private fun isItOp(s: Char): Boolean {
        return operators.replace(Calculator.Operators.CLOSEBRACKET.op.toString(),"").contains(s)
    }

    private fun sumClicked() {
        equation = result
    }

    private fun addDot() {
        val lastCharacter = equation.last()
        if (isItNumber(lastCharacter) && !isDotInLastNumber(equation)) {
            val newValue = equation + Calculator.Operators.DOT.op
            equation = newValue
        }
    }

    private fun isDotInLastNumber(text: String): Boolean {
        val ops = operators.replace(Calculator.Operators.DOT.op.toString(), "")
        for (sign in text.reversed()) {
            if (ops.contains(sign)) {
                return false
            }
            if (sign == Calculator.Operators.DOT.op) {
                return true
            }
        }
        return false
    }

    private fun opposite() {
        if (equation.take(2) == StringBuilder().append(Calculator.Operators.SUB.op).
                append(Calculator.Operators.OPENBRACKET.op).toString()
                && equation.last() == Calculator.Operators.CLOSEBRACKET.op) {
            val newValue = equation.drop(2).dropLast(1)
            equation = newValue
        } else {
            val newValue = StringBuilder().append(Calculator.Operators.SUB.op).
            append(Calculator.Operators.OPENBRACKET.op).
            append(equation).
            append(Calculator.Operators.CLOSEBRACKET.op).toString()
            equation = newValue
        }

    }

    private fun addNumber(sign: String){
        val lastCharacter = equation.last()
        if(isItNumber(sign.toCharArray().first()) && lastCharacter != Calculator.Operators.CLOSEBRACKET.op){
            addToEquation(sign)
        }
    }

    private fun addToEquation(sign: String) {
        if (equation == "0") {
            equation = sign
        } else {
            val newValue = equation + sign
            equation = newValue

        }
    }

}