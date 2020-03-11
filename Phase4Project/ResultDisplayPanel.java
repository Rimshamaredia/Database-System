import java.awt.GridLayout;
import javax.swing.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class ResultDisplayPanel extends JPanel{
    JTable table;
    JScrollPane scrollPane;
    ResultTable output;
    public ResultDisplayPanel(){
        table = new JTable();
        scrollPane = new JScrollPane();
        scrollPane.add(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.setLayout(new GridLayout(1,1));
    }

    public void updateData(ResultTable result){
        output = result;
        this.remove(scrollPane);
        DefaultTableModel model = new UneditableTableModel();

        table = new JTable(model);
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        if (result == null){
            JLabel noData = new JLabel("No result received...");
            scrollPane = new JScrollPane(noData);
            noData.setHorizontalAlignment(JLabel.CENTER);
            this.add(scrollPane);
            return;
        }
        ArrayList<String> columnNames = result.getFieldNames();
        try {
            for (int i = 0; i < result.getNumColumns(); i++){
                String columnName = columnNames.get(i);
                columnNames.add(columnName);
                model.addColumn(columnName);
            }

            for (int j = 0; j < result.getNumRows(); j++){
                Object[] rowData = new Object[result.getNumColumns()];
                for (int c = 0; c < result.getNumColumns(); c++){
                    rowData[c] = result.getString(j, result.getFieldIndex(columnNames.get(c)));
                }
                model.addRow(rowData);
            }
        } 
        catch(Exception e){
            e.printStackTrace();
        }

        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane);
    }
    
    public ResultTable getResultTable(){
        return output;
    }
    
}
