import org.biojava.bio.Annotation;
import org.biojava.bio.alignment.AlignmentPair;
import org.biojava.bio.alignment.NeedlemanWunsch;
import org.biojava.bio.alignment.SubstitutionMatrix;
import org.biojava.bio.seq.io.CharacterTokenization;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AlignmentTest {

    private void registerBinaryAlphabet() {
        Symbol zero = AlphabetManager.createSymbol("zero", Annotation.EMPTY_ANNOTATION);
        Symbol one = AlphabetManager.createSymbol("one", Annotation.EMPTY_ANNOTATION);
        Symbol dash = AlphabetManager.createSymbol("dash", Annotation.EMPTY_ANNOTATION);

        Set<Symbol> symbols = new HashSet<>();
        symbols.add(zero);
        symbols.add(one);
        symbols.add(dash);

        SimpleAlphabet binaryAlphabet = new SimpleAlphabet(symbols, "Binary");

        CharacterTokenization tokenization = new CharacterTokenization(binaryAlphabet, true);
        tokenization.bindSymbol(zero, '0');
        tokenization.bindSymbol(one, '1');
        tokenization.bindSymbol(dash, '-');
        binaryAlphabet.putTokenization("token", tokenization);

        AlphabetManager.registerAlphabet(binaryAlphabet.getName(), binaryAlphabet);
    }

    @Test
    public void test() throws Exception {
        registerBinaryAlphabet();

        FiniteAlphabet binaryAlphabet = (FiniteAlphabet) AlphabetManager.alphabetForName("Binary");
        SymbolTokenization tokenization = binaryAlphabet.getTokenization("token");

        short match = 1;
        short replace = -1;
        SubstitutionMatrix matrix = new SubstitutionMatrix(binaryAlphabet, match, replace);
        short insert = 1;
        short delete = 1;
        short gapExtend = 1;
        NeedlemanWunsch aligner = new NeedlemanWunsch(match, replace, insert, delete, gapExtend, matrix);

        String line0Corrupted =
                "00000000 00000000 11000011 00100000 11101010 00001011 00101011 00110111 00110100 00101100 00101100 00101000 00010000 01001011 00000010 11011101 10".replace(" ", "");
        String line0Target =
                "00000000 00000001 00001100 00000000 11101010 00001011 00101011 00110111 00110100 00101100 00101100 00101110 00110011 00101100 00001011 01110110".replace(" ", "");
        SymbolList query = new SimpleSymbolList(tokenization, line0Corrupted);
        SymbolList target = new SimpleSymbolList(tokenization, line0Target);

        AlignmentPair alignmentPair = aligner.pairwiseAlignment(query, target);
        System.out.println(alignmentPair.formatOutput(1000));

        System.out.println(alignmentPair.getQuery().seqString());
        System.out.println(alignmentPair.getSubject().seqString());
    }
}
