import sinclair.basic.ZX81SysVars;

import java.io.File;
import java.io.FileInputStream;

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

        // insert 2 bits to make sure line 1 works. Line length is still wrong
        //
        fileBytes = BitUtils.insert(fileBytes, 8 * 116, false);
        fileBytes = BitUtils.insert(fileBytes, 8 * 118, false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 7) + 5, true);
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 7) + 6, true); // C -> I
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 2, true); // ( -> N
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 6, true); // ( -> N
        fileBytes = BitUtils.delete(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 7);
        fileBytes = BitUtils.delete(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 7);

        // insert a bit to make sure line 2 ends with a " to close the print
        // 5 PRINT AT 16,3;"{6}{H}{F}{F}{6}{E}{E}{E}{E}{E}{E}{E}{E}{E}{E}{E}+{T}"
        fileBytes = BitUtils.insert(fileBytes, 8 * (16525 - ZX81SysVars.SAVE_START + 4 + 38), false);

        // line 3, fix NEWLINE
        fileBytes = BitUtils.set(fileBytes, 8 * (16569 - ZX81SysVars.SAVE_START + 4 + 11) + 1, true);


        Line l1 = new Line(1, new int[] {234, 11, 43, 55, 52, 44, 44, 46, 51, 44, 11, 118});
        BitUtils.printLine(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START), 12, l1);

        BitUtils.printLine(fileBytes, 8 * (16525 - ZX81SysVars.SAVE_START), 40);

        Line l3 = new Line(10, new int[] {241, 45, 20, 1, 5, 126, 132, 112, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16569 - ZX81SysVars.SAVE_START), 12, l3);

        BitUtils.printLine(fileBytes, 8 * (16585 - ZX81SysVars.SAVE_START), 11);
    }
}
