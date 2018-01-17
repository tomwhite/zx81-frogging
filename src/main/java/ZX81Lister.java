import sinclair.basic.ZX81Basic;
import sinclair.basic.ZX81SysVars;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by tom on 25/03/2016.
 * This is basically PFileUtils
 */
public class ZX81Lister {
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

        System.out.println("=== File");
        System.out.println("Length: " + fileBytes.length);
        System.out.println("Max address: " + (fileBytes.length + ZX81SysVars.SAVE_START));
        System.out.println("Program end offset: " + ZX81SysVars.getVariableValueOffset(fileBytes, ZX81SysVars.D_FILE, 2));
        System.out.println("Program end address: " + ZX81SysVars.getVariableValue(fileBytes, ZX81SysVars.D_FILE, 2));

        System.out.println("=== System variables");
        StringBuffer var = new StringBuffer();
        ZX81SysVars.dumpSystemVariables(fileBytes, 0, ZX81SysVars.SAVE_START, var);
        System.out.println(var);

        System.out.println("=== Newlines (16476 is end of PRBUF, then program block, then D_FILE block)");
        for (int i = 0; i < fileBytes.length; i++) {
            if ((fileBytes[i] & 255) == 118) {
                System.out.println(ZX81SysVars.SAVE_START + i);
            }
        }

        // insert 2 bits to make sure line 1 works. Line length is still wrong
        //
        byte[] fileBytes1 = BitUtils.insert(fileBytes, 8 * 116, false);
        fileBytes1 = BitUtils.insert(fileBytes1, 8 * 118, false);
        byte[] line1 = new byte[12];
        System.arraycopy(fileBytes1, 116 + 4, line1, 0, line1.length);
        StringBuffer sb1 = new StringBuffer();
        ZX81Basic.dumpLine(line1, 1, false, true, true, sb1);
        System.out.print(sb1.toString());

        // insert a bit to make sure line 2 ends with a " to close the print
        // 5 PRINT AT 16,3;"{6}{H}{F}{F}{6}{E}{E}{E}{E}{E}{E}{E}{E}{E}{E}{E}+{T}"
        fileBytes = BitUtils.insert(fileBytes, 8 * (16525 - ZX81SysVars.SAVE_START + 4 + 38), false);
        byte[] line2 = new byte[40];
        System.arraycopy(fileBytes, 16525 - ZX81SysVars.SAVE_START + 4, line2, 0, line2.length);
        System.out.println("line2 end byte: " + (line2[line2.length - 1] & 255));
        StringBuffer sb2 = new StringBuffer();
        ZX81Basic.dumpLine(line2, 5, false, true, true, sb2);
        System.out.print(sb2.toString());

        // insert 1 bits for line 5 (PRINT AT H,V) ?
        fileBytes = BitUtils.insert(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START + 4 + 6), false);

        // set 1 bits for NL on line 6 (LET M=) ? Converts 74_GRAPHICS to NEWLINE
