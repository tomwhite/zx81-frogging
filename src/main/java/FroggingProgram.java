import sinclair.basic.ZX81SysVars;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FroggingProgram {

    public static byte[] loadReconstructionFileBytes() throws IOException {
        File f = new File("pfiles/frogging-reconstruction.p");
        FileInputStream fis = new FileInputStream(f);
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }

    public static byte[] loadFileBytes() throws IOException {
        File f = new File("pfiles/frogging-normalized.1.program1.p");
        FileInputStream fis = new FileInputStream(f);
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }

    public static byte[] loadAndEditFileBytes() throws IOException {
        byte[] fileBytes = loadFileBytes();

        // ins two bits to see if we can get frogging program out...
        fileBytes = BitUtils.ins(fileBytes, 8 * (ZX81SysVars.PRBUFF - ZX81SysVars.SAVE_START), false);
        fileBytes = BitUtils.ins(fileBytes, 8 * (ZX81SysVars.MEMBOT - ZX81SysVars.SAVE_START), false);

        // Line 1: ins 2 bits to make sure it works
        fileBytes = BitUtils.ins(fileBytes, 8 * 116, false);
        fileBytes = BitUtils.ins(fileBytes, 8 * 118, false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 2) + 1, false); // line length
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 2) + 4, true); // line length
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 2) + 5, true); // line length
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 2) + 6, false); // line length
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 2) + 7, false); // line length
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 3) + 2, false); // line length
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 7) + 5, true);
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 7) + 6, true); // C -> I
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 2, true); // ( -> N
        fileBytes = BitUtils.set(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 6, true); // ( -> N
        fileBytes = BitUtils.del(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 7);
        fileBytes = BitUtils.del(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START + 4 + 8) + 7);

        // Line 5: ins a bit to make sure ends with a " to close the print
        // 5 PRINT AT 16,3;"{6}{H}{F}{F}{6}{E}{E}{E}{E}{E}{E}{E}{E}{E}{E}{E}+{T}"
        fileBytes = BitUtils.ins(fileBytes, 8 * (16525 - ZX81SysVars.SAVE_START + 4 + 38), false);

        // Line 10: fix NEWLINE
        fileBytes = BitUtils.set(fileBytes, 8 * (16569 - ZX81SysVars.SAVE_START + 4 + 11) + 1, true);

        // Line 30: ins 1 bits (PRINT AT H,V ...)
        fileBytes = BitUtils.ins(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START + 4 + 6), false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START + 4 + 6) + 2, false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START + 4 + 6) + 5, false);

        // Line 40: fix NEWLINE, but still lots of corruption within the line!
        fileBytes = BitUtils.ins(fileBytes, 8 * (16614 - ZX81SysVars.SAVE_START + 8) + 7, false);

        // Line 60: fix NEWLINE, but still some corruption within
        fileBytes = BitUtils.ins(fileBytes, 8 * (16660 - ZX81SysVars.SAVE_START + 3), false);

        // Line 61 (or line after 60): fix NEWLINE, but still some corruption within
        fileBytes = BitUtils.set(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START + 1) + 2, true); // line number
        fileBytes = BitUtils.set(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START + 1) + 3, true); // line number
        fileBytes = BitUtils.set(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START + 1) + 4, true); // line number
        fileBytes = BitUtils.set(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START + 1) + 5, true); // line number
        fileBytes = BitUtils.del(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START + 4) + 1);
        fileBytes = BitUtils.set(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START + 4) + 1, true);
        fileBytes = BitUtils.set(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START + 4) + 2, true);
        fileBytes = BitUtils.del(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START + 11));

        // Line 70: fix NEWLINE
        fileBytes = BitUtils.set(fileBytes, 8 * (16691 - ZX81SysVars.SAVE_START + 1) + 1, true); // line number
        fileBytes = BitUtils.del(fileBytes, 8 * (16691 - ZX81SysVars.SAVE_START + 11) + 7);

        // Line 100: fix NEWLINE
        fileBytes = BitUtils.ins(fileBytes, 8 * (16729 - ZX81SysVars.SAVE_START + 1), false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16729 - ZX81SysVars.SAVE_START + 15) + 1, true); // NL

        // Line 120: fix NEWLINE
        fileBytes = BitUtils.del(fileBytes, 8 * (16745 - ZX81SysVars.SAVE_START + 15));
        fileBytes = BitUtils.set(fileBytes, 8 * (16745 - ZX81SysVars.SAVE_START + 24) + 2, true); // NL

        // Line 140: fix NEWLINE
        fileBytes = BitUtils.del(fileBytes, 8 * (16795 - ZX81SysVars.SAVE_START + 10) + 3); // THEN
        fileBytes = BitUtils.ins(fileBytes, 8 * (16795 - ZX81SysVars.SAVE_START + 16), false); // 1
        fileBytes = BitUtils.ins(fileBytes, 8 * (16795 - ZX81SysVars.SAVE_START + 18) + 5, false); // NL

        // Line 150: fix NEWLINE
        fileBytes = BitUtils.ins(fileBytes, 8 * (16819 - ZX81SysVars.SAVE_START), false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16819 - ZX81SysVars.SAVE_START + 1) + 2, false); // line number
        fileBytes = BitUtils.set(fileBytes, 8 * (16819 - ZX81SysVars.SAVE_START + 1) + 3, true); // line number

        // Line 160: fix NEWLINE
        fileBytes = BitUtils.set(fileBytes, 8 * (16843 - ZX81SysVars.SAVE_START + 13) + 5, true); // NL

        // Line 170: fix NEWLINE
        fileBytes = BitUtils.ins(fileBytes, 8 * (16857 - ZX81SysVars.SAVE_START + 16), false); // NL

        // Line 190: fix line length
        fileBytes = BitUtils.set(fileBytes, 8 * (16913 - ZX81SysVars.SAVE_START + 2) + 4, true); // line length

        // Line 200: fix NEWLINE
        fileBytes = BitUtils.ins(fileBytes, 8 * (16927 - ZX81SysVars.SAVE_START + 2), false);
        fileBytes = BitUtils.set(fileBytes, 8 * (16927 - ZX81SysVars.SAVE_START + 4), true); // NEXT

        // Line 210: fix NEWLINE
        fileBytes = BitUtils.set(fileBytes, 8 * (16934 - ZX81SysVars.SAVE_START + 1) + 1, true); // line number
        fileBytes = BitUtils.set(fileBytes, 8 * (16934 - ZX81SysVars.SAVE_START + 5) + 5, true); // NL
        fileBytes = BitUtils.set(fileBytes, 8 * (16934 - ZX81SysVars.SAVE_START + 5) + 6, true); // NL

        // Line 220: fix NEWLINE
        fileBytes = BitUtils.set(fileBytes, 8 * (16940 - ZX81SysVars.SAVE_START + 2) + 4, true); // line length
        fileBytes = BitUtils.set(fileBytes, 8 * (16940 - ZX81SysVars.SAVE_START + 2) + 7, true); // line length
        fileBytes = BitUtils.ins(fileBytes, 8 * (16940 - ZX81SysVars.SAVE_START + 10), false);
        fileBytes = BitUtils.del(fileBytes, 8 * (16940 - ZX81SysVars.SAVE_START + 26) + 7); // graphic char - hard to know what is right here

        // Line 230: fix start
        fileBytes = BitUtils.ins(fileBytes, 8 * (16969 - ZX81SysVars.SAVE_START), false);

        return fileBytes;
    }


    public static void main(String[] args) throws Exception {
        byte[] fileBytes = FroggingProgram.loadAndEditFileBytes();

        Line l1 = new Line(1, new int[] {234, 11, 43, 55, 52, 44, 44, 46, 51, 44, 11, 118});
        BitUtils.printLine(fileBytes, 8 * (16509 - ZX81SysVars.SAVE_START), 12, l1);

        BitUtils.printLine(fileBytes, 8 * (16525 - ZX81SysVars.SAVE_START), 40);

        Line l10 = new Line(10, new int[] {241, 45, 20, 1, 5, 126, 132, 112, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16569 - ZX81SysVars.SAVE_START), 12, l10);

        BitUtils.printLine(fileBytes, 8 * (16585 - ZX81SysVars.SAVE_START), 11);

        BitUtils.printLine(fileBytes, 8 * (16600 - ZX81SysVars.SAVE_START), 10);

        Line l40 = new Line(40, new int[] {241, 50, 20, 207, 16, 64, 23, 29, 28, 126, 132, 32, 0, 0, 0, 21, 29, 126, 129, 0, 0, 0, 0, 17, 118});
        BitUtils.printLine(fileBytes, 8 * (16614 - ZX81SysVars.SAVE_START), 25, l40);

        BitUtils.printLine(fileBytes, 8 * (16643 - ZX81SysVars.SAVE_START), 13);

        Line l60 = new Line(60, new int[] {245, 193, 45, 26, 59, 25, 11, 0, 11, 118});
        BitUtils.printLine(fileBytes, 8 * (16660 - ZX81SysVars.SAVE_START), 10, l60);

        Line l61 = new Line(61, new int[] {241, 45, 20, 45, 22, 29, 126, 129, 0, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16674 - ZX81SysVars.SAVE_START), 13, l61);

        Line l70 = new Line(70, new int[] {241, 59, 20, 59, 21, 29, 126, 129, 0, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16691 - ZX81SysVars.SAVE_START), 13, l70);

        BitUtils.printLine(fileBytes, 8 * (16708 - ZX81SysVars.SAVE_START), 10);

        BitUtils.printLine(fileBytes, 8 * (16722 - ZX81SysVars.SAVE_START), 3);

        Line l100 = new Line(100, new int[] {241, 38, 20, 29, 29, 126, 132, 48, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16729 - ZX81SysVars.SAVE_START), 12, l100);

        Line l120 = new Line(120, new int[] {235, 46, 20, 29, 126, 129, 0, 0, 0, 0, 223, 50, 21, 29, 126, 129, 0, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16745 - ZX81SysVars.SAVE_START), 21, l120);

        BitUtils.printLine(fileBytes, 8 * (16770 - ZX81SysVars.SAVE_START), 21);

        Line l140 = new Line(140, new int[] {250, 65, 20, 11, 33, 11, 222, 241, 38, 20, 38, 22, 29, 126, 129, 0, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16795 - ZX81SysVars.SAVE_START), 20, l140);

        Line l150 = new Line(150, new int[] {250, 65, 20, 11, 36, 11, 222, 241, 38, 20, 38, 21, 29, 126, 129, 0, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16819 - ZX81SysVars.SAVE_START), 20, l150);

        Line l160 = new Line(160, new int[] {245, 193, 45, 26, 59, 25, 11, 0, 11, 118});
        BitUtils.printLine(fileBytes, 8 * (16843 - ZX81SysVars.SAVE_START), 10, l160);

        Line l170 = new Line(170, new int[] {241, 45, 20, 45, 21, 29, 126, 129, 0, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16857 - ZX81SysVars.SAVE_START), 13, l170);

        Line l180 = new Line(180, new int[] {250, 45, 20, 29, 32, 126, 132, 96, 0, 0, 0, 218, 59, 20, 38, 21, 30, 126, 130, 0, 0, 0, 0, 222, 236, 30, 30, 28, 126, 136, 92, 0, 0, 0, 118});
        BitUtils.printLine(fileBytes, 8 * (16874 - ZX81SysVars.SAVE_START), 35, l180);

        Line l190 = new Line(190, new int[] {245, 193, 45, 26, 59, 25, 11, 151, 11, 118});
        BitUtils.printLine(fileBytes, 8 * (16913 - ZX81SysVars.SAVE_START), 10, l190);

        Line l200 = new Line(200, new int[] {243, 46, 118});
        BitUtils.printLine(fileBytes, 8 * (16927 - ZX81SysVars.SAVE_START), 3, l200);

        Line l210 = new Line(210, new int[] {227, 118});
        BitUtils.printLine(fileBytes, 8 * (16934 - ZX81SysVars.SAVE_START), 2, l210);

        Line l220 = new Line(220, new int[] {245, 193, 29, 32, 126, 132, 96, 0, 0, 0, 26, 38, 21, 30, 126, 130, 0, 0, 0, 0, 25, 11, 144, 11, 118});
        BitUtils.printLine(fileBytes, 8 * (16940 - ZX81SysVars.SAVE_START), 25, l220);

        Line l230 = new Line(230, new int[] {245, 193, });
        BitUtils.printLine(fileBytes, 8 * (16969 - ZX81SysVars.SAVE_START), 10, l230);

//        byte[] one = ZX81Numbers.getNumberValues(1.0);
//        System.out.println(one[0] & 255);
//        System.out.println(Arrays.toString(one));
//
//        byte[] ten = ZX81Numbers.getNumberValues(10);
//        System.out.println(ten[0] & 255);
//        System.out.println(Arrays.toString(ten));
//
//        System.out.println(ZX81Numbers.debugNumberValues(11));
//        System.out.println(ZX81Numbers.debugNumberValues(14));
//        System.out.println(ZX81Numbers.getNumber(new byte[] { (byte) (136 & 255), (byte) 92, 0, 0, 0}, 0));
//        System.out.println(ZX81Numbers.getNumber(new byte[] { 0, 0, 0, (byte) (8 & 255), 0}, 0));

    }
}
