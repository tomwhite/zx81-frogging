import sinclair.basic.ZX81SysVars;

/**
 * Convert a P file to a ASCII listing that can be compiled by http://freestuff.grok.co.uk/zxtext2p/index.html.
 */
public class ZX81PToListing {

    private static class ListingLine {
        private final int number;
        private final int length;
        private final String listing;

        public ListingLine(int number, int length, String listing) {
            this.number = number;
            this.length = length;
            this.listing = listing;
        }

        public int getNumber() {
            return number;
        }

        public int getLength() {
            return length;
        }

        public String getListing() {
            return listing;
        }
    }

    private static final String NAK = "|";

    private static final String[] ZX81_TO_ASCII_TABLE = {
        /* 000-009 */ " ", "\\' ", "\\ '", "\\''", "\\. ", "\\: ", "\\.'", "\\:'", "\\##", "\\, , ",
        /* 010-019 */ "\\~~", "\"", "£", "$", ":", "?", "(", ")", ">", "<",
        /* 020-029 */ "=", "+", "-", "*", "/", ";", ",", ".", "0", "1",
        /* 030-039 */ "2", "3", "4", "5", "6", "7", "8", "9", "A", "B",
        /* 040-049 */ "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
        /* 050-059 */ "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
        /* 060-069 */ "W", "X", "Y", "Z", "RND", "INKEY$", "PI", NAK, NAK, NAK,
        /* 070-079 */ NAK, NAK, NAK, NAK, NAK,  NAK, NAK, NAK, NAK, NAK,
        /* 080-089 */ NAK, NAK, NAK, NAK, NAK,  NAK, NAK, NAK, NAK, NAK,
        /* 090-099 */ NAK, NAK, NAK, NAK, NAK,  NAK, NAK, NAK, NAK, NAK,
        /* 100-109 */ NAK, NAK, NAK, NAK, NAK,  NAK, NAK, NAK, NAK, NAK,
        /* 110-119 */ NAK, NAK, NAK, NAK, NAK,  NAK, NAK, NAK, NAK, NAK,
        /* 120-129 */ NAK, NAK, NAK, NAK, NAK,  NAK, NAK, NAK, "\\::", "\\.:",
        /* 130-139 */ "\\:.", "\\..", "\\':", "\\ :", "\\'.", "\\ .", "\\@@", "\\;;", "\\!!", "%\"",
        /* 140-149 */ "%£", "%$", "%:", "%?", "%(", "%)", "%>", "%<", "%=", "%+",
        /* 150-159 */ "%-", "%*", "%/", "%;", "%, ", "%.", "%0", "%1", "%2", "%3",
        /* 160-169 */ "%4", "%5", "%6", "%7", "%8", "%9", "%A", "%B", "%C", "%D",
        /* 170-179 */ "%E", "%F", "%G", "%H", "%I", "%J", "%K", "%L", "%M", "%N",
        /* 180-189 */ "%O", "%P", "%Q", "%R", "%S", "%T", "%U", "%V", "%W", "%X",
        /* 190-199 */ "%Y", "%Z", "\\\"", "AT ", "TAB ", NAK, "CODE ", "VAL ", "LEN ", "SIN ",
        /* 200-209 */ "COS ", "TAN ", "ASN ", "ACS ", "ATN ", "LN ", "EXP ", "INT ", "SQR ", "SGN ",
        /* 210-219 */ "ABS ", "PEEK ", "USR ", "STR$ ", "CHR$ ", "NOT ", "**", " OR ", " AND ", "<=",
        /* 220-229 */ ">=", "<>", " THEN ", " TO ", " STEP ", "LPRINT ", "LLIST ", "STOP ", "SLOW ", "FAST ",
        /* 230-239 */ "NEW ", "SCROLL ", "CONT ", "DIM ", "REM ", "FOR ", "GOTO ", "GOSUB ", "INPUT ", "LOAD ",
        /* 240-249 */ "LIST ", "LET ", "PAUSE ", "NEXT ", "POKE ", "PRINT ", "PLOT ", "RUN ", "SAVE ", "RAND ",
        /* 250-255 */ "IF ", "CLS ", "UNPLOT ", "CLEAR ", "RETURN ", "COPY "
    };

    public static String pFileToListing(byte[] memory) {
        int offset = 16509 - ZX81SysVars.SAVE_START;
        int end = ZX81SysVars.getVariableValueOffset(memory, 16396, 2);
        if (end < 0) {
            end = ZX81SysVars.getVariableValueOffset(memory, 16400, 2);
        }
        if (end > memory.length) {
            end = memory.length;
        }
        StringBuilder sb = new StringBuilder();
        while (offset < end - 2) {
            ListingLine listingLine = lineToListingLine(memory, offset);
            sb.append(listingLine.getListing());
            offset += listingLine.length + 4;
        }
        return sb.toString();
    }

    private static ListingLine lineToListingLine(byte[] memory, int offset) {
        StringBuilder sb = new StringBuilder();
        int pos = offset;
        int number = readLineNumber(memory, pos);
        pos += 2;
        int length = readLineLength(memory, pos);
        pos += 2;
        int end = pos + length;

        String pad = number < 10 ? "   " : (number < 100 ? "  " : (number < 1000 ? " " : ""));
        sb.append(pad).append(number).append(" ");

        boolean remStatement = (memory[pos] & 255) == 234;

        int ch;
        while (pos < end - 1) {
            ch = memory[pos++] & 255;
            if (ch == 126 && !remStatement) {
                pos += 5;
            } else {
                sb.append(translateZX81ToASCII(ch));
            }
        }

        sb.append("\n");

        return new ListingLine(number, length, sb.toString());
    }

    public static String lineToListing(byte[] memory, int offset) {
        return lineToListingLine(memory, offset).getListing();
    }

    private static String translateZX81ToASCII(int ch) {
        return ZX81_TO_ASCII_TABLE[ch];
    }

    public static int readLineNumber(byte[] binary, int offset) {
        return ((binary[offset] & 255) << 8) + (binary[offset + 1] & 255);
    }

    public static int readLineLength(byte[] binary, int offset) {
        return (binary[offset] & 255) + ((binary[offset + 1] & 255) << 8);
    }
}