//        fileBytes = BitUtils.set(fileBytes, 8 * (16614 - ZX81SysVars.SAVE_START + 4
//            + 7) + 6, true);

        System.out.println("=== Program reconstruction");
        BitUtils.printLine(fileBytes1, 8 * (16509 - ZX81SysVars.SAVE_START), 12, "1 REM \"FROGGING\"");
        BitUtils.printLine(fileBytes, 8 * (16525 - ZX81SysVars.SAVE_START), 40);        // 5
        BitUtils.printLine(fileBytes, 8 * (16569 - ZX81SysVars.SAVE_START), 12);        // 10
        BitUtils.printLine(fileBytes, 8 * (16585 - ZX81SysVars.SAVE_START), 11);        // 20
        BitUtils.printLine(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START), 10);        // 30
        BitUtils.printLine(fileBytes, 8 * (16614 - ZX81SysVars.SAVE_START), 20);        // 40 LET M=INT(... ? number at 16626 (+6 bit offset)
        BitUtils.printLine(fileBytes, 8 * (16642 - ZX81SysVars.SAVE_START) + 7, 13);    // 50
        BitUtils.printLine(fileBytes, 8 * (16659 - ZX81SysVars.SAVE_START) + 7, 5);// nope, probably not a FOR as there is only one NEXT, could be PRINT
        System.out.println("...missing");
        BitUtils.printLine(fileBytes, 8 * (16691 - ZX81SysVars.SAVE_START), 13);        // 70 (1 bit difference between line number 6 and 70)
        BitUtils.printLine(fileBytes, 8 * (16708 - ZX81SysVars.SAVE_START) + 1, 10);    // 80
        BitUtils.printLine(fileBytes, 8 * (16722 - ZX81SysVars.SAVE_START) + 1, 3);     // 90
        BitUtils.printLine(fileBytes, 8 * (16729 - ZX81SysVars.SAVE_START), 12);        // 100
        BitUtils.printLine(fileBytes, 8 * (16745 - ZX81SysVars.SAVE_START), 21);        // 120 FOR I=1 TO
        BitUtils.printLine(fileBytes, 8 * (16770 - ZX81SysVars.SAVE_START) + 1, 21);    // 130
        BitUtils.printLine(fileBytes, 8 * (16795 - ZX81SysVars.SAVE_START) + 1, 20);    // 140 IF ... THEN GOTO ...
        System.out.println("...missing");
        BitUtils.printLine(fileBytes, 8 * (16842 - ZX81SysVars.SAVE_START) + 7, 10);    // 160
        BitUtils.printLine(fileBytes, 8 * (16856 - ZX81SysVars.SAVE_START) + 7, 13);    // 170 LET H=H+1
        BitUtils.printLine(fileBytes, 8 * (16873 - ZX81SysVars.SAVE_START) + 6, 35);    // 180 IF ... THEN GOTO ...
        BitUtils.printLine(fileBytes, 8 * (16912 - ZX81SysVars.SAVE_START) + 6, 10);    // 190
        BitUtils.printLine(fileBytes, 8 * (16926 - ZX81SysVars.SAVE_START) + 6, 3);     // 200 NEXT I ?
        BitUtils.printLine(fileBytes, 8 * (16933 - ZX81SysVars.SAVE_START) + 5, 2);     // 210 (similar to 146)
        BitUtils.printLine(fileBytes, 8 * (16939 - ZX81SysVars.SAVE_START) + 5, 16);    // 220
        BitUtils.printLine(fileBytes, 8 * (16968 - ZX81SysVars.SAVE_START) + 4, 10);     // 230
        // up to 17013

        System.out.println("=== Newlines at any bit offset");
        BitUtils.findNewlines(fileBytes, 16509 - ZX81SysVars.SAVE_START);

        System.out.println("=== REM at any bit offset");
        BitUtils.find(fileBytes, (byte) 234);

        System.out.println("=== NEXT at any bit offset");
        BitUtils.find(fileBytes, (byte) 243);

        System.out.println("=== THEN at any bit offset");
        BitUtils.find(fileBytes, (byte) 222);

        System.out.println("=== GOTO at any bit offset");
        BitUtils.find(fileBytes, (byte) 236);

        System.out.println("=== I at any bit offset");
        BitUtils.find(fileBytes, (byte) 46);

        System.out.println("=== INKEY$ at any bit offset");
        BitUtils.find(fileBytes, (byte) 65);

        System.out.println("=== Line number 150 at any bit offset");
        BitUtils.findLineNumber(fileBytes, 150);

        System.out.println("=== number at any bit offset");
        BitUtils.find(fileBytes, (byte) 126);


//        System.out.println("=== Program");
////        for (int of = 0; of < 16; of++) {
////            // finds REM (234) "(11) F(43) R(55) O(52) G(44) G(44) ... for of = 2!
////            BitUtils.printLineNumberAndLength(fileBytes, 8 * 116 - of);
////        }
//        BitUtils.printByteAt(fileBytes, 116);
//        BitUtils.printByteAt(fileBytes, 117);// first line number is 12, not 10 - perhaps a 0 bit was dropped? try inserting one?
//        BitUtils.printByteAt(fileBytes, 118);
//        BitUtils.printByteAt(fileBytes, 119);
//        int ln = ((fileBytes[116] & 255) << 8) + (fileBytes[117] & 255);
//        int ll = (fileBytes[118] & 255) + ((fileBytes[119] & 255) << 8);
//        System.out.println("ln: " + ln);
//        System.out.println("ll: " + ll);
//        Map<Integer, byte[]> programLines = ZX81Basic.getProgramLines(fileBytes);
//        for (Map.Entry<Integer, byte[]> line : programLines.entrySet()) {
//            int lineNumber = line.getKey();
//            StringBuffer sb = new StringBuffer();
//            ZX81Basic.dumpLine(line.getValue(), lineNumber, false, true, true, sb);
//            System.out.print(sb.toString());
//        }

//        System.out.println("=== Variables");
//        Map variables = ZX81Basic.getVariables(fileBytes);
//        System.out.println(variables);
//
//        System.out.println("=== Variable memory");
//        Map variableMemory = ZX81Basic.getVariableMemory(fileBytes);
//        System.out.println(variableMemory);

    }
}
