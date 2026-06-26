package client;

import model.users;
import utils.configManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class userClient {
    private final RequestSpecification requestSpec;

    public userClient() {
        this.requestSpec = new RequestSpecBuilder()
                .setBaseUri(configManager.get("BASE_URL"))
                .setContentType(ContentType.JSON)
                .log(LogDetail.BODY)
                .build();
    }

    private RequestSpecification getRequestSpec(boolean isAuthRequired) {
        RequestSpecification spec = given().spec(requestSpec).log().ifValidationFails();
        if (isAuthRequired) {
            String token = configManager.get("GOREST_API_TOKEN");
            spec.header("Authorization", "Bearer " + token);
        }
        return spec;
    }

    public Response getAllUsers() {
        return getRequestSpec(false).get("/users");
    }

    public Response getUser(int id, boolean withAuth) {
        return getRequestSpec(withAuth).get("/users/" + id);
    }

    public Response createUser(users user, boolean withAuth) {
        return getRequestSpec(withAuth).body(user).post("/users");
    }

    public Response createUser(users user, String token) {
        return given()
            .spec(getRequestSpec(false))
            .header("Authorization", "Bearer " + token)
            .body(user)
            .post("/users");
    }

    public Response createUser(String email, boolean withAuth) {
        users user = new users();
        user.setName("Duplicate User"); 
        user.setEmail(email);        
        user.setGender("male");
        user.setStatus("active");
        return getRequestSpec(withAuth).body(user).post("/users");
    }

    public Response updateUser(int id, users user) {
        return getRequestSpec(true).body(user).put("/users/" + id);
    }

    public Response deleteUser(int id) {
        return getRequestSpec(true).delete("/users/" + id);
    }
}