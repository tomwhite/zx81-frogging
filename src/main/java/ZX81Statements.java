import sinclair.basic.ZX81Translate;

public class ZX81Statements {

  public static boolean isStatement(byte zx81Char) {
    return isStatement((int) zx81Char);
  }

  public static boolean isStatement(int zx81Char) {
    return (zx81Char & 255) >= 225 && (zx81Char & 255) <= 255;
  }

  public static void main(String[] args) {

    for (int i = 225; i <= 255; i++) {
      System.out.printf("%s %s %s\n", i, Integer.toBinaryString(i), ZX81Translate.translateZX81ToASCII(i));
    }

    int i = 168; // [C]
    System.out.printf("%s %s %s\n", i, Integer.toBinaryString(i), ZX81Translate.translateZX81ToASCII(i));
  }
}
