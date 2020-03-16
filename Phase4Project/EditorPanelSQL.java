import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class EditorPanelSQL extends JPanel implements Savable{
    public static int SAVE_DEFAULT = 0;
    public static int SAVE_QUERY = 1;
    public static int SAVE_RESULT = 2;
    
    DatabaseManager dbManager = new DatabaseManager();
    ResultDisplayPanel resultDisplayPanel;
    
    JScrollPane inputScrollPane;
    JTextArea inputArea;
    JButton executeButton;
    
    JPanel upperHalfPanel;
    JPanel lowerHalfPanel;
    
    public EditorPanelSQL(){
        this.setLayout(new GridLayout(1, 1));
        
        upperHalfPanel = new JPanel();
        upperHalfPanel.setLayout(new BorderLayout());
        
        inputArea = new JTextArea();
        inputScrollPane = new JScrollPane(inputArea);
        
        inputScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        executeButton = new JButton("Execute Query");
        
        upperHalfPanel.add(inputScrollPane, BorderLayout.CENTER);
        upperHalfPanel.add(executeButton, BorderLayout.SOUTH);
        
        lowerHalfPanel = new JPanel(new BorderLayout());
        resultDisplayPanel = new ResultDisplayPanel();
        lowerHalfPanel.add(resultDisplayPanel, BorderLayout.CENTER);
        
        addListeners();
        
        this.add(upperHalfPanel, 0, 0);
        this.add(lowerHalfPanel, 0, 1);
        this.validate();
        this.setVisible(true);
    }
    
    private void addListeners() {
        executeButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                // Action logic goes here
                String query = inputArea.getText();
                ResultTable resultTable = null;
                
      
             
                resultTable = MainApplication.dbManager.performQuery(query);
                //System.out.println(resultTable.toString());
             
            
                resultDisplayPanel.updateData(resultTable);
                refreshPanel();
            }
        });
    }
    
    private void refreshPanel(){
        this.validate();
    }

    @Override
    public String getSaveString(int option) {
        String saveString = "";
        
        if (option == EditorPanelSQL.SAVE_QUERY){
            saveString = inputArea.getText();
            
        }else if (option == EditorPanelSQL.SAVE_RESULT || option == EditorPanelSQL.SAVE_DEFAULT){
            ResultTable rt = resultDisplayPanel.getResultTable();
            if (rt == null){
                saveString = "No Data";
            }else {
                saveString = rt.toString();
            }
        }else {
            
        }
        return saveString;
    }

    
}
