import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class MainApplication extends JFrame {
    PanelQ1 q1Panel;
    PanelQ3 q3Panel;
    PanelQ4 q4Panel;
    PanelQ5 q5Panel;
    PanelBonus bonusPanel;
    EditorPanelSQL editorPanel;
    
    JPanel mainPanel;
    
    JFileChooser fileChooser;
    
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem saveAs;
    
    JMenu aboutMenu;
    JMenuItem about;
    
    JTabbedPane tabbedPane;
    
    public static void main(String[] args){
        MainApplication mainApp = new MainApplication();
    }
    
    public MainApplication() {
        this.setTitle("CSCE 315-900 Phase 4 Application");
        this.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        this.setSize(720, 480);
        this.setLayout(new BorderLayout());
        
        // Code for initlializing the menu bar
        
        menuBar = new JMenuBar();
        
        fileMenu = new JMenu("File");
        saveAs = new JMenuItem("Save as...");
        fileMenu.add(saveAs);
        
        about = new JMenuItem("About...");
        aboutMenu = new JMenu("About");
        aboutMenu.add(about);
        
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        
        // End code for initializing the menu bar
        
        // Code for initializing the tabbed pane
        tabbedPane = new JTabbedPane();
        
        
        tabbedPane.add(new JLabel("test1"), "Test 1");
        
        q1Panel = new PanelQ1();
        tabbedPane.add(q1Panel, "Question 1");
        
        q3Panel = new PanelQ3();
        tabbedPane.add(q3Panel, "Question 3");
        
        q4Panel = new PanelQ4();
        tabbedPane.add(q4Panel,"Question 4" );
        
        q5Panel = new PanelQ5();
        tabbedPane.add(q5Panel, "Question 5");
        
        bonusPanel = new PanelBonus();
        tabbedPane.add(bonusPanel, "Bonus Questions");
        
        editorPanel = new EditorPanelSQL();
        tabbedPane.add(editorPanel, "Editor");
        
        // End code for initializing the tabbed pane
        
        this.add(menuBar, BorderLayout.NORTH);
        this.add(tabbedPane, BorderLayout.CENTER);
        
        this.addListeners();
        
        this.validate();
        this.setVisible(true);
    }
    
    private void addListeners(){
        saveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                saveAsHandler();
            }
        });
        
        about.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                aboutHandler();
            }
        });
    }
    
    private void saveAsHandler(){
        int saveOption = 0;
        Savable selectedPanel = (Savable) this.tabbedPane.getSelectedComponent();
        
        if (selectedPanel.equals(editorPanel)){
            Object[] options = {"The Query", "The Results", "Cancel"};
            String questionTitle = "Save selection";
            String question = "Which information would you like to save?";
            int choice = JOptionPane.showOptionDialog(null, question,
                questionTitle,
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            
            switch (choice){
                case 0:
                    saveOption = EditorPanelSQL.SAVE_QUERY;
                    break;
                case 1:
                    saveOption = EditorPanelSQL.SAVE_RESULT;
                    break;
                case 2:
                    saveOption = -1;
                    break;
                default:
                    saveOption = -1;
                    break;
            }
        }
        
        if (saveOption == -1){
            return;
        }
        
        System.out.println("Save option: " + saveOption);
        fileChooser = new JFileChooser();
        int fileChosenResult = fileChooser.showSaveDialog(null);
        String filePath = "";
        if (fileChosenResult == JFileChooser.APPROVE_OPTION ){
            filePath = fileChooser.getSelectedFile().getAbsolutePath();
        }
        System.out.println("Save file to " + filePath);
        
        boolean success = FileSaver.saveFile(filePath, selectedPanel.getSaveString(saveOption));
        if (success){
            // Show that save was successful
        }else {
            // Show that save was successful
        }
    }
    
    private void aboutHandler(){
        String message = "About this program:\n";
        message += "CSCE 315 - 900 Group Project 1 Phase 4\n";
        message += "Team Members: John Marrs, Michaela Matocha,\nNathan Mandell, Rimsha Maredia";
        
        JOptionPane.showMessageDialog(this, message);
    }
}
