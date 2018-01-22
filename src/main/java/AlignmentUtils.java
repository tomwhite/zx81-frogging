import org.biojava.bio.Annotation;
import org.biojava.bio.BioException;
import org.biojava.bio.alignment.AlignmentPair;
import org.biojava.bio.alignment.NeedlemanWunsch;
import org.biojava.bio.alignment.SubstitutionMatrix;
import org.biojava.bio.seq.io.CharacterTokenization;
import org.biojava.bio.seq.io.SymbolTokenization;
import org.biojava.bio.symbol.*;

import java.util.HashSet;
import java.util.Set;

public class AlignmentUtils {
    static {
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

    public static AlignmentPair align(String query, String target) throws BioException {
        FiniteAlphabet binaryAlphabet = (FiniteAlphabet) AlphabetManager.alphabetForName("Binary");
        SymbolTokenization tokenization = binaryAlphabet.getTokenization("token");

        short match = 1;
        short replace = -1;
        SubstitutionMatrix matrix = new SubstitutionMatrix(binaryAlphabet, match, replace);
        short insert = 1;
        short delete = 1;
        short gapExtend = 1;

        NeedlemanWunsch aligner = new NeedlemanWunsch(match, replace, insert, delete, gapExtend, matrix);
        return aligner.pairwiseAlignment(new SimpleSymbolList(tokenization, query), new SimpleSymbolList(tokenization, target));
    }
}
