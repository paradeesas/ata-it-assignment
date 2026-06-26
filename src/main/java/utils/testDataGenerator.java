package utils;

import com.github.javafaker.Faker;
import model.users;

public class testDataGenerator {
    private static final Faker faker = new Faker();

    public static users generateRandomUser() {
        String randomStatus = faker.options().option("active", "inactive");
        
        return users.builder()
            .name(faker.name().fullName())
            .email(faker.internet().safeEmailAddress())
            .gender(faker.demographic().sex().toLowerCase())
            .status(randomStatus)
            .build();
    }

    public static users generateUserMissingFields() {
        String randomStatus = faker.options().option("active", "inactive");

        return users.builder()
            .email(faker.internet().safeEmailAddress())
            .status(randomStatus)
            .build();
    }

    public static users getInvalidGenderUser() {
        String randomStatus = faker.options().option("active", "inactive");

        return users.builder()
            .name(faker.name().fullName())
            .email(faker.internet().safeEmailAddress())
            .gender("invalidgender")
            .status(randomStatus)
            .build();
    }

    public static users getInvalidStatusUser() {
        return users.builder()
            .name(faker.name().fullName())
            .email(faker.internet().safeEmailAddress())
            .gender(faker.demographic().sex().toLowerCase())
            .status("invalidstatus")
            .build();
    }

    public static final int INVALID_USER_ID = 9999999;
    public static final String INVALID_TOKEN = "Bearer this-is-a-fake-token-12345";
}