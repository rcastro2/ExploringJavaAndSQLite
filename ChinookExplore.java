
import java.io.IOException;

import java.sql.*;
//javac -cp sqlite-jdbc-3.23.1.jar; DatabaseExplore.java

public class ChinookExplore {

    public static void main(String[] args) throws IOException {
        
        Database db = new Database("jdbc:sqlite:chinook.db");
	db.setVerbose(true);
        
        String sql;
        
        sql  = "SELECT * FROM artists " + 
               "WHERE Name = 'House Of Pain'";
        System.out.println(db.runSQL(sql,"table-auto"));
	

        sql  = "SELECT albums.Albumid, artists.Name FROM albums " + 
               "INNER JOIN artists ON artists.ArtistId = albums.ArtistId " +
               "WHERE Name = 'House Of Pain'";
        System.out.println(db.runSQL(sql,"table-auto"));

        sql = "SELECT tracks.TrackId, tracks.Name, albums.Title FROM tracks " + 
              "INNER JOIN albums ON albums.AlbumId = tracks.AlbumId " +
              "INNER JOIN artists ON artists.ArtistId = albums.ArtistId " +
              "WHERE artists.Name = 'House Of Pain'";
        System.out.println(db.runSQL(sql,"table-auto"));

    }    

}
