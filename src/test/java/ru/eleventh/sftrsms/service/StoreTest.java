package ru.eleventh.sftrsms.service;

import lombok.val;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:test.properties")
public class StoreTest {

    String number = "9994567560", userId = "id56738", code = "00000";

    @Value("tries")
    Integer tries;

    @InjectMocks
    Store store;

    void addTest() {
        store.add(number, userId, code);

        val stored = store.get(number);
        assertNotNull(stored);
        assertEquals(stored.userId, userId);
        assertEquals(stored.tries, tries);
        assertEquals(stored.code, code);
        assertNotNull(stored.getCreatedTime());
    }

    void isExistTest() {
        assertFalse(store.isExist(number));

        store.add(number, userId, code);
        assertTrue(store.isExist(number));
    }

    void removeTest() {
        store.add(number, userId, code);
        assertNotNull(store.get(number));

        store.remove(number);
        assertNull(store.get(number));
    }

}