//https://raima.com/how-to-create-a-database-using-jdbc/
//https://www.sqlitetutorial.net/sqlite-java/create-table/
import java.sql.*;

public class Database{
    private String url;
    private Connection conn;

    public Database(String url){
        this.url = url;
        if(!connect()){
            createDB();
        }else{
            System.out.println("Successfully tested connection to database"); 
        }
    }
    private boolean createDB(){
        boolean success = true;
        try(Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();)
        {	
            System.out.println("Database created successfully");   	  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            success = false;
        } 
        return success;
    }
    private boolean connect(){
        boolean success = true;
        try{
            //Create a connection to the database
            conn = DriverManager.getConnection(url);
            //System.out.println("Connection to SQLite has been established.");    
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
    public String selectData(String sql){
        String result = null;
        connect();
        //System.out.println(sql);
        try (Statement stmt  = conn.createStatement()){
            ResultSet rs    = stmt.executeQuery(sql);
            result = jsonify(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
        return result;
    }

    public ResultSet runSQL(String sql){
        ResultSet rs = null;
        connect();
        try (Statement stmt  = conn.createStatement()){
            rs = stmt.executeUpdate(sql);
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {     
            close();      
        }
        return rs;
    }
    public static String jsonify(ResultSet rs){
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
}