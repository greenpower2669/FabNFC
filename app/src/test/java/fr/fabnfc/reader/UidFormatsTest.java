package fr.fabnfc.reader;

import org.junit.Test;
import static org.junit.Assert.*;

public class UidFormatsTest {
    @Test public void formatsAndReverseAreExact() {
        byte[] uid=new byte[]{(byte)0x04,(byte)0xA1,(byte)0x00,(byte)0xFF};
        assertEquals("04A100FF",UidFormats.hex(uid));
        assertEquals("FF00A104",UidFormats.hex(UidFormats.reverse(uid)));
        assertEquals("77660415",UidFormats.decimal(uid));
        assertArrayEquals(uid,UidFormats.fromHex("04a100ff"));
    }
    @Test public void zeroAndUnsigned() {
        assertEquals("255",UidFormats.decimal(new byte[]{(byte)0xFF}));
        assertEquals("0",UidFormats.decimal(new byte[]{0}));
    }
    @Test(expected=IllegalArgumentException.class)
    public void invalidHexRejected() { UidFormats.fromHex("GG"); }
}
