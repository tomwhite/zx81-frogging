import sinclair.basic.ZX81SysVars;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Run this to find the first line of the program by trying various bit offsets and looking for a valid statement.
 * If nothing is found it's likely that the first byte is corrupted so it doesn't look like a statement.
 */
public class ZX81FindFirstLine {

    public static void main(String[] args) throws IOException {
        String filename = args.length > 0 ? args[0] : "pfiles/frogging-normalized.1.program1.p";
        try (FileInputStream fis = new FileInputStream(filename)) {
            byte[] fileBytes = new byte[fis.available()];
            fis.read(fileBytes);

            for (int bitOffset = -8; bitOffset < 8; bitOffset++) {
                int firstByte = BitUtils.getByteAtBitPosition(fileBytes, 8 * (ZX81SysVars.END - ZX81SysVars.SAVE_START + 4) + bitOffset);
                if (ZX81Statements.isStatement(firstByte)) {
                    System.out.printf("%s (%s bit offset)\n", ZX81SysVars.END, bitOffset);
                    BitUtils.printLine(fileBytes, 8 * (ZX81SysVars.END - ZX81SysVars.SAVE_START) + bitOffset, 40);
                }
            }
        }
    }
}
