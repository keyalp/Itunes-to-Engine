package cat.keyalp.sql;

public class SqlDataBase {

    private String jdbcUrl;

    public SqlDataBase(String jdbcUrl) {
        this.jdbcUrl = "jdbc:sqlite:/" + jdbcUrl;
    }
}
