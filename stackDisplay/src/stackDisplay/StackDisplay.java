package stackDisplay;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class StackDisplay extends JFrame {

	private JTextArea stackDisplay;
    private JTextField topDisplay;
    private Stack<Integer> stack;
    private Random random;

    public StackDisplay() {
        setTitle("Stack Display");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        stack = new Stack<>();
        random = new Random();

        JPanel topPanel = new JPanel(new BorderLayout());
        topDisplay = new JTextField();
        topDisplay.setEditable(false);
        topPanel.add(topDisplay, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton pushButton = new JButton("Push");
        JButton popButton = new JButton("Pop");

        buttonPanel.add(pushButton);
        buttonPanel.add(popButton);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        stackDisplay = new JTextArea();
        stackDisplay.setEditable(false);
        add(new JScrollPane(stackDisplay), BorderLayout.CENTER);

        pushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int randomNumber = random.nextInt(999) + 1;
                stack.push(randomNumber);
                updateStackDisplay();
            }
        });

        popButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!stack.isEmpty()) {
                    int topNumber = stack.pop();
                    topDisplay.setText(String.valueOf(topNumber));
                    updateStackDisplay();
                } else {
                    stackDisplay.setText("Empty");
                }
            }
        });
    }

    private void updateStackDisplay() {
        if (stack.isEmpty()) {
            stackDisplay.setText("Empty");
        } else {
            StringBuilder display = new StringBuilder();
            for (int i = stack.size() - 1; i >= 0; i--) {
                display.append(stack.get(i)).append("\n");
            }
            stackDisplay.setText(display.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new StackDisplay().setVisible(true);
            }
        });
    }

}
