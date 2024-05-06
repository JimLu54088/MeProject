class CalculatorImp {

    fun evaluateExpression(expression: String): Double {
        var exp = expression
        exp = exp.replace("e", Math.E.toString())
        exp = exp.replace("pi", Math.PI.toString())

        val operands = ArrayDeque<Double>()
        val operators = ArrayDeque<Char>()

        var i = 0
        while (i < exp.length) {
            val ch = exp[i]
            when {
                ch == ' ' -> {
                    i++
                    continue
                }

                ch == '(' -> operators.addFirst(ch)
                Character.isDigit(ch) || ch == '.' -> {
                    val sb = StringBuilder()
                    var j = i
                    while (j < exp.length && (Character.isDigit(exp[j]) || exp[j] == '.')) {
                        sb.append(exp[j])
                        j++
                    }
                    operands.addFirst(sb.toString().toDouble())
                    i = j
                    i-- // 回退一步，因为最后一次循环已经将 i 递增了
                }

                ch == '-' -> {
                    if (i == 0 || exp[i - 1] == '(') {
                        val sb = StringBuilder("-")
                        var j = i + 1
                        while (j < exp.length && (Character.isDigit(exp[j]) || exp[j] == '.')) {
                            sb.append(exp[j])
                            j++
                        }
                        operands.addFirst(sb.toString().toDouble())
                        i = j
                        i-- // 回退一步，因为最后一次循环已经将 i 递增了
                    } else {
                        while (!operators.isEmpty() && precedence(ch) <= precedence(operators.first())) {
                            evaluate(operands, operators)
                        }
                        operators.addFirst(ch)
                    }
                }

                ch == ')' -> {
                    while (!operators.isEmpty() && operators.first() != '(') {
                        evaluate(operands, operators)
                    }
                    operators.removeFirst() // Pop '('
                }

                else -> {
                    while (!operators.isEmpty() && precedence(ch) <= precedence(operators.first())) {
                        evaluate(operands, operators)
                    }
                    operators.addFirst(ch)
                }
            }
            i++
        }


        while (!operators.isEmpty()) {
            evaluate(operands, operators)
        }

        return operands.removeFirst()
    }

    private fun evaluate(operands: ArrayDeque<Double>, operators: ArrayDeque<Char>) {
        val b = operands.removeFirst()
        val a = operands.removeFirst()
        val operator = operators.removeFirst()
        when (operator) {
            '+' -> operands.addFirst(a + b)
            '-' -> operands.addFirst(a - b)
            '*' -> operands.addFirst(a * b)
            '/' -> {
                if (b == 0.0) {
                    throw ArithmeticException("devided cannot be zero")
                }
                operands.addFirst(a / b)
            }

            '^' -> operands.addFirst(Math.pow(a, b))
            else -> throw ArithmeticException("Wrong inputed operator")
        }
    }

    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            '^' -> 3
            else -> -1
        }
    }


//    fun main() {
////    val scanner = Scanner(System.`in`)
////    println("請輸入表達式：")
//        val expression = "2+3*8"
//
//        try {
//            val result = evaluateExpression(expression)
//            println("結果：$result")
//        } catch (e: Exception) {
//            println("錯誤：${e.message}")
//        }
//    }
}