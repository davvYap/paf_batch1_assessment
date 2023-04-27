package sg.edu.nus.iss.batch1_assessment.repository;

public class DBQueries {
        public static final String GET_ALL_ACCOUNTS = """
                        select * from accounts;
                        """;

        public static final String GET_ACCOUNT_BY_ID = """
                        select * from accounts where account_id = ?;
                            """;

        public static final String UPDATE_ACCOUNT_BALANCE_TRANSFER = """
                        update accounts set balance = balance - ? where account_id = ?;
                                """;

        public static final String UPDATE_ACCOUNT_BALANCE_RECEIVE = """
                        update accounts set balance = balance + ? where account_id = ?;
                                """;
}
