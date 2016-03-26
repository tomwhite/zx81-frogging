import sinclair.basic.ZX81Basic;
import sinclair.basic.ZX81SysVars;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * Created by tom on 25/03/2016.
 * This is basically PFileUtils
 */
public class ZX81Lister {
    public static void main(String[] args) throws Exception {
        File f = new File("/Users/tom/Downloads/Salvo.p/salvo.p");
        //File f = new File("/Users/tom/projects-workspace/zx81/randompatterns.p");
        FileInputStream fis = new FileInputStream(f);
        byte[] fileBytes = new byte[fis.available()];
        fis.read(fileBytes);
        fis.close();

        System.out.println("=== File");
        System.out.println("Length: " + fileBytes.length);
        System.out.println("Program end offset: " + ZX81SysVars.getVariableValueOffset(fileBytes, 16396, 2));

        System.out.println("=== System variables");
        StringBuffer var = new StringBuffer();
        ZX81SysVars.dumpSystemVariables(fileBytes, 0, ZX81SysVars.SAVE_START, var);
        System.out.println(var);

        System.out.println("=== Program");
        System.out.println(fileBytes[116]);
        System.out.println(fileBytes[117]); // first line number is 12, not 10 - perhaps a 0 bit was dropped? try inserting one?
        System.out.println(fileBytes[118]);
        System.out.println(fileBytes[119]);
        Map<Integer, byte[]> programLines = ZX81Basic.getProgramLines(fileBytes);
        for (Map.Entry<Integer, byte[]> line : programLines.entrySet()) {
            int lineNumber = line.getKey();
            StringBuffer sb = new StringBuffer();
            ZX81Basic.dumpLine(line.getValue(), lineNumber, false, sb);
            System.out.print(sb.toString());
        }

        System.out.println("=== Variables");
        Map variables = ZX81Basic.getVariables(fileBytes);
        System.out.println(variables);

        System.out.println("=== Variable memory");
        Map variableMemory = ZX81Basic.getVariableMemory(fileBytes);
        System.out.println(variableMemory);

    }
}
