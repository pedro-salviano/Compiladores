// importando as bibliotecas necessárias
package br.ufscar.dc.compiladores.LA;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Principal {
    public static void main(String args[]) {

        try {
            String arquivoSaida = args[1];
            CharStream cs = CharStreams.fromFileName(args[0]);

            try (PrintWriter pw = new PrintWriter(arquivoSaida)) { //permite escrita em arquivo
                try{
                    LALexer lex = new LALexer(cs); //define o lexico como a LA

                    lex.removeErrorListeners();
                    LexerErrorListener mc_Lex_el = new LexerErrorListener();
                    lex.addErrorListener(mc_Lex_el);

                    lex.reset();
                    CommonTokenStream tokens = new CommonTokenStream(lex);
                    LAParser parser = new LAParser(tokens);

                    parser.removeErrorListeners();
                    SyntaxErrorListener mcel = new SyntaxErrorListener();
                    parser.addErrorListener(mcel);

                    parser.programa();
                }   catch (ParseCancellationException e){
                    pw.println(e.getMessage());
                    pw.println("Fim da compilacao");
                }

            }catch(FileNotFoundException fnfe) { //erro gerado quando o arquivo nao eh encontrado
                System.err.println("O arquivo/diretório não existe:"+args[1]);
            }
        } catch (IOException e) { //se nao consegue encontrar o arquivo saida (argumento 1) ou nao consegue ler
            //o arquivo de entrada (argumento 0), ele para o programa (falha) e nem chega a compilar
            //basicamente, se colocar pra rodar e nao escrever os argumentos certo, ele falha
            e.printStackTrace();
        }
    }
}
