
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultTable {
    private HashMap<String, Integer> fieldNames;
    private ArrayList<ArrayList<String>> dataTable;
    private boolean hasFieldNames;
    private Integer numRows;
    private Integer numColumns;
    
    public ResultTable(){
        hasFieldNames = false;
    }
    
    // This constructor will create a ResultTable object reflecting a ResultSet object
    public ResultTable(ResultSet rs){
        fieldNames = new HashMap<String, Integer>();
        dataTable = new ArrayList<ArrayList<String>>();
        hasFieldNames = true;
        try {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++){
                String fieldName = rs.getMetaData().getColumnName(i);
                fieldNames.put(fieldName, i-1);
            }
            numColumns = fieldNames.size();
            while (rs.next()){
                ArrayList<String> rowData = new ArrayList<String>(numColumns);
                for (int col = 0; col < numColumns; col++){
                    rowData.add(col, rs.getString(col + 1));
                }
                dataTable.add(rowData);
            }
            numRows = dataTable.size();
             
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ResultTable(ArrayList<String> _fieldNames){
        fieldNames = new HashMap<String, Integer>();
        for (int i = 0; i < _fieldNames.size(); i++){
            fieldNames.put(_fieldNames.get(i), i);
        }
        numColumns = _fieldNames.size();
        numRows = 0;
        hasFieldNames = true;
    }
    
    public Integer getNumRows() {
        return numRows;
    }
    
    public Integer getNumColumns() {
        return numColumns;
    }
    
    public String getString(int row, int col) throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        
        String value = null;
        value = dataTable.get(row).get(col);
        return value;
    }
    
    public Integer getInteger(int row, int col) throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        
        Integer value = null;
        String tempValue = dataTable.get(row).get(col);
        value = Integer.parseInt(tempValue);
        return value;
    }
    
    public Date getDate(int row, int col) throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        
        Date value = null;
        
        return value;
    }
    
    public Double getDouble(int row, int col) throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        
        Double value = null;
        String tempValue = dataTable.get(row).get(col);
        value = Double.parseDouble(tempValue);
        return value;
    }
    
    public void setInteger(int row, int col, Integer value)throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        dataTable.get(row).set(col, value.toString());
    }
    
    public void setString(int row, int col, String value)throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        dataTable.get(row).set(col, value);
    }
    
    public void setDouble(int row, int col, Double value)throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        dataTable.get(row).set(col, value.toString());
    }
    
    public void setDate(int row, int col, Date value)throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        dataTable.get(row).set(col, value.toString());
    }
    
    // Creates a new row for the data. The method returns the index of the new row. 
    public Integer addRow(){
        ArrayList<String> newRow = new ArrayList<String>(numColumns);
        dataTable.add(newRow);
        numRows = dataTable.size();
        Integer rowIndex = numRows - 1;
        return rowIndex;
    }
    
    public void addRow(ArrayList<String> rowData){
        dataTable.add(rowData);
        numRows = dataTable.size();
    }
    
    public Integer getFieldIndex(String field){
        Integer result = -1;
        return fieldNames.get(field);
    }
    
    @Override
    public String toString(){
        String value = "";
        if (hasFieldNames){
            for (int i = 0; i < fieldNames.size(); i++){
                value += fieldNames.get(i);
                if (i < fieldNames.size() - 1){
                    value += ", ";
                }else {
                    value += "\n";
                }
            }
        }
        
        for (int row = 0; row < numRows; row++){
            for (int col = 0; col < numColumns; col++){
                value += getString(row, col);
                if (col < numColumns - 1){
                    value += ", ";
                }
            }
            if (row < numRows - 1){
                value += "\n";
            }
        }
        return value;
    }
}
