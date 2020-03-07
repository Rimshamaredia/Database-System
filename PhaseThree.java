import javax.swing.*;
import java.awt.Color;

public class PhaseThree extends JPanel{
    static JPanel phase3 = new JPanel();
    String opts[] = {"Largest Football Stadiums", "10 Worst Kickoffs", "10 Most Attended Games"};
    public PhaseThree(){
        phase3.setBackground(new java.awt.Color(44, 68, 101));
        phase3.setLayout(new BoxLayout(phase3, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("<html><h1 style=\"font-weight: 500;\">DataBall FootBase</h1></html>");
        title.setForeground(Color.white);
        phase3.add(title);

        queryListener qL = new queryListener(phase3, opts);
        saveListener sL = new saveListener(phase3);
    }
}