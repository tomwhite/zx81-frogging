import com.github.jinahya.bit.io.ArrayByteInput;
import com.github.jinahya.bit.io.ByteInput;
import com.github.jinahya.bit.io.DefaultBitInput;
import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BitUtilsTest {
  byte[] memory = new byte[] {
      0x0F, 0x30,
  };

  @Test
  public void testReadBits() throws IOException {
    ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
    DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(true, bitInput.readBoolean());
    assertEquals(true, bitInput.readBoolean());
    assertEquals(true, bitInput.readBoolean());
    assertEquals(true, bitInput.readBoolean());

    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(true, bitInput.readBoolean());
    assertEquals(true, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());

    try {
      bitInput.readBoolean();
    } catch (IllegalStateException e) {
      // expected
    }

  }

  @Test
  public void testReadUnsignedBytes() throws IOException {
    ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
    DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

    assertEquals(0x0F, bitInput.readInt(true, 8));
    assertEquals(0x30, bitInput.readInt(true, 8));

    try {
      bitInput.readInt(true, 8);
    } catch (IllegalStateException e) {
      // expected
    }

  }

  @Test
  public void testReadBitsAndUnsignedBytes() throws IOException {
    ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
    DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());

    assertEquals(0xF3, bitInput.readInt(true, 8));

    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());
    assertEquals(false, bitInput.readBoolean());

    try {
      bitInput.readBoolean();
    } catch (IllegalStateException e) {
      // expected
    }

  }
}
