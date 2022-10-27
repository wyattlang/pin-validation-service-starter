package aws.training.pinvalidation;

import com.amazonaws.services.lambda.runtime.*;

import java.sql.*;
import java.util.Map;

public class PinValidationHandler implements RequestHandler<Map<String, String>, Map<String, String>> {

    @Override
    public Map<String, String> handleRequest(Map<String, String> request, Context context) {
        LambdaLogger logger = context.getLogger();

        logger.log("Attempting to retrieve environments variables: PATH_TO_DB, DB_USER, and DB_PASSWORD.");
        String pathToDb = System.getenv("PATH_TO_DB");
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        String pin = null;

        logger.log("Attempting to connect to RDS.");
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://" + pathToDb + ":3306/payment_system", dbUser, dbPassword);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select * from card where card_number = " + request.get("cardNumber"));
            rs.next();
            String userUuid = rs.getString("card_user_id");
            logger.log(userUuid);
            rs = stmt.executeQuery("select pin from card_user where id = \"" + userUuid + "\"");
            rs.next();
            pin = rs.getString(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        boolean valid = false;

        if (request.get("pinGiven").equals(pin)) {
            valid = true;
            logger.log("Given pin matches user's pin in database.");
        }

        request.put("valid", Boolean.toString(valid));

        return request;
    }

}
