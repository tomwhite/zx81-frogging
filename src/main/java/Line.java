import sinclair.basic.ZX81Translate;

public class Line {
    private int number;
    private int length;
    private int[] content;

    public Line(int number, int[] content) {
        this.number = number;
        this.length = content.length;
        this.content = content;
    }

    public int getNumber() {
        return number;
    }

    public int getLength() {
        return length;
    }

    public int[] getContent() {
        return content;
    }

    public void print() {
        StringBuilder zx81Line = new StringBuilder();
        StringBuilder zx81SymbolString = new StringBuilder();
        StringBuilder byteString = new StringBuilder();
        StringBuilder bitString = new StringBuilder();

        zx81Line.append(number).append(" ");

        int a = (number >> 8) & 255;
        int b = number & 255;
        int c = length & 255;
        int d = (length >> 8) & 255;

        zx81SymbolString.append(String.format("%-16s", number)).append("  ");
        byteString.append(String.format("%-8s", a)).append(" ");
        byteString.append(String.format("%-8s", b)).append(" ");
        bitString.append(String.format("%8s", Integer.toBinaryString(a)).replace(' ', '0')).append(" ");
        bitString.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0')).append(" ");

        zx81SymbolString.append(String.format("%-16s", length)).append("  ");
        byteString.append(String.format("%-8s", c)).append(" ");
        byteString.append(String.format("%-8s", d)).append(" ");
        bitString.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0')).append(" ");
        bitString.append(String.format("%8s", Integer.toBinaryString(d)).replace(' ', '0')).append(" ");

        for (int i = 0; i < length; i++) {
            if (i < length - 1) { // ignore NEWLINE
                zx81Line.append(ZX81Translate.translateZX81ToASCII(content[i]));
            }
            zx81SymbolString.append(String.format("%-8s", ZX81Translate.translateZX81ToASCII(content[i]))).append(" ");
            byteString.append(String.format("%-8s", content[i])).append(" ");
            bitString.append(String.format("%8s", Integer.toBinaryString(content[i])).replace(' ', '0')).append(" ");
        }
        System.out.printf("%s\n", zx81Line);
        System.out.printf("\t%s\n", zx81SymbolString);
        System.out.printf("\t%s\n", byteString);
        System.out.printf("\t%s\n", bitString);
    }
}
