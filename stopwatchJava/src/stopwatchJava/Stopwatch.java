package stopwatchJava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Stopwatch extends JFrame {

    private long startTime = 0;
    private long countdownTime = 0;
    private boolean running = false;
    private boolean isCountdown = false;
    private Timer timer;
    private JLabel timeLabel;
    private JButton startButton;
    private JButton stopButton;
    private JButton resetButton;
    private JButton setCountdownButton;
    private JButton setStopwatchButton;

    public Stopwatch() {
        setTitle("Stopwatch");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        timeLabel = new JLabel("00:00:00.000");
        timeLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        add(timeLabel);

        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimer();
            }
        });
        add(startButton);

        stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopTimer();
            }
        });
        add(stopButton);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTimer();
            }
        });
        add(resetButton);

        setCountdownButton = new JButton("Set Countdown");
        setCountdownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCountdown();
            }
        });
        add(setCountdownButton);

        setStopwatchButton = new JButton("Set Stopwatch");
        setStopwatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setStopwatch();
            }
        });
        add(setStopwatchButton);

        timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
    }

    private void startTimer() {
        if (!running) {
            startTime = System.currentTimeMillis();
            running = true;
            timer.start();
        }
    }

    private void stopTimer() {
        if (running) {
            running = false;
            timer.stop();
        }
    }

    private void resetTimer() {
        stopTimer();
        startTime = 0;
        countdownTime = 0;
        isCountdown = false;
        timeLabel.setText("00:00:00.000");
    }

    private void setCountdown() {
        String input = JOptionPane.showInputDialog(this, "Enter countdown time in seconds:");
        try {
            countdownTime = Integer.parseInt(input) * 1000;
            isCountdown = true;
            running = false;
            timer.stop();
            timeLabel.setText(String.format("%02d:%02d:%02d.%03d", 0, 0, Integer.parseInt(input), 0));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number.");
        }
    }

    private void setStopwatch() {
        isCountdown = false;
        resetTimer();
    }

    private void updateTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        if (isCountdown) {
            long remainingTime = countdownTime - elapsedTime;

            if (remainingTime <= 0 && running) {
                running = false;
                timer.stop();
                remainingTime = 0;
                JOptionPane.showMessageDialog(this, "Countdown finished!");
            }

            long hours = (remainingTime / 3600000) % 24;
            long minutes = (remainingTime / 60000) % 60;
            long seconds = (remainingTime / 1000) % 60;
            long milliseconds = remainingTime % 1000;

            timeLabel.setText(String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds));
        } else {
            long hours = (elapsedTime / 3600000) % 24;
            long minutes = (elapsedTime / 60000) % 60;
            long seconds = (elapsedTime / 1000) % 60;
            long milliseconds = elapsedTime % 1000;

            timeLabel.setText(String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Stopwatch().setVisible(true);
            }
        });
    }
}




