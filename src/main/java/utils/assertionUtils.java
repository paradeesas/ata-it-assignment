package utils;

import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

public class assertionUtils {

    public static void assertAllUsersResponse(Response response) {
        response.then()
                .log().ifValidationFails()
                .body("$", hasSize(greaterThan(0)))
                .body("id", everyItem(instanceOf(Integer.class)))
                .body("name", everyItem(instanceOf(String.class)))
                .body("email", everyItem(matchesPattern("^[A-Za-z0-9+_.-]+@(.+)$")))
                .body("gender", everyItem(notNullValue()))
                .body("status", everyItem(notNullValue()));
    }
    
    public static void assertUserResponse(Response response, String expectedName, String expectedEmail, String expectedGender, String expectedStatus) {
        response.then()
                .log().ifValidationFails()
                .body("id", instanceOf(Integer.class))
                .body("name", instanceOf(String.class))
                .body("email", matchesPattern("^[A-Za-z0-9+_.-]+@(.+)$"))
                .body("gender", anyOf(equalTo("male"), equalTo("female")))
                .body("status", anyOf(equalTo("active"), equalTo("inactive")));
    }

    public static void assertObjectErrorMessage(Response response, String expectedKey, String expectedValue) {
        response.then()
                .log().ifValidationFails()
                .body(expectedKey, equalTo(expectedValue));
    }

    public static void assertArrayErrorMessage(Response response, String expectedField, String expectedMessage) {
        response.then()
                .log().ifValidationFails()
                .body("[0].field", equalTo(expectedField))
                .body("[0].message", equalTo(expectedMessage));
    }

    public static void assertListErrorMessage(Response response, String expectedField, String expectedMessage) {
        response.then()
                .log().ifValidationFails()
                .body("field", hasItems(expectedField))
                .body("message", hasItems(expectedMessage));
    }
}