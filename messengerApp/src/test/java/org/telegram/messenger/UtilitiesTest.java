package org.telegram.messenger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtilitiesTest {

    @Test
    public void testGenerateRandomString_Length() {
        assertEquals(0, Utilities.generateRandomString(0).length());
        assertEquals(1, Utilities.generateRandomString(1).length());
        assertEquals(16, Utilities.generateRandomString(16).length());
        assertEquals(100, Utilities.generateRandomString(100).length());
    }

    @Test
    public void testGenerateRandomString_Characters() {
        String randomString = Utilities.generateRandomString(1000);
        for (int i = 0; i < randomString.length(); i++) {
            char c = randomString.charAt(i);
            assertTrue("Generated string contains invalid character: " + c,
                    Utilities.RANDOM_STRING_CHARS.indexOf(c) != -1);
        }
    }

    @Test
    public void testGenerateRandomString_DefaultLength() {
        assertEquals(16, Utilities.generateRandomString().length());
    }

    @Test
    public void testGenerateRandomString_Randomness() {
        String str1 = Utilities.generateRandomString(16);
        String str2 = Utilities.generateRandomString(16);
        assertNotEquals("Consecutive calls to generateRandomString produced the same result", str1, str2);
    }
}
