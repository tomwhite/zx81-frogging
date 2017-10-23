import sinclair.basic.ZX81Basic;
import sinclair.basic.ZX81SysVars;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * Created by tom on 26/03/2016.
 */
public class ZX81ToCsv {
    public static void main(String[] args) throws Exception {
        File dir = new File("pfiles");
        System.out.println("Pfile,VERSN,E_PPC,D_FILE,DF_CC,VARS,DEST,E_LINE,CH_ADD,X_PTR,STKBOT,STKEND,BREG,MEM,UN1,DF_SZ,S_TOP,LAST_K,DEBOUN,MARGIN,NXTLIN,OLDPPC,FLAGX,STRLEN,T_ADDR,SEED,FRAMES,COORDS,COORDY,PR_CC,S_POSN,S_POSL,CDFLAG,PRBUFF,MEMBOT,UN2,NumLines,MeanLineLength");
        for (File f : dir.listFiles()) {
            System.out.print(f.getName());

            FileInputStream fis = new FileInputStream(f);
            byte[] fileBytes = new byte[fis.available()];
            fis.read(fileBytes);
            fis.close();

            StringBuffer var = new StringBuffer();
            ZX81SysVars.dumpSystemVariables(fileBytes, 0, ZX81SysVars.SAVE_START, var);
            String[] lines = var.toString().split("\n");
            for (String field : lines) {
                System.out.print(",");
                String[] vals = field.split("=");
                if (vals[0].trim().equals("PRBUFF")) {
                    System.out.print(prbuff(fileBytes));
                } else if (vals[0].trim().equals("MEMBOT")) {
                    System.out.print(membot(fileBytes));
                } else {
                    System.out.print(field.split("=")[1].trim());
                }
            }

            Map<Integer, byte[]> programLines = ZX81Basic.getProgramLines(fileBytes);
            System.out.print(",");
            System.out.print(programLines.size());
            System.out.print(",");
            System.out.print(meanLineLength(programLines));

            System.out.println();
        }
    }

    private static String prbuff(byte[] memory) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 33; i++) {
            int variableValue = ZX81SysVars.getVariableValue(memory, 0, ZX81SysVars.SAVE_START, ZX81SysVars.PRBUFF + i, 1);
            sb.append(String.format("%02x ", variableValue));
        }
        return sb.toString();
    }

    private static String membot(byte[] memory) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 30; i++) {
            int variableValue = ZX81SysVars.getVariableValue(memory, 0, ZX81SysVars.SAVE_START, ZX81SysVars.MEMBOT + i, 1);
            sb.append(String.format("%02x ", variableValue));
        }
        return sb.toString();
    }

    private static float meanLineLength(Map<Integer, byte[]> programLines) {
        float mean = 0;
        for (byte[] line : programLines.values()) {
            mean += line.length;
        }
        return mean / programLines.size();
    }
}
