
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ResultTable {
    private LinkedHashMap<String, Integer> fieldNames;
    private ArrayList<ArrayList<String>> dataTable;
    private boolean hasFieldNames;
    private Integer numRows;
    private Integer numColumns;
    
    public ResultTable(){
        hasFieldNames = false;
    }
    
    // This constructor will create a ResultTable object reflecting a ResultSet object
    public ResultTable(ResultSet rs){
        fieldNames = new LinkedHashMap<String, Integer>();
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
        fieldNames = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < _fieldNames.size(); i++){
            fieldNames.put(_fieldNames.get(i), i);
        }
        dataTable = new ArrayList<ArrayList<String>>();
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
    
    public ArrayList<String> getFieldNames(){
        // BUG: Need to fix order issues
        // TODO: Fix the order bug.
        ArrayList<String> originalKeys = null;
        originalKeys = new ArrayList<String>(fieldNames.keySet());
        
        ArrayList<String> orderedKeys = new ArrayList<String>();
        
        // this code should sort the key names properly
        for (int col = 0; col < originalKeys.size(); col++){
            orderedKeys.add(originalKeys.get(this.getFieldIndex(originalKeys.get(col))));
        }
        return orderedKeys;
    }
    
    public String getString(int row, int col) throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        
        String value = null;
        value = dataTable.get(row).get(col);
        return value;
    }
    
    public Boolean getBoolean(int row, int col) throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        Boolean value = null;
        String inputString = dataTable.get(row).get(col);
        if (inputString.equals("1") || inputString.equals("0")){
            if (inputString.equals("1")){
                value = true;
            }else {
                value = false;
            }
            return value;
        } else if (inputString.equalsIgnoreCase("true") || inputString.equalsIgnoreCase("false")){
           value = Boolean.parseBoolean(dataTable.get(row).get(col));
        }
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
    
    public Double getDouble(int row, int col) throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        
        Double value = null;
        String tempValue = dataTable.get(row).get(col);
        value = Double.parseDouble(tempValue);
        return value;
    }
    
    public void setBoolean(int row, int col, Boolean value) throws IndexOutOfBoundsException{
        if ((row < 0 || row > numRows) || (col < 0 || col > numColumns)){
            throw new IndexOutOfBoundsException();
        }
        dataTable.get(row).set(col, value.toString());
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
        System.out.println(dataTable.size());
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
                value += this.getFieldNames().get(i);
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
