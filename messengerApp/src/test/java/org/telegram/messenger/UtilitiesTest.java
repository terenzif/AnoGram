package org.telegram.messenger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
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
        assertTrue("Generated string contains invalid characters",
                randomString.matches("[0-9a-zA-Z]*"));
    }

    @Test
    public void testGenerateRandomString_DefaultLength() {
        assertEquals(16, Utilities.generateRandomString().length());
    }

    @Test
    public void testGenerateRandomString_Randomness() {
        String randomString = Utilities.generateRandomString(1000);
        assertTrue("Generated string should not consist of the same character repeated",
                randomString.chars().distinct().count() > 1);
    }
}
