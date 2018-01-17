import sinclair.basic.ZX81Basic;
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
        byte[] fileBytes1 = BitUtils.insert(fileBytes, 8 * 116, false);
        fileBytes1 = BitUtils.insert(fileBytes1, 8 * 118, false);
        byte[] fileBytes2 = BitUtils.set(fileBytes1, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 7) + 5, true);
        fileBytes2 = BitUtils.set(fileBytes2, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 7) + 6, true);

        Line l1 = new Line(1, new int[] {234, 11, 43, 55, 52, 44, 44, 46, 51, 44, 11, 118});

        BitUtils.printLine(fileBytes1, 8 * (16509 - ZX81SysVars.SAVE_START), 12, l1);
        BitUtils.printLine(fileBytes2, 8 * (16509 - ZX81SysVars.SAVE_START), 12, l1);

    }
}
