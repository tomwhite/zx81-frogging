import com.github.jinahya.bit.io.*;
import sinclair.basic.ZX81SysVars;
import sinclair.basic.ZX81Translate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 27/03/2016.
 */
public class BitUtils {

    private interface BitProcessor {
        void process(int bitPosition, boolean bit, DefaultBitOutput<ByteOutput> bitOutput) throws IOException;
    }

    private interface BitReader {
        void read(int bitPosition, boolean bit) throws IOException;
    }

    private static byte[] processBitwise(byte[] memory, BitProcessor bitProcessor) {
        ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
        DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

        byte[] memoryCopy = new byte[memory.length + 1];
        ArrayByteOutput arrayByteOutput = new ArrayByteOutput(memoryCopy, 0, memoryCopy.length);
        DefaultBitOutput<ByteOutput> bitOutput = new DefaultBitOutput<ByteOutput>(arrayByteOutput);

        int pos = 0;
        while (true) {
            try {
                bitProcessor.process(pos, bitInput.readBoolean(), bitOutput);
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

    private static void readBitwise(byte[] memory, BitReader bitReader) {
        ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
        DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

        int pos = 0;
        while (true) {
            try {
                bitReader.read(pos, bitInput.readBoolean());
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

    public static byte[] set(byte[] memory, int bitPosition, boolean value) {
        return processBitwise(memory, (pos, bit, bitOutput) -> {
            if (pos == bitPosition) {
                bitOutput.writeBoolean(value);
            } else {
                bitOutput.writeBoolean(bit);
            }
        });
    }

    public static byte[] del(byte[] memory, int bitPosition) {
        return processBitwise(memory, (pos, bit, bitOutput) -> {
            if (pos != bitPosition) {
                bitOutput.writeBoolean(bit);
            }
        });
    }

    public static byte[] ins(byte[] memory, int bitPosition, boolean value) {
        return processBitwise(memory, (pos, bit, bitOutput) -> {
            if (pos == bitPosition) {
                bitOutput.writeBoolean(value);
            }
            bitOutput.writeBoolean(bit);
        });
    }

    public static void find(byte[] memory, byte search) {
        readBitwise(memory, new BitReader() {
            int b = 0;
            @Override
            public void read(int bitPosition, boolean bit) throws IOException {
                b = (b << 1) & 0xFF;
                if (bit) {
                    b = b | 1;
                }
                if (((byte) b) == search) {
                    int startPos = bitPosition + 1 - 8;
                    System.out.printf("Found at byte pos %s (+%s bit offset)\n", ZX81SysVars.SAVE_START + (startPos / 8), startPos % 8);
                    printByteAtBitPosition(memory, startPos + 8);
                }
            }
        });
    }

    public static void findLineNumber(byte[] memory, int lineNumber) {
        readBitwise(memory, new BitReader() {
            int b = 0;
            @Override
            public void read(int bitPosition, boolean bit) throws IOException {
                b = (b << 1) & 0xFFFF;
                if (bit) {
                    b = b | 1;
                }
                int ln = (((b >> 8) & 255) << 8) + (b & 255);
                if (ln == lineNumber) {
                    int startPos = bitPosition + 1 - 16;
                    System.out.printf("Found at byte pos %s (+%s bit offset)\n", ZX81SysVars.SAVE_START + (startPos / 8), startPos % 8);
                    printLineNumberAndLength(memory, startPos);
                }
            }
        });
    }

    public static List<Integer> findNewlines(byte[] memory, int start) {
        List<Integer> newlines = new ArrayList();

        byte search = 118;
        ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
        DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

        int b = 0;
        int pos = 0;
        int lastPos = 0;
        while (true) {
            try {
                boolean bit = bitInput.readBoolean();
                pos++;
                if (pos < start) {
                    continue;
                }
                b = (b << 1) & 0xFF;
                if (bit) {
                    b = b | 1;
                }
                if (((byte) b) == search) {
                    int startPos = pos - 8;
                    newlines.add(startPos);
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
        return newlines;
    }


    public static void printByteAt(byte[] memory, int offset) {
        int v = memory[offset] & 255;
        System.out.printf("%s (%s)\n", Integer.toBinaryString(v), v);
    }

    public static void printByteAtBitPosition(byte[] memory, int bitPosition) {
        int v = getByteAtBitPosition(memory, bitPosition);
        System.out.printf("%s (%s)\n", ZX81Translate.translateZX81ToASCII(v), v);
    }

    public static int getByteAtBitPosition(byte[] memory, int bitPosition) {
        ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
        DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

        int pos = 0;
        while (true) {
            try {
                if (pos == bitPosition) {
                    return bitInput.readInt(true, 8);
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
        throw new IllegalArgumentException();
    }


    public static void printLineNumberAndLength(byte[] memory, int bitPosition) {
        ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
        DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

        int pos = 0;
        while (true) {
            try {
                if (pos == bitPosition) {
                    int a = bitInput.readInt(true, 8);
                    int b = bitInput.readInt(true, 8);
                    int c = bitInput.readInt(true, 8);
                    int d = bitInput.readInt(true, 8);
                    int ln = ((a & 255) << 8) + (b & 255);
                    int ll = (c & 255) + ((d & 255) << 8);

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 8; i++) {
                        int e = bitInput.readInt(true, 8) & 255;
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

    public static void printLine(byte[] memory, int bitPosition, int lineLength) {
        printLine(memory, bitPosition, lineLength, null);
    }
    public static void printLine(byte[] memory, int bitPosition, int lineLength, Line suggestion) {
        ArrayByteInput arrayByteInput = new ArrayByteInput(memory, 0, memory.length);
        DefaultBitInput<ByteInput> bitInput = new DefaultBitInput<ByteInput>(arrayByteInput);

        int pos = 0;
        while (true) {
            try {
                if (pos == bitPosition) {
                    int a = bitInput.readInt(true, 8);
                    int b = bitInput.readInt(true, 8);
                    int c = bitInput.readInt(true, 8);
                    int d = bitInput.readInt(true, 8);
                    int ln = ((a & 255) << 8) + (b & 255);
                    int ll = (c & 255) + ((d & 255) << 8);

                    StringBuilder sb = new StringBuilder();
                    StringBuilder debug0 = new StringBuilder();
                    StringBuilder debug1 = new StringBuilder();
                    StringBuilder bitstring = new StringBuilder();

                    debug0.append(String.format("%-16s", ln)).append("  ");
                    debug1.append(String.format("%-8s", a)).append(" ");
                    debug1.append(String.format("%-8s", b)).append(" ");
                    bitstring.append(String.format("%8s", Integer.toBinaryString(a)).replace(' ', '0')).append(" ");
                    bitstring.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0')).append(" ");

                    debug0.append(String.format("%-16s", ll)).append("  ");
                    debug1.append(String.format("%-8s", c)).append(" ");
                    debug1.append(String.format("%-8s", d)).append(" ");
                    bitstring.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0')).append(" ");
                    bitstring.append(String.format("%8s", Integer.toBinaryString(d)).replace(' ', '0')).append(" ");

                    int maxLineLength;
                    if (lineLength > 0) {
                        maxLineLength = lineLength;
                    } else if (ll < 50) {
                        maxLineLength = ll;
                    } else {
                        maxLineLength = 0;
                    }
                    int e = 0;
                    for (int i = 0; i < maxLineLength; i++) {
                        e = bitInput.readInt(true, 8) & 255;
                        sb.append(ZX81Translate.translateZX81ToASCII(e));
                        debug0.append(String.format("%-8s", ZX81Translate.translateZX81ToASCII(e))).append(" ");
                        debug1.append(String.format("%-8s", e)).append(" ");
                        bitstring.append(String.format("%8s", Integer.toBinaryString(e)).replace(' ', '0')).append(" ");
                    }
                    System.out.printf("%s %s\n", ln, sb);
                    System.out.printf("\t%s\n", debug0);
                    System.out.printf("\t%s\n", debug1);
                    System.out.printf("\t%s\n", bitstring);
                    if (suggestion != null) {
                        suggestion.print();
                        if (suggestion.getNumber() != ln) {
                            System.out.printf("\tWarning: Line number differs. Expected %d, but was %d.\n", suggestion.getNumber(), ln);
                        }
                    }
                    if (lineLength != ll) {
                        if (lineLength > 0) {
                            System.out.printf("\tWarning: Line length differs. Expected %d, but was %d.\n", lineLength, ll);
                        }
                    } else if (e != 118) {
                        System.out.printf("\tWarning: Line does not end with NEWLINE.\n");
                    }
                    System.out.println();
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
