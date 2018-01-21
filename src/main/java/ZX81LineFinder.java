import sinclair.basic.ZX81SysVars;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ZX81LineFinder {

    static class CandidateLine {
        int lineStartBitPosition;
        int approximateLengthBits;

        public CandidateLine(int lineStartBitPosition, int approximateLengthBits) {
            this.lineStartBitPosition = lineStartBitPosition;
            this.approximateLengthBits = approximateLengthBits;
        }
    }

    public static void findNewLines() throws IOException {

        File f = new File("pfiles/frogging-normalized.1.program1.p");
        FileInputStream fis = new FileInputStream(f);
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        fis.close();

        Map<Integer, CandidateLine> candidateLines = new TreeMap<>();

        // get all NEWLINES at any bit offset
        List<Integer> newlines = BitUtils.findNewlines(fileBytes, ZX81SysVars.END - ZX81SysVars.SAVE_START);
        // if line length takes us to next newline, then we have a perfect match
        for (int i = 0; i < newlines.size(); i++) {
            int newlineBitPosition = newlines.get(i);
            int lineStartBitPosition = newlineBitPosition + 8;
            int a = BitUtils.getByteAtBitPosition(fileBytes, lineStartBitPosition);
            int b = BitUtils.getByteAtBitPosition(fileBytes, lineStartBitPosition + 8);
            int c = BitUtils.getByteAtBitPosition(fileBytes, lineStartBitPosition + 16);
            int d = BitUtils.getByteAtBitPosition(fileBytes, lineStartBitPosition + 24);
            int ln = ((a & 255) << 8) + (b & 255);
            int ll = (c & 255) + ((d & 255) << 8);
            if (i < newlines.size() - 1) {
                int nextNewlineBitPosition = newlines.get(i + 1);
                int expectedNextNewlineBitPosition = newlineBitPosition + (4 * 8) + (8 * ll);
                if (expectedNextNewlineBitPosition == nextNewlineBitPosition) {
                    System.out.println("Exact");
                    candidateLines.put(lineStartBitPosition, new CandidateLine(lineStartBitPosition, expectedNextNewlineBitPosition - nextNewlineBitPosition));
                    BitUtils.printLineNumberAndLength(fileBytes, lineStartBitPosition);
                } else if (expectedNextNewlineBitPosition + 1 == nextNewlineBitPosition) {
                    System.out.println("offset 1");
                    candidateLines.put(lineStartBitPosition, new CandidateLine(lineStartBitPosition, expectedNextNewlineBitPosition + 1 - nextNewlineBitPosition));
                    BitUtils.printLineNumberAndLength(fileBytes, lineStartBitPosition);
                } else if (expectedNextNewlineBitPosition - 1 == nextNewlineBitPosition) {
                    System.out.println("offset -1");
                    candidateLines.put(lineStartBitPosition, new CandidateLine(lineStartBitPosition, expectedNextNewlineBitPosition - 1 - nextNewlineBitPosition));
                    BitUtils.printLineNumberAndLength(fileBytes, lineStartBitPosition);
                } else if (expectedNextNewlineBitPosition + 2 == nextNewlineBitPosition) {
                    System.out.println("offset 2");
                    candidateLines.put(lineStartBitPosition, new CandidateLine(lineStartBitPosition, expectedNextNewlineBitPosition + 2 - nextNewlineBitPosition));
                    BitUtils.printLineNumberAndLength(fileBytes, lineStartBitPosition);
                } else if (expectedNextNewlineBitPosition - 2 == nextNewlineBitPosition) {
                    System.out.println("offset -2");
                    candidateLines.put(lineStartBitPosition, new CandidateLine(lineStartBitPosition, expectedNextNewlineBitPosition - 2 - nextNewlineBitPosition));
                    BitUtils.printLineNumberAndLength(fileBytes, lineStartBitPosition);
                }
            }
        }

        // detect dodgy line lengths by looking at bit offsets between subsequent NEWLINEs
//        for (int i = 0; i < newlines.size(); i++) {
//            int newlineBitPosition = newlines.get(i);
//            int lineStartBitPosition = newlineBitPosition + 8;
//            if (i < newlines.size() - 1) {
//                int nextNewlineBitPosition = newlines.get(i + 1);
//                if ((nextNewlineBitPosition - newlineBitPosition) % 8 == 0) {
//                    System.out.println("Exact multiple of 8 bits between subsequent NEWLINEs");
//                    candidateLines.put(lineStartBitPosition, new CandidateLine(lineStartBitPosition, nextNewlineBitPosition - nextNewlineBitPosition));
//                    BitUtils.printLineNumberAndLength(fileBytes, lineStartBitPosition);
//                } else if ((nextNewlineBitPosition - newlineBitPosition) % 8 == 1) {
//                    System.out.println("Offset of 1 for multiple of 8 bits between subsequent NEWLINEs");
//                    candidateLines.put(lineStartBitPosition, new CandidateLine(lineStartBitPosition, nextNewlineBitPosition + 1 - nextNewlineBitPosition));
//                    BitUtils.printLineNumberAndLength(fileBytes, lineStartBitPosition);
//                } else if ((nextNewlineBitPosition - newlineBitPosition) % 8 == 7) {
//                    System.out.println("Offset of 7 for multiple of 8 bits between subsequent NEWLINEs");
//                    candidateLines.put(lineStartBitPosition, new CandidateLine(lineStartBitPosition, nextNewlineBitPosition - 1 - nextNewlineBitPosition));
//                    BitUtils.printLineNumberAndLength(fileBytes, lineStartBitPosition);
//                }
//            }
//        }

        // look for sane line numbers (multiple of 10, < 1000) and lengths < 50
        for (int i = 0; i < newlines.size(); i++) {
            int newlineBitPosition = newlines.get(i);
            int lineStartBitPosition = newlineBitPosition + 8;
            int a = BitUtils.getByteAtBitPosition(fileBytes, lineStartBitPosition);
            int b = BitUtils.getByteAtBitPosition(fileBytes, lineStartBitPosition + 8);
            int c = BitUtils.getByteAtBitPosition(fileBytes, lineStartBitPosition + 16);
            int d = BitUtils.getByteAtBitPosition(fileBytes, lineStartBitPosition + 24);
            int ln = ((a & 255) << 8) + (b & 255);
            int ll = (c & 255) + ((d & 255) << 8);
            if (ln % 10 == 0 && ln < 1000 && ll > 1 && ll < 50) {
                System.out.println("Sane line number and length");
                candidateLines.put(lineStartBitPosition, new CandidateLine(lineStartBitPosition, 8 * (4 + ll)));
                BitUtils.printLineNumberAndLength(fileBytes, lineStartBitPosition);
            }
        }

        System.out.println();

        System.out.println("Number of candidate lines: " + candidateLines.size());

        System.out.println();

        for (CandidateLine candidateLine : candidateLines.values()) {
            System.out.printf("%s (+%s bit offset)\n", ZX81SysVars.SAVE_START + candidateLine.lineStartBitPosition / 8, candidateLine.lineStartBitPosition % 8);
            BitUtils.printLine(fileBytes, candidateLine.lineStartBitPosition, (candidateLine.approximateLengthBits / 8) - 4);
        }
    }

    public static void main(String[] args) throws IOException {
        findNewLines();
    }
}
