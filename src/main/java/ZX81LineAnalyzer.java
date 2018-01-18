import sinclair.basic.ZX81SysVars;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class ZX81LineAnalyzer {
    public static void main(String[] args) throws Exception {
        File f = new File("pfiles/frogging-normalized.1.program1.p");
        //File f = new File("/Users/tom/projects-workspace/zx81/randompatterns.p");
        FileInputStream fis = new FileInputStream(f);
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        fis.close();

        // insert two bits to see if we can get frogging program out...
        fileBytes = BitUtils.insert(fileBytes, 8 * (ZX81SysVars.PRBUFF - ZX81SysVars.SAVE_START), false);
        fileBytes = BitUtils.insert(fileBytes, 8 * (ZX81SysVars.MEMBOT - ZX81SysVars.SAVE_START), false);

        // Line 1: insert 2 bits to make sure it works. Line length is still wrong
        fileBytes = BitUtils.insert(fileBytes, 8 * 116, false);
        fileBytes = BitUtils.insert(fileBytes, 8 * 118, false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 7) + 5, true);
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 7) + 6, true); // C -> I
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 2, true); // ( -> N
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 6, true); // ( -> N
        fileBytes = BitUtils.delete(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 7);
        fileBytes = BitUtils.delete(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 7);

        // Line 5: insert a bit to make sure ends with a " to close the print
        // 5 PRINT AT 16,3;"{6}{H}{F}{F}{6}{E}{E}{E}{E}{E}{E}{E}{E}{E}{E}{E}+{T}"
        fileBytes = BitUtils.insert(fileBytes, 8 * (16525 - ZX81SysVars.SAVE_START + 4 + 38), false);

        // Line 10: fix NEWLINE
        fileBytes = BitUtils.set(fileBytes, 8 * (16569 - ZX81SysVars.SAVE_START + 4 + 11) + 1, true);

        // Line 30: insert 1 bits (PRINT AT H,V ...)
        fileBytes = BitUtils.insert(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START + 4 + 6), false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START + 4 + 6) + 2, false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START + 4 + 6) + 5, false);

        // Line 40: fix NEWLINE, but still lots of corruption within the line!
        fileBytes = BitUtils.insert(fileBytes, 8 * (16614 - ZX81SysVars.SAVE_START + 4 + 6), false);

//        Line l1 = new Line(1, new int[] {234, 11, 43, 55, 52, 44, 44, 46, 51, 44, 11, 118});
//        BitUtils.printLine(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START), 12, l1);
//
//        BitUtils.printLine(fileBytes, 8 * (16525 - ZX81SysVars.SAVE_START), 40);
//
//        Line l10 = new Line(10, new int[] {241, 45, 20, 1, 5, 126, 132, 112, 0, 0, 0, 118});
//        BitUtils.printLine(fileBytes, 8 * (16569 - ZX81SysVars.SAVE_START), 12, l10);
//
//        BitUtils.printLine(fileBytes, 8 * (16585 - ZX81SysVars.SAVE_START), 11);
//
//        BitUtils.printLine(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START), 10);
        
        Line l40 = new Line(40, new int[] {241, 50, 20, 207, 16, 64, 23, 29, 28, 126, 132, 32, 0, 0, 0, 21, 29, 126, 129, 0, 0, 0, 0, 17, 118});
        BitUtils.printLine(fileBytes, 8 * (16614 - ZX81SysVars.SAVE_START), 25, l40);

        byte[] one = ZX81Numbers.getNumberValues(1.0);
        System.out.println(one[0] & 255);
        System.out.println(Arrays.toString(one));

        byte[] ten = ZX81Numbers.getNumberValues(10);
        System.out.println(ten[0] & 255);
        System.out.println(Arrays.toString(ten));

    }
}
