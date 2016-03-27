import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by tom on 27/03/2016.
 */
public class BitUtilsTest {
    @Test
    public void test() {
        // 0001 0001 0000 0001
        byte[] bytes = new byte[] { 0x11, 0x01 };
        // 0000 1000 1000 0000 1000 0000
        byte[] expectedBytes = new byte[] { (byte) 0x08, (byte) 0x80, (byte) 0x80 };
        byte[] insert = BitUtils.insert(bytes, 3, false);
        assertArrayEquals(expectedBytes, insert);
    }
}
