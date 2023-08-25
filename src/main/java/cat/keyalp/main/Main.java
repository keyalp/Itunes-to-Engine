package cat.keyalp.main;

import cat.keyalp.constants.Constants;
import cat.keyalp.constants.SqlColumns;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class Main {
    public static void main(String[] args) {
        replicateItmDb();
        String engineOsUrlDB = "jdbc:sqlite:" + Constants.ENGINE_DB;
        String itunesUrlDB = "jdbc:sqlite:" + Constants.ITUNES_DB;
//temp
        try {
            Connection engineOsDB = DriverManager.getConnection(engineOsUrlDB);
            Connection itunesDB = DriverManager.getConnection(itunesUrlDB);

            //Create Missing Tables on the Itunes Schema
            createMissingTables(itunesDB);

            //Get All Entries from source DB (Engine DJ)
            PreparedStatement selectAllEngineEntries = engineOsDB.prepareStatement("SELECT * FROM track");
            ResultSet allEngineEntries = selectAllEngineEntries.executeQuery();

//            //Get All Entries from dest DB (Itunes Lib)
//            PreparedStatement selectAllItunesEntries = itunesDB.prepareStatement("SELECT * FROM track");
//            ResultSet allItunesEntries = selectAllItunesEntries.executeQuery();

            while (allEngineEntries.next()) {
                String path = allEngineEntries.getString("path");
                System.out.println(path);
                // Update the destination database with the retrieved data
                StringBuilder updateQuery = new StringBuilder("UPDATE track SET ");
                for (SqlColumns sqlColumn : SqlColumns.values()) {
                    updateQuery.append(sqlColumn.getColumnName()).append(" = ?, ");
                }
                updateQuery = new StringBuilder(updateQuery.substring(0, updateQuery.length() - 2)); // Remove the last comma and space
                updateQuery.append(" WHERE path = ?"); // Use the appropriate primary key column

                try (PreparedStatement updateStmt = itunesDB.prepareStatement(updateQuery.toString())) {
                    int columnIndex = 1;
                    for (SqlColumns sqlColumn : SqlColumns.values()) {
                        switch (sqlColumn.getDataType()){
                            case Types.BOOLEAN:
                                updateStmt.setBoolean(columnIndex++, allEngineEntries.getBoolean(sqlColumn.getColumnName()));
                                break;
                            case Types.INTEGER:
                                updateStmt.setInt(columnIndex++, allEngineEntries.getInt(sqlColumn.getColumnName()));
                                break;
                            case Types.REAL:
                                updateStmt.setBigDecimal(columnIndex++, allEngineEntries.getBigDecimal(sqlColumn.getColumnName()));
                                break;
                            case Types.BLOB:
                                updateStmt.setBytes(columnIndex++, allEngineEntries.getBytes(sqlColumn.getColumnName()));
                                break;
                            case Types.TIMESTAMP:
                                updateStmt.setLong(columnIndex++, allEngineEntries.getLong(sqlColumn.getColumnName()));
                                break;
                            case Types.VARCHAR:
                                updateStmt.setString(columnIndex++, allEngineEntries.getString(sqlColumn.getColumnName()));
                                break;
                        }
                    }
                    updateStmt.setString(columnIndex, path);
                    updateStmt.executeUpdate();
                }

//                PreparedStatement selectItunesTrack = itunesDB.prepareStatement("SELECT * FROM track WHERE path = ?");
//                selectItunesTrack.setString(1,path);
//                ResultSet itunesTrack = selectItunesTrack.executeQuery();
//                if(itunesTrack.next())
//                    updateMetadataFromEngineToItunes();
//                else
//                    System.out.println("Removed Track: "+ path);
//                System.out.println(itunesTrack.getString("path"));
            }


            selectAllEngineEntries.close();
            allEngineEntries.close();
            engineOsDB.close();
            itunesDB.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

    private static void createMissingTables(Connection itunesDB) throws SQLException{
        //TODO copy from source DB missing tables on destination db
        String createPackTable = "CREATE TABLE \"Pack\" (\"id\" INTEGER,\"packId\" TEXT," +
                "\"changeLogDatabaseUuid\" TEXT,\"changeLogId\" INTEGER," +
                "\"lastPackTime\" DATETIME,PRIMARY KEY(\"id\" AUTOINCREMENT));";
        String createPrepareListTable = "CREATE TABLE \"PreparelistEntity\" (\"id\" INTEGER,\"trackId\" INTEGER," +
                "\"trackNumber\" INTEGER, PRIMARY KEY(\"id\" AUTOINCREMENT));";
        String createSmartListTable = "CREATE TABLE \"Smartlist\" (\"listUuid\" TEXT,\"title\" TEXT,\"parentPlaylistPath\" TEXT,\"nextPlaylistPath\" TEXT," +
                "\"nextListUuid\" TEXT,\"rules\" TEXT,\"lastEditTime\" DATETIME,PRIMARY KEY(\"listUuid\"));";

        try (PreparedStatement preparedStatement = itunesDB.prepareStatement(createPackTable)) {
            preparedStatement.executeUpdate();
            System.out.println("Table created successfully.");
        }
        try (PreparedStatement preparedStatement = itunesDB.prepareStatement(createPrepareListTable)) {
            preparedStatement.executeUpdate();
            System.out.println("Table created successfully.");
        }
        try (PreparedStatement preparedStatement = itunesDB.prepareStatement(createSmartListTable)) {
            preparedStatement.executeUpdate();
            System.out.println("Table created successfully.");
        }
    }

    private static void updateMetadataFromEngineToItunes() {

    }


    public static void replicateItmDb() {
        // Replace these with your actual source and destination file paths
        Path sourcePath = Path.of("itm.db");
        Path destinationPath = Path.of("m_new.db");

        try {
            // Copy the file using the Files.copy method
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while copying the file.");
            System.exit(-1);
        }
    }
}
