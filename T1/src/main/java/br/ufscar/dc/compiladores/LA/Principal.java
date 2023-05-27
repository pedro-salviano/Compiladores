// importando as bibliotecas necessárias
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
            try (PrintWriter pw = new PrintWriter(arquivoSaida)) { //permite escrita em arquivo
                var lex = new LA(cs);

                Token t = null; // "primeiro" token eh nulo
                //le token por token, identificando seu tipo de acordo com a LA, conferindo os erros
                while ((t = lex.nextToken()).getType() != Token.EOF) {
                    String nomeToken = LA.VOCABULARY.getDisplayName(t.getType());

                    if(nomeToken.equals("ERRO")) { //simbolo nao identificado
                        pw.println("Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado");
                        break;
                    } else if(nomeToken.equals("CADEIA_NAO_FECHADA")) { //falta o ;
                        pw.println("Linha " + t.getLine() + ": cadeia literal nao fechada");
                        break;
                    } else if(nomeToken.equals("COMENTARIO_NAO_FECHADO")) { // comentario nao fechado
                        pw.println("Linha " + t.getLine() + ": comentario nao fechado");
                        break;
                    }
                    else if (nomeToken.equals("PALAVRA_CHAVE")){ //confere se o token faz parte de uma palavra chave
                        pw.println("<'" + t.getText() + "','" + t.getText() + "'>");
                    }
                    else if (nomeToken.equals("OP_ARIT")){ //confere se o token eh um simbolo de operador aritmetico
                        pw.println("<'" + t.getText() + "','" + t.getText() + "'>");
                    }
                    else if (nomeToken.equals("OP_REL")){ //confere se o token eh ou faz parte de um simbolo de operador relacional
                        pw.println("<'" + t.getText() + "','" + t.getText() + "'>");
                    }
                    else if (nomeToken.equals("OP_COMPR")){ //confere se o token eh ou faz parte de um simbolo de operador logico
                        pw.println("<'" + t.getText() + "','" + t.getText() + "'>");
                    }
                    else { //se nenhum dos casos acima for verdadeiro, o token nao faz parte de nenhuma palavra ou algorismo reservado
                        //portanto, eh um
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
