
import java.io.IOException;

import java.sql.*;
//javac -cp sqlite-jdbc-3.23.1.jar; DatabaseExplore.java

public class DatabaseExplore {

    public static void main(String[] args) throws IOException {
        
        Database db = new Database("jdbc:sqlite:mydb.db");
        db.setDebug(false);
	db.setVerbose(true);
        String sql;
        
        sql = "CREATE TABLE Student (";
        sql +=  "id integer PRIMARY KEY,";
        sql +=  "name text NOT NULL,";
        sql +=  "course text NOT NULL";
        sql += ")"; 
        System.out.println(db.runSQL(sql));

        sql = "INSERT INTO Student (id,name,course) VALUES (1,'Castro','CS Career Development')";
        System.out.println(db.runSQL(sql));
        sql = "INSERT INTO Student (id,name,course) VALUES (2,'Porchetta','Intro to CS')";
        System.out.println(db.runSQL(sql));
        sql = "INSERT INTO Student (id,name,course) VALUES (3,'Bob','Web Development')";
        System.out.println(db.runSQL(sql));
        sql = "INSERT INTO Student (id,name,course) VALUES (4,'Naggar','Programming Technologies')";
        System.out.println(db.runSQL(sql));
        sql = "INSERT INTO Student (id,name,course) VALUES (5,'Margolin','\"Programming\" Technologies')";
        System.out.println(db.runSQL(sql));
        sql = "INSERT INTO Student (id,name,course) VALUES (6,\"O'Hara\",'Programming , Business Tech')";
        System.out.println(db.runSQL(sql));

        sql = "SELECT * FROM Student";
        System.out.println(db.runSQL(sql,"table-10"));

        sql = "UPDATE Student SET name = 'Reji' WHERE id = 3;";
        System.out.println(db.runSQL(sql));

        sql = "DELETE FROM Student WHERE id = 4;";
        System.out.println(db.runSQL(sql));

        sql = "SELECT id, name FROM Student";
        System.out.println(db.runSQL(sql,"table-15"));
        System.out.println(db.runSQL(sql,"table-auto"));
        System.out.println(db.runSQL(sql,"json"));
        System.out.println(db.runSQL(sql,"csv"));
       
    }    

}
