import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Dashboard extends JPanel{

    private JLabel mainTitle;
    private JPanel chartPanel;
    private JPanel statusPanel;
    private JPanel balancePanel, incomePanel, expensePanel;
    private JPanel recentPanel;
    private JButton incomeButton, expensButton, transferButton;

    public Dashboard(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS))
    }


}

public class GUI extends JFrame{
    public GUI(){
        this
    }
}
    public static void main(String[] args) {
        GUI app = new GUI();


    }
    
}
