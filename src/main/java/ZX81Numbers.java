public class ZX81Numbers {
    public static double getNumber(byte[] content, int pos) {
        // Based on sinclair.basic.ZX81Basic.getNumber
        if (pos > content.length - 5) {
            throw new IllegalArgumentException();
        } else {
            long E = (long)(content[pos] & 255);
            long A = (long)(content[pos + 1] & 255);
            long B = (long)(content[pos + 2] & 255);
            long C = (long)(content[pos + 3] & 255);
            long D = (long)(content[pos + 4] & 255);
            if (A == 0 && B == 0 && C == 0 && D == 0 && E == 0) {
                return 0.0;
            }
            return (double)(2 * (A < 128L ? 1 : 0) - 1) * Math.pow(2.0D, (double)(E - 160L)) * (double)(((256L * (A + (long)(128 * (A < 128L ? 1 : 0))) + B) * 256L + C) * 256L + D);
        }
    }

    public static byte[] getNumberValues(double num) {
        // based on http://freestuff.grok.co.uk/zxtext2p/
        byte[] content = new byte[5];
        if (num == 0.0) {
            return content;
        }
        if (num < 0) {
            num = -num;
        }

        int exp = (int) Math.floor(Math.log(num)/Math.log(2));

        if (exp < -129 || exp > 126) {
            throw new IllegalArgumentException("Exponent too big");
        }

        long man = (long) Math.floor ( (num/Math.pow(2,exp) - 1) * 0x80000000L + 0.5);
        man = man & 0x7fffffffL;

        exp += 129;

        content[0] = (byte) (exp & 255);
        content[1] = (byte) (man >> 24 & 255);
        content[2] = (byte) (man >> 16 & 255);
        content[3] = (byte) (man >> 8 & 255);
        content[4] = (byte) (man & 255);

        return content;
    }
}
