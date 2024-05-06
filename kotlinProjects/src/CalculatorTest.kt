import java.awt.BorderLayout
import javax.swing.*

class CalculatorTest {
    init {
        val frame = JFrame("Hello World")
        val button = JButton("Click Me")
        val label = JLabel("")

        button.addActionListener {
            label.text = "Button Clicked"
        }

        frame.layout = BorderLayout()
        frame.add(button, BorderLayout.NORTH)
        frame.add(label, BorderLayout.CENTER)

        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.setSize(300, 200)
        frame.isVisible = true
    }
}

fun main() {
    SwingUtilities.invokeLater { CalculatorTest() }
}
