/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testproject;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author John Marrs
 */
public class UneditableTableModel extends DefaultTableModel{
    @Override
    public boolean isCellEditable(int row, int col){
        return false;
    }
}
