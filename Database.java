//https://raima.com/how-to-create-a-database-using-jdbc/
//https://www.sqlitetutorial.net/sqlite-java/create-table/
import java.sql.*;

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
    public ResultSet runQuery(String sql){
        ResultSet rs = null;
        connect();
        try (Statement stmt  = conn.createStatement()){
            rs = stmt.executeQuery(sql);
            conn.commit();
            //print(table(rs,12));
            //print(json(rs));
            //print(csv(rs));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
        return rs;
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
    
    //https://stackoverflow.com/questions/24229442/print-the-data-in-resultset-along-with-column-names
    public String pad(String text, int width){
        String s = text.length() % 2 == 1? text += " ": text;
        s = text.length() >= width ? text.substring(0,10): text;
        int diff = width - s.length();
        int padSize = diff / 2;
        String padding = new String(new char[padSize]).replace("\0", " ");
        return padding + s + padding;
    }

    public static String json(ResultSet rs){
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

    public String table(ResultSet rs, int colWidth){
		String result = "";
        try{       
            //Get field names            
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();   

            String rowSeparator = "+";
            for(int i = 0; i < columnCount; i++){
                for(int j = 0; j < colWidth; j++){
                    rowSeparator += "-";
                }
                rowSeparator += "+";
            }

            String field, value, header = "|", row;
            //Create Column Headers
            for (int i = 1; i <= columnCount; i++) {
                field = metadata.getColumnName(i);
                header += pad(field,colWidth) + "|" ;
            }
            result = rowSeparator + "\n" + header + "\n" + rowSeparator + "\n";
            while (rs.next()) {
                row = "|";
                for (int i = 1; i <= columnCount; i++) {
                    field = metadata.getColumnName(i);
                    value = rs.getString(field);
                    row += pad(value,colWidth) + "|";
                }
                result += row + "\n" + rowSeparator + "\n";
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
	}

    public String csv(ResultSet rs){
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

    private static void print(Object obj){
        System.out.println(obj.toString());
    }
    
}