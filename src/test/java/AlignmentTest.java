import org.biojava.bio.alignment.AlignmentPair;
import org.junit.Test;

public class AlignmentTest {
    @Test
    public void test() throws Exception {

        String line0Corrupted =
                "00000000 00000000 11000011 00100000 11101010 00001011 00101011 00110111 00110100 00101100 00101100 00101000 00010000 01001011 00000010 11011101 10".replace(" ", "");
        String line0Target =
                "00000000 00000001 00001100 00000000 11101010 00001011 00101011 00110111 00110100 00101100 00101100 00101110 00110011 00101100 00001011 01110110".replace(" ", "");

        AlignmentPair alignmentPair = AlignmentUtils.align(line0Corrupted, line0Target);
        System.out.println(alignmentPair.formatOutput(1000));

        System.out.println(alignmentPair.getQuery().seqString());
        System.out.println(alignmentPair.getSubject().seqString());
    }
}
