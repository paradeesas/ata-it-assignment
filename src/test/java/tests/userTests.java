package tests;

import client.userClient;
import model.users;
import utils.testDataGenerator;
import io.restassured.response.Response;

import org.junit.jupiter.api.*;
import utils.assertionUtils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class userTests {
    private static userClient api;
    private static users testUser;
    private static int createdUserId;

    @BeforeAll
    public static void setup() {
        api = new userClient();
        testUser = testDataGenerator.generateRandomUser();
    }

    @Test
    @Order(1)
    @DisplayName("TC01: To verify that system can retrieve all users and validate schema")
    public void testGetAllUsers() {
        Response response = api.getAllUsers();
        
        response.then()
                .statusCode(200);
        assertionUtils.assertAllUsersResponse(
            response
        );
    }
    
    @Test
    @Order(2)
    @DisplayName("TC02: To verify that the system should be able to create user Successfully")
    public void testCreateUser() {
        Response response = api.createUser(testUser, true);
        createdUserId = response.jsonPath().getInt("id"); 

        response.then().statusCode(201);
            assertionUtils.assertUserResponse(
            response, 
            testUser.getName(), 
            testUser.getEmail(), 
            testUser.getGender(), 
            testUser.getStatus()
        );
    }

    @Test
    @Order(3)
    @DisplayName("TC03: To verify that the system should be able to retrieve created user by ID Successfully")
    public void testGetCreatedUser() {
        Response response = api.getUser(createdUserId, true);
        
        response.then().statusCode(200);
            assertionUtils.assertUserResponse(
            response, 
            testUser.getName(), 
            testUser.getEmail(), 
            testUser.getGender(), 
            testUser.getStatus()
        );
    }

    @Test
    @Order(4)
    @DisplayName("TC04: To verify that the system should be able to update user successfully")
    public void testUpdateUser() {
        users updatedData = testDataGenerator.generateRandomUser();
        Response response = api.updateUser(createdUserId, updatedData);
        
        response.then().statusCode(200);
            assertionUtils.assertUserResponse(
            response, 
            testUser.getName(), 
            testUser.getEmail(), 
            testUser.getGender(), 
            testUser.getStatus()
        );
    }

    @Test
    @Order(5)
    @DisplayName("TC05: To verify that the system should not allow creation of duplicate users")
    public void testCreateDuplicateUser() {
        users newUser = testDataGenerator.generateRandomUser();
        api.createUser(newUser, true);

        Response response = api.createUser(newUser, true);

        response.then().statusCode(422);
        assertionUtils.assertArrayErrorMessage(
        response, 
        "email", 
        "has already been taken"
    );
    }

    @Test
    @Order(6)
    @DisplayName("TC06: To verify that the system should not allow updating user with duplicate email")
    public void testUpdateDuplicateUser() {
        users user1 = testDataGenerator.generateRandomUser();
        api.createUser(user1, true);

        users user2 = testDataGenerator.generateRandomUser();
        Response response2 = api.createUser(user2, true);
        Integer user2Id = response2.jsonPath().getInt("id");

        users updateData = new users();
        updateData.setName(user2.getName());
        updateData.setEmail(user1.getEmail());
        updateData.setGender(user2.getGender());
        updateData.setStatus(user2.getStatus());

        Response response = api.updateUser(user2Id, updateData);

        response.then().statusCode(422);
        assertionUtils.assertArrayErrorMessage(
            response, 
            "email", 
            "has already been taken"
        );
    }

    @Test
    @Order(7)
    @DisplayName("TC07: To verify that the system should not allow creation with invalid gender")
    public void testCreateWithInvalidGender() {
        users invalidGenderUser = testDataGenerator.getInvalidGenderUser();
        Response response = api.createUser(invalidGenderUser, true); 

        response.then().statusCode(422);
        assertionUtils.assertArrayErrorMessage(
            response,
            "gender", 
            "can't be blank, can be male of female"
        );
    }

    @Test
    @Order(8)
    @DisplayName("TC08: To verify that the system should not allow creation with invalid status")
    public void testCreateWithInvalidStatus() {
        users invalidStatusUser = testDataGenerator.getInvalidStatusUser();
        Response response = api.createUser(invalidStatusUser, true);

        response.then().statusCode(422);
        assertionUtils.assertArrayErrorMessage(
            response,
            "status", 
            "can't be blank"
        );
    }

    @Test
    @Order(9)
    @DisplayName("TC09: To verify that the system should not allow creation of user with missing required fields")
    public void testCreateUserWithMissingFields() {
        users invalidUser = testDataGenerator.generateUserMissingFields();
        Response response = api.createUser(invalidUser, true);

        response.then().statusCode(422);
        assertionUtils.assertListErrorMessage(response, "name", "can't be blank");
        assertionUtils.assertListErrorMessage(response, "gender", "can't be blank");
    }

    @Test
    @Order(10)
    @DisplayName("TC10: To verify that the system should not allow retrieval of non-existent user")
    public void testGetNonExistentUser() {
        Response response = api.getUser(testDataGenerator.INVALID_USER_ID, true);

        response.then().statusCode(404);
        assertionUtils.assertObjectErrorMessage(
            response,
            "message", 
            "Resource not found"
        );
    }

    @Test
    @Order(11)
    @DisplayName("TC11: To verify that the system should not allow update of user with invalid email format")
    public void testUpdateUserWithInvalidEmail() {
        users invalidEmailUser = testDataGenerator.generateRandomUser();
        invalidEmailUser.setEmail("invalid-email-format");
        
        Response response = api.updateUser(createdUserId, invalidEmailUser);
        response.then().statusCode(422);
        assertionUtils.assertArrayErrorMessage(
            response,
            "email", 
            "is invalid"
        );
    }

    @Test
    @Order(12)
    @DisplayName("TC12: To verify that the system should be able to delete user successfully")
    public void testDeleteUser() {
        Response response = api.deleteUser(createdUserId);
        response.then().statusCode(204);
    }

    @Test
    @Order(13)
    @DisplayName("TC13: To verify that the system should return error when delete a user has been deleted")
    public void testVerifyDeleteUserIsDeleted() {
        Response response = api.deleteUser(createdUserId);
        response.then().statusCode(404);
        assertionUtils.assertObjectErrorMessage(
            response,
            "message", 
            "Resource not found"
        );
    }

    @Test
    @Order(14)
    @DisplayName("TC14: To verify that the system should indicate when a user has been deleted")
    public void testVerifyUserIsDeleted() {
        Response response = api.getUser(createdUserId, true);
        response.then().statusCode(404);
        assertionUtils.assertObjectErrorMessage(
            response,
            "message", 
            "Resource not found"
        );
    }

    @Test
    @Order(15)
    @DisplayName("TC15: To verify that the system should not allow creation of user without token")
    public void testCreateUserWithoutToken() {
        Response response = api.createUser(testUser, false);

        response.then().statusCode(401);
        assertionUtils.assertObjectErrorMessage(
            response,
            "message", 
            "Authentication failed"
        );
    }

    @Test
    @Order(16)
    @DisplayName("TC16: To verify that the system should not allow user creation with invalid token")
    public void testCreateUserWithInvalidToken() {
        Response response = api.createUser(testUser, testDataGenerator.INVALID_TOKEN);
        
        response.then().statusCode(401);
        assertionUtils.assertObjectErrorMessage(
            response,
            "message", 
            "Invalid token"
        );
    }
    
}