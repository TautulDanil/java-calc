import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Stack;

public class Calculator extends JFrame {
    JButton digits[] = {
            new JButton(" 0 "),
            new JButton(" 1 "),
            new JButton(" 2 "),
            new JButton(" 3 "),
            new JButton(" 4 "),
            new JButton(" 5 "),
            new JButton(" 6 "),
            new JButton(" 7 "),
            new JButton(" 8 "),
            new JButton(" 9 ")
            };

    JButton operators[] = {
            new JButton(" + "),
            new JButton(" - "),
            new JButton(" * "),
            new JButton(" / "),
            new JButton(" = "),
            new JButton(" C "),
            new JButton(" ( "),
            new JButton(" ) "),
            new JButton(" . ")
    };

    String oper_values[] = {"+", "-", "*", "/", "=", "", "(", ")", "."};

    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setSize(280, 220);
        calculator.setTitle(" Java-Calc, PP Lab1 ");
        calculator.setResizable(true);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator() {
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (int i = 0; i < 10; i++)
            buttonpanel.add(digits[i]);

        for (int i = 0; i < 9; i++)
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            digits[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    area.append(Integer.toString(finalI));
                }
            });
        }

        for (int i = 0; i < 9; i++) {
            int finalI = i;
            operators[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (finalI == 5) {
                        area.setText("");
                    } else if (finalI == 4) {
                        String expression = area.getText();
                        try {
                            double result = evaluate(expression);
                            if (result == (long) result)
                                area.append(" = " + (long) result);
                            else
                                area.append(" = " + result);
                        } catch (Exception e) {
                            area.setText(" no never not good!");
                        }
                    } else {
                        area.append(oper_values[finalI]);
                    }
                }
            });
        }
    }
    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private String toRPN(String expression) {
        StringBuilder output = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                while (i < expression.length() &&
                       (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    output.append(expression.charAt(i));
                    i++;
                }
                output.append(' ');
                continue;
            } else if (c == '(') {
                operatorStack.push(c);
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    output.append(operatorStack.pop()).append(' ');
                }
                if (!operatorStack.isEmpty()) operatorStack.pop();
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!operatorStack.isEmpty() &&
                       precedence(operatorStack.peek()) >= precedence(c)) {
                    output.append(operatorStack.pop()).append(' ');
                }
                operatorStack.push(c);
            }
            i++;
        }

        while (!operatorStack.isEmpty()) {
            output.append(operatorStack.pop()).append(' ');
        }

        return output.toString().trim();
    }

    private double evaluateRPN(String rpn) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = rpn.split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            try {
                stack.push(Double.parseDouble(token));
            } catch (NumberFormatException e) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                    case '*': stack.push(a * b); break;
                    case '/':
                        if (b == 0) throw new ArithmeticException("Impartire la zero!");
                        stack.push(a / b); break;
                }
            }
        }
        return stack.pop();
    }
    public double evaluate(String expression) {
        String rpn = toRPN(expression);
        return evaluateRPN(rpn);
    }
}