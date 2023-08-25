import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLTest {
    public static void main(String[] args) {
        //String sourceUrl = "jdbc:sqlite:/D:/m.db"; // Replace with source database file path
        String sourceUrl = "jdbc:sqlite:/D:/Documents/OneDrive - alu-etsetb.upc.edu/Música/Engine Library/Database2/m.db";
        String destUrl = "jdbc:sqlite:/D:/Documents/OneDrive - alu-etsetb.upc.edu/Música/Engine Library/Database2/m_new.db"; // Replace with destination database file path

        try {
            // Read the BLOB from the source database
            Connection sourceConnection = DriverManager.getConnection(sourceUrl);
//            String selectRatingSql = "SELECT rating FROM track WHERE id = ?";
//            String selectTitleSql = "SELECT title FROM track WHERE id = ?";

            PreparedStatement selectAll = sourceConnection.prepareStatement("SELECT * FROM track");
            PreparedStatement selectRow = sourceConnection.prepareStatement("SELECT * FROM track WHERE id = ?");
            PreparedStatement selectRatings = sourceConnection.prepareStatement("SELECT * FROM track WHERE rating > 50");
            selectRow.setInt(1, 1); // Replace 1 with the appropriate ID
            ResultSet resultSet = selectRow.executeQuery();
            ResultSet resultRatings = selectRatings.executeQuery();
            ResultSet resultAll = selectAll.executeQuery();

            while(resultRatings.next()){
                System.out.println(resultRatings.getString("path"));
            }

            while(resultAll.next()){
                System.out.println(resultAll.getString("title"));
            }

            byte[] loops = null;
            byte[] quickCues = null;
            String path = null;
            int rating=0;
            if (resultSet.next()) {
                path = resultSet.getString("path");
                rating = resultSet.getInt("rating");
                System.out.println(rating);
                System.out.println(path);
                //loops = resultSet.getBytes("loops");
                //quickCues = resultSet.getBytes("quickCues");
            }

            resultSet.close();
            selectRow.close();
            sourceConnection.close();

            if (rating != 0) {
                // Write the BLOB to the destination database
                Connection destConnection = DriverManager.getConnection(destUrl);
                String updateRating = "UPDATE track SET rating = ? WHERE path = ?";
                // String updateSQL = "UPDATE dest_table SET column1 = ?, column2 = ?, ... WHERE id = ?";
                PreparedStatement updateStmt = destConnection.prepareStatement(updateRating);
                updateStmt.setInt(1, rating);
                updateStmt.setString(2, path);
                updateStmt.executeUpdate();

                updateStmt.close();
                destConnection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void countEntriesFromTable(){
        String databaseUrl = "jdbc:sqlite:/path/to/your/database.db"; // Replace with your database file path
        String tableName = "your_table_name"; // Replace with the name of your table

        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(databaseUrl);

            // Prepare the COUNT(*) query
            String countQuery = "SELECT COUNT(*) FROM " + tableName;
            PreparedStatement preparedStatement = connection.prepareStatement(countQuery);

            // Execute the query and retrieve the result
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int rowCount = resultSet.getInt(1);
                System.out.println("Number of entries in " + tableName + ": " + rowCount);
            }

            // Close resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
