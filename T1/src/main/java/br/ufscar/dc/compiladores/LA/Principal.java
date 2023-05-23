package br.ufscar.dc.compiladores.LA;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

import br.ufscar.dc.compiladores.LA.LA;

public class Principal {
    public static void main(String args[]) {

        try {
            String arquivoSaida = args[1];
            CharStream cs = CharStreams.fromFileName(args[0]);
            try (PrintWriter pw = new PrintWriter(arquivoSaida)) {
                var lex = new LA(cs);

                Token t = null;
                while ((t = lex.nextToken()).getType() != Token.EOF) {
                    String nomeToken = LA.VOCABULARY.getDisplayName(t.getType());

                    if(nomeToken.equals("ERRO")) {
                        pw.println("Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado");
                        break;
                    } else if(nomeToken.equals("CADEIA_NAO_FECHADA")) {
                        pw.println("Linha " + t.getLine() + ": cadeia literal nao fechada");
                        break;
                    } else if(nomeToken.equals("COMENTARIO_NAO_FECHADO")) {
                        pw.println("Linha " + t.getLine() + ": comentario nao fechado");
                        break;
                    }
                    else if (nomeToken.equals("PALAVRA_CHAVE")){
                        pw.println("<'" + t.getText() + "','" + t.getText() + "'>");
                    }
                    else if (nomeToken.equals("OP_ARIT")){
                        pw.println("<'" + t.getText() + "','" + t.getText() + "'>");
                    }
                    else if (nomeToken.equals("OP_REL")){
                        pw.println("<'" + t.getText() + "','" + t.getText() + "'>");
                    }
                    else if (nomeToken.equals("OP_COMPR")){
                        pw.println("<'" + t.getText() + "','" + t.getText() + "'>");
                    }
                    else {
                        pw.println("<'" + t.getText() + "'," + nomeToken + ">");
                    }
                }
            }catch(FileNotFoundException fnfe) {
                System.err.println("O arquivo/diretório não existe:"+args[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}