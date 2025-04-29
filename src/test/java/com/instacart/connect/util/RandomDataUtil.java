package com.instacart.connect.util;

import com.github.javafaker.Faker;
import com.instacart.connect.pojo.User;

import java.util.UUID;

public class RandomDataUtil {

    private static Faker faker = new Faker();

    private RandomDataUtil() {}

    public static String getRandomId() {
        return UUID.randomUUID().toString();
    }

    public static int getRandomCents() {
        return faker.number().numberBetween(500, 2000);
    }

    public static long getShortRandomNumber() {
        return faker.number().numberBetween(1L,10L);
    }

    public static String getRandomDate() {
        return faker.date().birthday(18,50).toString();
    }

    public static User createUserData() {
        return User.builder().
        first_name(faker.name().firstName()).
                last_name(faker.name().lastName()).
                phone_number(faker.phoneNumber().cellPhone()).
                user_id(UUID.randomUUID().toString()).
                build();
    }

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String getPhoneNumber() {
        return faker.phoneNumber().cellPhone();
    }

}
