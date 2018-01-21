import org.junit.Test;

import java.io.IOException;

public class FroggingProgramTest {
    @Test
    public void test() throws IOException {
        byte[] memory = FroggingProgram.loadAndEditFileBytes();
        String listing = ZX81PToListing.pFileToListing(memory);
        System.out.println(listing);
        // TODO: make edits so that it can be round tripped.
    }
}
