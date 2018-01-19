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
        byte[] fileBytes = FroggingProgram.loadAndEditFileBytes();

        System.out.println("=== File");
        System.out.println("Length: " + fileBytes.length);
        System.out.println("Max address: " + (fileBytes.length + ZX81SysVars.SAVE_START));
        System.out.println("Program end offset: " + ZX81SysVars.getVariableValueOffset(fileBytes, ZX81SysVars.D_FILE, 2));
        System.out.println("Program end address: " + ZX81SysVars.getVariableValue(fileBytes, ZX81SysVars.D_FILE, 2));

        System.out.println("=== System variables");
        StringBuffer var = new StringBuffer();
        ZX81SysVars.dumpSystemVariables(fileBytes, 0, ZX81SysVars.SAVE_START, var);
        System.out.println(var);

        System.out.println("=== Program reconstruction");
        BitUtils.printLine(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START), 12);        // 1
        BitUtils.printLine(fileBytes, 8 * (16525 - ZX81SysVars.SAVE_START), 40);        // 5
        BitUtils.printLine(fileBytes, 8 * (16569 - ZX81SysVars.SAVE_START), 12);        // 10
        BitUtils.printLine(fileBytes, 8 * (16585 - ZX81SysVars.SAVE_START), 11);        // 20
        BitUtils.printLine(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START), 10);        // 30
        BitUtils.printLine(fileBytes, 8 * (16614 - ZX81SysVars.SAVE_START), 20);        // 40
        BitUtils.printLine(fileBytes, 8 * (16643 - ZX81SysVars.SAVE_START), 13);        // 50
        BitUtils.printLine(fileBytes, 8 * (16660 - ZX81SysVars.SAVE_START), 5);         // 60
        BitUtils.printLine(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START), 13);        // 6x
        BitUtils.printLine(fileBytes, 8 * (16691 - ZX81SysVars.SAVE_START), 13);        // 70 (1 bit difference between line number 6 and 70)
        BitUtils.printLine(fileBytes, 8 * (16708 - ZX81SysVars.SAVE_START), 10);        // 80
        BitUtils.printLine(fileBytes, 8 * (16722 - ZX81SysVars.SAVE_START), 3);         // 90
        BitUtils.printLine(fileBytes, 8 * (16729 - ZX81SysVars.SAVE_START), 12);        // 100
        BitUtils.printLine(fileBytes, 8 * (16745 - ZX81SysVars.SAVE_START), 21);        // 120
        BitUtils.printLine(fileBytes, 8 * (16770 - ZX81SysVars.SAVE_START), 21);        // 130
        BitUtils.printLine(fileBytes, 8 * (16795 - ZX81SysVars.SAVE_START), 20);        // 140
        BitUtils.printLine(fileBytes, 8 * (16819 - ZX81SysVars.SAVE_START), 20);        // 150
        BitUtils.printLine(fileBytes, 8 * (16843 - ZX81SysVars.SAVE_START), 10);        // 160
        BitUtils.printLine(fileBytes, 8 * (16857 - ZX81SysVars.SAVE_START), 13);        // 170
        BitUtils.printLine(fileBytes, 8 * (16874 - ZX81SysVars.SAVE_START), 35);        // 180 IF ... THEN GOTO ...
        BitUtils.printLine(fileBytes, 8 * (16913 - ZX81SysVars.SAVE_START), 10);        // 190
        BitUtils.printLine(fileBytes, 8 * (16927 - ZX81SysVars.SAVE_START), 3);         // 200
        BitUtils.printLine(fileBytes, 8 * (16934 - ZX81SysVars.SAVE_START), 2);         // 210 (similar to 146)
        BitUtils.printLine(fileBytes, 8 * (16940 - ZX81SysVars.SAVE_START), 25);        // 220
        BitUtils.printLine(fileBytes, 8 * (16969 - ZX81SysVars.SAVE_START), 10);        // 230
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
