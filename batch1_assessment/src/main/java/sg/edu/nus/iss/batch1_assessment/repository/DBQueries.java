package sg.edu.nus.iss.batch1_assessment.repository;

public class DBQueries {
    public static final String GET_ALL_ACCOUNTS = """
            select * from accounts;
            """;

    public static final String GET_ACCOUNT_BY_ID = """
            select * from accounts where account_id = ?;
                """;
}
