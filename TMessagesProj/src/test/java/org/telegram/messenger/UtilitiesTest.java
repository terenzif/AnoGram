package org.telegram.messenger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilitiesTest {

    @Test
    public void testHexToBytes_Valid() {
        assertArrayEquals(new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0xAB, (byte) 0xCD}, Utilities.hexToBytes("1234ABCD"));
        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0xFF}, Utilities.hexToBytes("00ff"));
    }

    @Test
    public void testHexToBytes_Null() {
        assertNull(Utilities.hexToBytes(null));
    }

    @Test
    public void testHexToBytes_Empty() {
        assertArrayEquals(new byte[0], Utilities.hexToBytes(""));
    }

    @Test
    public void testHexToBytes_OddLength() {
        assertThrows(IllegalArgumentException.class, () -> Utilities.hexToBytes("ABC"));
    }

    @Test
    public void testHexToBytes_InvalidChars() {
        assertThrows(IllegalArgumentException.class, () -> Utilities.hexToBytes("G1"));
        assertThrows(IllegalArgumentException.class, () -> Utilities.hexToBytes("12G3"));
    }
}
