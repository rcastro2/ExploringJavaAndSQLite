//https://raima.com/how-to-create-a-database-using-jdbc/
//https://www.sqlitetutorial.net/sqlite-java/create-table/
import java.sql.*;
import java.util.ArrayList;

public class Database{
    private String url;
    private Connection conn;

    public Database(String url){
        this.url = url;
        connect(); 
    }
    private boolean connect(){
        boolean success = true;
        try{
            //Create a connection to the database. If it doesn't exist, create the DB
            conn = DriverManager.getConnection(url);    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        } 
        return success;
    }
    private boolean close(){
       boolean success = true;
        try{
            //Close the connection to the database
            conn.close();   
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        }
        return success; 
    }
    public boolean runSQL(String sql){
        boolean success = true;
        connect();
        try(Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();)
        {	
            stmt.execute(sql);
            //conn.commit(); 	  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        }finally {
            close();
        }
        return success;
    }
    public String runQuery(String sql, String format){
        String result = null;
        connect();
        try (Statement stmt  = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            if(format.indexOf("table") != -1){
                String colWidth = format.substring(format.indexOf("-")+1);
                result = table(rs,colWidth);
            }else if(format.equals("json")){
                result = json(rs);
            }else if(format.equals("csv")){
                result = csv(rs);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
        return result;
    }

    public int runUpdate(String sql){
        int rs = 0;
        connect();
        try (Statement stmt  = conn.createStatement()){
            rs = stmt.executeUpdate(sql);
            //conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {     
            close();      
        }
        return rs;
    }
    
    public String pad(String text, int width){
        String s = text.length() % 2 == 1? text += " ": text;
        s = text.length() >= width ? text.substring(0,width - 2): text;
        int diff = width - s.length();
        int padSize = diff / 2;
        String padding = new String(new char[padSize]).replace("\0", " ");
        return padding + s + padding;
    }

    private String json(ResultSet rs){
		String result = "";
        try{       
            //Get field names            
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();   

            String field, value, build = "[";
            // loop through the result set
            while (rs.next()) {
                build += "{";
                for (int i = 1; i <= columnCount; i++) {
                    field = metadata.getColumnName(i);
                    value = rs.getString(field);
                    build += "\"" + field + "\":\"" + value + "\",";
                }
                build = build.substring(0,build.length()-1) + "},";
            }
            result = build.substring(0,build.length()-1) + "]";
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
	}

    private String table(ResultSet rs, String colWidth){
		String result = "", build;
        try{       
            //Get field names           
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount(); 
            ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
            ArrayList<String> row;
            int[] maxWidths = new int[columnCount]; 
            String field, value;
            //Determine maxwidth for each column and store data
            row = new ArrayList<String>();
            for (int i = 1; i <= columnCount; i++) {
                field = metadata.getColumnName(i);
                row.add(field);
                maxWidths[i - 1] = Math.max(maxWidths[i - 1], field.length());
            }
            data.add(row);
            while (rs.next()) {
                row = new ArrayList<String>();
                for (int i = 1; i <= columnCount; i++) {
                    field = metadata.getColumnName(i);
                    value = rs.getString(field);
                    row.add(value);
                    maxWidths[i - 1] = Math.max(maxWidths[i - 1], value.length());
                }   
                data.add(row);
            }
            if(colWidth.equals("auto")){
                for(int i = 0; i < columnCount;i++){
                    if(maxWidths[i] % 2 == 1){
                        maxWidths[i]++;
                    } 
                }
            }else{
                int width = Integer.parseInt(colWidth); 
                if(width % 2 == 1){
                    width ++;
                }
                for(int i = 0; i < columnCount;i++){
                    maxWidths[i] = width;
                }
            }
            
            String rowSeparator = "+";
            for(int i = 0; i < columnCount; i++){
                for(int j = 0; j < maxWidths[i]+2; j++){
                    rowSeparator += "-";
                }
                rowSeparator += "+";
            }
            String header = "|";
            //Create Column Headers
            row = data.get(0);
            for (int i = 0; i < columnCount; i++) {
                header += pad(row.get(i),maxWidths[i]+2) + "|" ;
            }
            result = rowSeparator + "\n" + header + "\n" + rowSeparator + "\n";
            for(int i = 1; i < data.size(); i++){
                row = data.get(i);
                result += "|";
                for(int j = 0; j < row.size(); j++){                
                    result += pad(row.get(j),maxWidths[j]+2) + "|";
                }
                result += "\n" + rowSeparator + "\n";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
	}

    private String csv(ResultSet rs){
		String result = "";
        try{       
            //Get field names            
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();   

            String field, value;
            //Create Column Headers
            for (int i = 1; i <= columnCount; i++) {
                field = metadata.getColumnName(i);
                result += field + "," ;
            }
            result += "\n";
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    field = metadata.getColumnName(i);
                    value = rs.getString(field);
                    result += value + ",";
                }
                result += "\n";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
	}    
}