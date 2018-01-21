import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ZX81PToListingTest {
    @Test
    public void testLineToListing() {
        byte[] memory = new byte[] { 0, 40, 25, 0, (byte) (241 & 255), 50, 20, (byte) (207 & 255), 16, 64, 23, 29, 28, 126, (byte) (132 & 255), 32, 0, 0, 0, 21, 29, 126, (byte) (129 & 255), 0, 0, 0, 0, 17, 118};
        String listing = ZX81PToListing.lineToListing(memory, 0);
        assertEquals("  40 LET M=INT (RND*10+1)\n", listing);
    }

    @Test
    public void testLineToListingWithGraphics() {
        byte[] memory = new byte[] { 0, (byte) (190 & 255), 10, 0, (byte) (245 & 255), (byte) (193 & 255), 45, 26, 59, 25, 11, (byte) (151 & 255), 11, 118};
        String listing = ZX81PToListing.lineToListing(memory, 0);
        assertEquals(" 190 PRINT AT H,V;\"%*\"\n", listing);
    }

    @Test
    public void testPFileToListing() throws IOException {
        byte[] fileBytes = FroggingProgram.loadFileBytes();

        String s = ZX81PToListing.pFileToListing(fileBytes);
        List<String> actual = Arrays.asList(s.split("\n"));

        List<String> expected = Files.readAllLines(Paths.get("frogging-reconstruction.bas"));
        assertEquals(expected, actual);
    }
}
