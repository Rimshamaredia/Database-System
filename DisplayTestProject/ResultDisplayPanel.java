/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package testproject;
import java.awt.GridLayout;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class ResultDisplayPanel extends JPanel{
    JTable table;
    JScrollPane scrollPane;
    public ResultDisplayPanel(){
        table = new JTable();
        scrollPane = new JScrollPane();
        scrollPane.add(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.setLayout(new GridLayout(1,1));
    }

    public void updateData(ResultSet result){
        this.remove(scrollPane);
        DefaultTableModel model = new UneditableTableModel();
        ArrayList<String> columnNames = new ArrayList<String>();

        table = new JTable(model);
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );


        if (result == null){
            JLabel noData = new JLabel("No result received...");
            scrollPane = new JScrollPane(noData);
            noData.setHorizontalAlignment(JLabel.CENTER);
            this.add(scrollPane);
            return;
        }
        try {
            for (int i = 1; i <= result.getMetaData().getColumnCount(); i++){
                String columnName = result.getMetaData().getColumnName(i);
                columnNames.add(columnName);
                model.addColumn(columnName);
            }

            //System.out.println("About to add the rows...");
            while (result.next()){
                //System.out.println("Iterated");
                Object[] rowData = new Object[columnNames.size()];
                for (int col = 0; col < columnNames.size(); col++){
                    rowData[col] = result.getString(columnNames.get(col));
                    //System.out.println("Tried to add " + columnNames.get(col));
                }

                model.addRow(rowData);
            }
        } catch(Exception e){
            e.printStackTrace();
        }

        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane);

    }

}
