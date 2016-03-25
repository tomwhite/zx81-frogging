import sinclair.basic.ZX81Basic;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * Created by tom on 25/03/2016.
 */
public class ZX81Lister {
    public static void main(String[] args) throws Exception {
        File f = new File("/Users/tom/projects-workspace/zx81/randompatterns.p");
        FileInputStream fis = new FileInputStream(f);
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        fis.close();
        Map<Integer, byte[]> programLines = ZX81Basic.getProgramLines(fileBytes);
        for (Map.Entry<Integer, byte[]> line : programLines.entrySet()) {
            int lineNumber = line.getKey();
            StringBuffer sb = new StringBuffer();
            ZX81Basic.dumpLine(line.getValue(), lineNumber, false, sb);
            System.out.println(sb.toString());
        }
    }
}
