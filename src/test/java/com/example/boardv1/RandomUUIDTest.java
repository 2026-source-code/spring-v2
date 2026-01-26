package com.example.boardv1;

import java.util.UUID;

import org.junit.jupiter.api.Test;

public class RandomUUIDTest {

    @Test
    public void uuid_test() {
        UUID uid = UUID.randomUUID();
        System.out.println("--------------");
        System.out.println(uid);
    }

}
