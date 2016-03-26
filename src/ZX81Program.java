import sinclair.basic.ZX81Basic;
import sinclair.basic.ZX81SysVars;
import tapeutils.zx81.PFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.BitSet;

/**
 * Created by tom on 18/01/2015.
 */
public class ZX81Program {
    public static void main(String[] args) throws IOException {
        File pFile = new File("/Users/tom/projects-workspace/zx81/white-tape/frogging/frogging-normalized.1.program1.p");
        //readNewlines(pFile, 0);

//        for (int shift = 0; shift < 8; shift++) {
//            System.out.println("Shift: " + shift);
//            PFile exc = new PFile(read(pFile, shift), null);
//            System.out.println("D_FILE:" + exc.getProgramEndOffset());
//            exc.list(false, false, false, false);
//        }
        //exc.list(true, true, true, true);

        for (int shift = 0; shift < 8; shift++) {
            readFirstLine(pFile, shift);
        }
    }

    private static byte[] read(File f, int shift) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        fis.close();

        BitSet bs = BitSet.valueOf(fileBytes);

        for (int i = 0; i < bs.length(); i++) {
            // look for nl
            if (i + 7 < bs.length()) {
                int b = ((bs.get(i + 0) ? 0 : 1) << 7) |
                        ((bs.get(i + 1) ? 0 : 1) << 6) |
                        ((bs.get(i + 2) ? 0 : 1) << 5) |
                        ((bs.get(i + 3) ? 0 : 1) << 4) |
                        ((bs.get(i + 4) ? 0 : 1) << 3) |
                        ((bs.get(i + 5) ? 0 : 1) << 2) |
                        ((bs.get(i + 6) ? 0 : 1) << 1) |
                        ((bs.get(i + 7) ? 0 : 1) << 0);
//                System.out.println(b);
                if (b == 118) {
                    System.out.println("NL! at " + i + "(" + i/8 + "," + i%8 + ")");
                }
            }
        }

        BitSet shifted = new BitSet();
        for (int i = 0; i < bs.length(); i++) {
            int off = (i < 20 * 8 ? 0 : shift); // apply shift after D_FILE
            shifted.set(i + off, bs.get(i));
        }


        byte[] shiftedBytes = shifted.toByteArray();
        for (int i = 0; i < shiftedBytes.length; i++) {
//            if (shiftedBytes[i] == 118) {
//                System.out.println("NL!");
//            }
        }
        return shiftedBytes;
    }

    private static void readNewlines(File f, int shift) throws IOException {
        // idea here is to look for new lines and try to convert the bytes that we find between them
        // trouble is that we need to be more careful when it's not a multiple of 8...
        FileInputStream fis = new FileInputStream(f);
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        fis.close();

        BitSet bs = BitSet.valueOf(fileBytes);

        int firstNl = -1;
        int secondNl = -1;
        for (int i = 0; i < bs.length(); i++) {
            // look for nl
            if (i + 7 < bs.length()) {
                // TODO: which order should this be in?
                int b = ((bs.get(i + 0) ? 0 : 1) << 0) |
                        ((bs.get(i + 1) ? 0 : 1) << 1) |
                        ((bs.get(i + 2) ? 0 : 1) << 2) |
                        ((bs.get(i + 3) ? 0 : 1) << 3) |
                        ((bs.get(i + 4) ? 0 : 1) << 4) |
                        ((bs.get(i + 5) ? 0 : 1) << 5) |
                        ((bs.get(i + 6) ? 0 : 1) << 6) |
                        ((bs.get(i + 7) ? 0 : 1) << 7);
//                System.out.println(b);
                if (b == 118) {
                    System.out.println("NL!");
                    if (firstNl == -1) {
                        firstNl = i;
                        System.out.println(i);
                    } else if (secondNl == -1) {
                        secondNl = i;
                        System.out.println(i);
                    }
                }
            }
        }
        byte[] line = bs.get(firstNl + 8 + (4 * 8) /* line number and line length */, secondNl+8+1).toByteArray();
        System.out.println(line[line.length-1 & 255]);

        StringBuffer sb = new StringBuffer();
        ZX81Basic.dumpLine(line, 10, true, sb);
        System.out.println(sb);

    }

    private static void readFirstLine(File f, int shift) throws IOException {
        // idea here is to try to get the first line out
        byte[] memory = read(f, shift);
        int pos = 116;
        int lineNumber = ((memory[pos++] & 255) << 8) + (memory[pos++] & 255);
        int lineLength = (memory[pos++] & 255) + ((memory[pos++] & 255) << 8);
        // these seem to be wrong...
        System.out.println("lineNumber: " + lineNumber);
        System.out.println("lineLength: " + lineLength);
    }
}