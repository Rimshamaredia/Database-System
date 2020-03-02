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