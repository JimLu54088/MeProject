import junit.framework.TestCase.assertEquals
import org.junit.Test


class CalculatorImpTest {

    @Test
    @Throws(Exception::class)
    fun performOperation_test11() {
        val imp = CalculatorImp()

        var result = imp.evaluateExpression("3+4*2")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(11.0, result, 0.0000000000001)

        result = imp.evaluateExpression("(1+1)*2")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(4.0, result, 0.0000000000001)

        result = imp.evaluateExpression("(1+1)*(1+1)")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(4.0, result, 0.0000000000001)

        result = imp.evaluateExpression("(1+1)^2")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(4.0, result, 0.0000000000001)

        result = imp.evaluateExpression("(12*3/(-2))*(3+5)/2")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(-72.0, result, 0.0000000000001)

        result = imp.evaluateExpression("pi*1")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(3.141592653589793, result, 0.0000000000001)

        result = imp.evaluateExpression("e*1")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(2.718281828459, result, 0.0000000000001)

        result = imp.evaluateExpression("e")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(2.718281828459, result, 0.0000000000001)

        result = imp.evaluateExpression("2-1")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(1.0, result, 0.0000000000001)

        result = imp.evaluateExpression("-2-1")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(-3.0, result, 0.0000000000001)

        result = imp.evaluateExpression("2*(-9)^2")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(162.0, result, 0.0000000000001)

        result = imp.evaluateExpression("(1 + 1)*5")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(10.0, result, 0.0000000000001)
        result = imp.evaluateExpression("0.3*5")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(1.5, result, 0.0000000000001)

        result = imp.evaluateExpression("2^3")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(8.0, result, 0.0000000000001)

        result = imp.evaluateExpression("1+.25*2")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(1.5, result, 0.0000000000001)

        result = imp.evaluateExpression("0+123.+2*5")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(133.0, result, 0.0000000000001)

        result = imp.evaluateExpression("(2 + 3) * (4 - 2)")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(10.0, result, 0.0000000000001)

        result = imp.evaluateExpression("-.3")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(-0.3, result, 0.0000000000001)

        result = imp.evaluateExpression("1-1-1")
        // 在这里进行断言，验证结果是否符合预期
        assertEquals(-1.0, result, 0.0000000000001)


    }


}