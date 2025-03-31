package com.instacart.connect.util;

import com.github.javafaker.Faker;
import com.instacart.connect.pojo.User;

import java.util.UUID;

public class RandomDataUtil {

    private static Faker faker = new Faker();

    private RandomDataUtil() {}

    public static User createUserData() {
        return User.builder().
        first_name(faker.name().firstName()).
                last_name(faker.name().lastName()).
                phone_number(faker.phoneNumber().cellPhone()).
                user_id(UUID.randomUUID().toString()).
                build();
    }
}
