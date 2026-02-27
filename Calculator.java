import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.Stack;

public class Calculator extends JFrame {
    JButton cifre[] = {
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

    JButton operatori[] = {
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

    String valori_operatori[] = {"+", "-", "*", "/", "=", "", "(", ")", "."};

    JTextArea zona = new JTextArea(3, 5);

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setSize(280, 220);
        calculator.setTitle(" Calculator Java, Lab PP1 ");
        calculator.setResizable(true);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Calculator() {
        add(new JScrollPane(zona), BorderLayout.NORTH);
        JPanel panou_butoane = new JPanel();
        panou_butoane.setLayout(new FlowLayout());

        for (int i = 0; i < 10; i++)
            panou_butoane.add(cifre[i]);

        for (int i = 0; i < 9; i++)
            panou_butoane.add(operatori[i]);

        add(panou_butoane, BorderLayout.CENTER);
        zona.setForeground(Color.BLACK);
        zona.setBackground(Color.WHITE);
        zona.setLineWrap(true);
        zona.setWrapStyleWord(true);
        zona.setEditable(false);

        for (int i = 0; i < 10; i++) {
            int indiceFinal = i;
            cifre[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent eveniment) {
                    zona.append(Integer.toString(indiceFinal));
                }
            });
        }

        for (int i = 0; i < 9; i++) {
            int indiceFinal = i;
            operatori[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent eveniment) {
                    if (indiceFinal == 5) {
                        zona.setText("");
                    } else if (indiceFinal == 4) {
                        String expresie = zona.getText();
                        try {
                            double rezultat = evalueaza(expresie);
                            if (rezultat == (long) rezultat)
                                zona.append(" = " + (long) rezultat);
                            else
                                zona.append(" = " + rezultat);
                        } catch (Exception e) {
                            zona.setText(" expresie incorecta!");
                        }
                    } else {
                        zona.append(valori_operatori[indiceFinal]);
                    }
                }
            });
        }
    }

    private int prioritate(char operator) {
        if (operator == '+' || operator == '-') return 1;
        if (operator == '*' || operator == '/') return 2;
        return 0;
    }

    private String inNotatiePolaraInversa(String expresie) {
        StringBuilder iesire = new StringBuilder();
        Stack<Character> stiva_operatori = new Stack<>();

        int i = 0;
        while (i < expresie.length()) {
            char c = expresie.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                while (i < expresie.length() &&
                       (Character.isDigit(expresie.charAt(i)) || expresie.charAt(i) == '.')) {
                    iesire.append(expresie.charAt(i));
                    i++;
                }
                iesire.append(' ');
                continue;
            } else if (c == '(') {
                stiva_operatori.push(c);
            } else if (c == ')') {
                while (!stiva_operatori.isEmpty() && stiva_operatori.peek() != '(') {
                    iesire.append(stiva_operatori.pop()).append(' ');
                }
                if (!stiva_operatori.isEmpty()) stiva_operatori.pop();
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!stiva_operatori.isEmpty() &&
                       prioritate(stiva_operatori.peek()) >= prioritate(c)) {
                    iesire.append(stiva_operatori.pop()).append(' ');
                }
                stiva_operatori.push(c);
            }
            i++;
        }

        while (!stiva_operatori.isEmpty()) {
            iesire.append(stiva_operatori.pop()).append(' ');
        }

        return iesire.toString().trim();
    }

    private double evalueazaNotatiePolaraInversa(String npi) {
        Stack<Double> stiva = new Stack<>();
        String[] tokenuri = npi.split("\\s+");

        for (String token : tokenuri) {
            if (token.isEmpty()) continue;
            try {
                stiva.push(Double.parseDouble(token));
            } catch (NumberFormatException e) {
                double b = stiva.pop();
                double a = stiva.pop();
                switch (token.charAt(0)) {
                    case '+': stiva.push(a + b); break;
                    case '-': stiva.push(a - b); break;
                    case '*': stiva.push(a * b); break;
                    case '/':
                        if (b == 0) throw new ArithmeticException("Împărțire la zero!");
                        stiva.push(a / b); break;
                }
            }
        }
        return stiva.pop();
    }

    public double evalueaza(String expresie) {
        String npi = inNotatiePolaraInversa(expresie);
        return evalueazaNotatiePolaraInversa(npi);
    }
}
