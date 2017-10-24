import com.github.jinahya.bit.io.*;
import sinclair.basic.ZX81SysVars;
import sinclair.basic.ZX81Translate;

import java.io.IOException;

/**
 * Created by tom on 27/03/2016.
 */
public class BitUtils {

    public static byte[] insert(byte[] memory, int bitPosition, boolean value) {
        ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
        DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

        byte[] memoryCopy = new byte[memory.length + 1];
        ArrayByteOutput arrayByteOutput = new ArrayByteOutput(memoryCopy, 0, memoryCopy.length);
        DefaultBitOutput<ByteOutput> bitOutput = new DefaultBitOutput<ByteOutput>(arrayByteOutput);

        int pos = 0;
        while (true) {
            try {
                if (pos == bitPosition) { // insert
                    //System.out.println(">"  + value);
                    bitOutput.writeBoolean(value);
                }
                boolean b = bitInput.readBoolean();
                //System.out.println(b);
                bitOutput.writeBoolean(b);
                pos++;
            } catch (IllegalStateException e) {
                // EOF
                for (int i = 0; i < 7; i++) {
                    try {
                        bitOutput.writeBoolean(false);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        return memoryCopy;
    }

    public static void find(byte[] memory, byte search) {
        ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
        DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

        int b = 0;
        int pos = 0;
        int lastPos = 0;
        while (true) {
            try {
                boolean bit = bitInput.readBoolean();
                pos++;
                b = (b << 1) & 0xFF;
                if (bit) {
                    b = b | 1;
                }
                if (((byte) b) == search) {
                    int startPos = pos - 8;
                    System.out.printf("Found at byte pos %s (+%s bit offset)\n", ZX81SysVars.SAVE_START + (startPos / 8), startPos % 8);
                    printLineNumberAndLength(memory, startPos + 8);
                    if (lastPos != 0) {
                        System.out.printf("Rough num bytes (inc line number, length): %s\n", (pos - lastPos)/8);
                    }
                    lastPos = pos;
                }
            } catch (IllegalStateException e) {
                // EOF
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void printByteAt(byte[] memory, int offset) {
        int v = memory[offset] & 255;
        System.out.printf("%s (%s)\n", Integer.toBinaryString(v), v);
    }

    public static void printLineNumberAndLength(byte[] memory, int bitPosition) {
        ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
        DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

        int pos = 0;
        while (true) {
            try {
                if (pos == bitPosition) {
                    int a = bitInput.read();
                    int b = bitInput.read();
                    int c = bitInput.read();
                    int d = bitInput.read();
                    int ln = ((a & 255) << 8) + (b & 255);
                    int ll = (c & 255) + ((d & 255) << 8);

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 8; i++) {
                        int e = bitInput.read() & 255;
                        sb.append(ZX81Translate.translateZX81ToASCII(e)).append("(").append(e).append(")").append(" ");
                    }
                    System.out.printf("Line: %s, len: %s, content: %s\n", ln, ll, sb);

                }
                bitInput.readBoolean();
                pos++;
            } catch (IllegalStateException e) {
                // EOF
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
