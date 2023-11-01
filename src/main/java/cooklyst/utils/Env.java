package cooklyst.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Env {
    private static final Dotenv dotenv = Dotenv.load();
    public static final String DB_HOST = dotenv.get("MYSQL_HOST");
    public static final String DB_PORT = dotenv.get("MYSQL_PORT");
    public static final String DB_NAME = dotenv.get("MYSQL_DATABASE");
    public static final String DB_USER = dotenv.get("MYSQL_USER", "root");
    public static final String DB_PASS = DB_USER.equals("root") ? dotenv.get("MYSQL_ROOT_PASSWORD") : dotenv.get("MYSQL_PASSWORD");
    public static final String PHP_KEY = dotenv.get("PHP_KEY");
    public static final String REST_KEY = dotenv.get("REST_KEY");
}
