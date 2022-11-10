package aws.training.pinvalidation;

import com.amazonaws.services.lambda.runtime.*;

import java.sql.*;
import java.util.Map;

public class PinValidationHandler implements RequestHandler<Map<String, String>, Map<String, String>> {

    @Override
    public Map<String, String> handleRequest(Map<String, String> request, Context context) {
        // your code here
        return null;
    }

}
