import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

public class ZX81NumbersTest {
  @Test
  public void test() {
    byte[] numberValues0 = new byte[] { 0, 0, 0, 0, 0 };
    byte[] numberValues5 = new byte[] { (byte) (131 & 255), (byte) 32, 0, 0, 0 };
    assertTrue(0.0 == ZX81Numbers.getNumber(numberValues0, 0));
    assertTrue(5.0 == ZX81Numbers.getNumber(numberValues5, 0));
    assertArrayEquals(numberValues0, ZX81Numbers.getNumberValues(0.0));
    assertArrayEquals(numberValues5, ZX81Numbers.getNumberValues(5.0));
  }
}
