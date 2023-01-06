
import java.io.IOException;

import java.sql.*;
//javac -cp sqlite-jdbc-3.23.1.jar; DatabaseExplore.java

public class DatabaseExplore {

    public static void main(String[] args) throws IOException {
        
        Database db = new Database("jdbc:sqlite:mydb.db");
        /*
        String sql = "CREATE TABLE IF NOT EXISTS Student (id integer PRIMARY KEY, name text NOT NULL)"; 
        System.out.println(db.runSQL(sql));

        sql = "INSERT INTO Student (id,name) VALUES(1,'Castro')";
        System.out.println(db.runSQL(sql));
        sql = "INSERT INTO Student (id,name) VALUES(2,'Porchetta')";
        System.out.println(db.runSQL(sql));
        sql = "INSERT INTO Student (id,name) VALUES(3,'Bob')";
        System.out.println(db.runSQL(sql));
        sql = "INSERT INTO Student (id,name) VALUES(4,'Naggar')";
        System.out.println(db.runSQL(sql));

        sql = "SELECT * FROM Student";
        System.out.println(db.selectData(sql));

        sql = "UPDATE Student SET name = 'Reji' WHERE id = 3;";
        System.out.println(db.runSQL(sql));

        sql = "DELETE FROM Student WHERE id = 4;";
        System.out.println(db.runSQL(sql));

        sql = "SELECT * FROM Student";
        System.out.println(db.selectData(sql));
        */
        /*
        System.out.println(Database.pad("A",12));
        System.out.println(Database.pad("id",12));
        System.out.println(Database.pad("hello",12));
        System.out.println(Database.pad("CourseCode",12));
        System.out.println(Database.pad("Course Code Name",12));
        */
        String sql = "SELECT * FROM Student";
        ResultSet rs = db.runQuery(sql);
        try{
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();
        }catch(SQLException e){

        }
          
        //System.out.println(db.table(rs,12));
        //System.out.println(Database.printResultSet(rs));


       
    }    

}
