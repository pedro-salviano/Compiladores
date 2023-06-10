package br.ufscar.dc.compiladores.LA;

import java.util.BitSet;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class LexerErrorListener implements ANTLRErrorListener {

    @Override
    public void reportAmbiguity(Parser arg0, DFA arg1, int arg2, int arg3, boolean arg4, BitSet arg5,
            ATNConfigSet arg6) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reportAmbiguity'");
    }

    @Override
    public void reportAttemptingFullContext(Parser arg0, DFA arg1, int arg2, int arg3, BitSet arg4, ATNConfigSet arg5) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reportAttemptingFullContext'");
    }

    @Override
    public void reportContextSensitivity(Parser arg0, DFA arg1, int arg2, int arg3, int arg4, ATNConfigSet arg5) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reportContextSensitivity'");
    }

    @Override
    public void	syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
    {
        String[] tokens = msg.split("\'");
        
        if(tokens[1].charAt(0) == '{' && tokens[1].charAt(tokens[1].length()-1) != '}'){
            throw new ParseCancellationException("Linha " + line + ":" + " comentario nao fechado" + "\n");
        }
        if(tokens[1].charAt(0) == '"' && tokens[1].charAt(tokens[1].length()-1) != '"'){
            throw new ParseCancellationException("Linha " + line + ":" + " cadeia literal nao fechada" + "\n");
        }
        throw new ParseCancellationException("Linha " + line + ":" + " " + tokens[1] + " - simbolo nao identificado" + "\n");
    }
}