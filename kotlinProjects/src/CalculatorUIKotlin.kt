import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.*

class CalculatorUIKotlin {
    init {
        val frame = JFrame("Jim's Calculator")
        val panel = JPanel()
        val textArea = JTextField(10)

        textArea.font = Font("Arial", Font.PLAIN, 80)

        val buttons = arrayOf(
            "7", "8", "9", "/", "C",
            "4", "5", "6", "*", "(",
            "1", "2", "3", "-", ")",
            "0", ".", "e", "+", "^",
            "π"
        )

        panel.layout = GridLayout(5, 5, 5, 5)

        for (buttonText in buttons) {
            val button = JButton(buttonText)
            button.font = Font("Arial", Font.PLAIN, 20)
            button.addActionListener { e ->
                val action = e.source as JButton
                val buttonText = action.text
                when (buttonText) {
                    "C" -> textArea.text = ""  // 清空文本区域
                    "π" -> textArea.text = textArea.text + "pi"  // 添加π
                    else -> textArea.text = textArea.text + buttonText  // 其他按钮，直接追加到文本区域
                }
            }
            panel.add(button)
        }

        val equalsButton = JButton("=")
        val imp = CalculatorImp()
        equalsButton.font = Font("Arial", Font.PLAIN, 20)
        equalsButton.addActionListener { e ->
            try {
                val result = imp.evaluateExpression(textArea.text)
                textArea.text = result.toString()

            } catch (ex: NoSuchElementException) {
                textArea.text = "Syntax error"
            } catch (ex: NumberFormatException) {
                textArea.text = "Syntax error"
            } catch (ex: Exception) {
                ex.printStackTrace()
                textArea.text = "Error ${ex.message}"
            }


        }

        // 将文本区域的键盘事件监听器改为监听键盘按下事件
        textArea.addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent) {
                // 如果按下的键是回车键，触发等号按钮的点击事件
                if (e.keyCode == KeyEvent.VK_ENTER) {
                    equalsButton.doClick()
                } else if (e.keyCode == KeyEvent.VK_DELETE) {
                    textArea.text = "" // 按下 Delete 键时，清空文本区域
                }
            }
        })
        panel.add(equalsButton)

        frame.contentPane.add(BorderLayout.NORTH, textArea)
        frame.contentPane.add(BorderLayout.CENTER, panel)

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(600, 600)
        frame.isVisible = true
    }


}

fun main() {
    SwingUtilities.invokeLater { CalculatorUIKotlin() }
}
