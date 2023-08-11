// Importação de bibliotecas
package br.ufscar.dc.compiladores.LA;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class Principal {
    public static void main(String args[]) {

        try {
            // Registra em arquivoSaida o caminho do arquivo de saída a partir do segundo argumento
            String arquivoSaida = args[1];
            // Registra em cs o arquivo de entrada a partirdo primeiro argumento
            CharStream cs = CharStreams.fromFileName(args[0]);

            // Permite escrita no arquivo de saída, criando um PrintWriter pw
            try (PrintWriter pw = new PrintWriter(arquivoSaida)) { 
                try{
                    // Inicializa o léxico
                    LALexer lex = new LALexer(cs);
                    Token t = null;
                    boolean procede = true;
                    while ((t = lex.nextToken()).getType() != Token.EOF && procede) {
                        String nomeToken = LALexer.VOCABULARY.getDisplayName(t.getType());
                        
                        // Verifica a existência de um símbolo não identicicado
                        if(nomeToken.equals("ERRO")) { 
                            procede = false;
                            throw new ParseCancellationException("Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado");
                        
                        // Verifica se falta fechar uma cadeira (fecha aspas)
                        } else if(nomeToken.equals("CADEIA_NAO_FECHADA")) { 
                            procede = false;
                            throw new ParseCancellationException("Linha " + t.getLine() + ": cadeia literal nao fechada");
                        
                        // Verifica se há um comentário não fechado
                        } else if(nomeToken.equals("COMENTARIO_NAO_FECHADO")) { 
                            procede = false;
                            throw new ParseCancellationException("Linha " + t.getLine() + ": comentario nao fechado");
                            
                        }
                    }
                    if(procede){
                        // Reseta o léxico para reiniciar a análise
                        lex.reset();

                        // Cria o fluxo de tokens a partir do léxico 
                        CommonTokenStream tokens = new CommonTokenStream(lex); 

                        // Inicializa o parser semântico com os tokens
                        LAParser parser = new LAParser(tokens);

                        // Inicializa o listener sintático
                        SyntaxErrorListener mcel = new SyntaxErrorListener();  
                        parser.removeErrorListeners();

                        // Adiciona o listener sintatico
                        parser.addErrorListener(mcel);    

                        // Executa a análise sintática, construindo a árvore de análise
                        var programa = parser.programa();  
                        
                        // Inicializa o Visitor Semântico, AnalisadorSemantico, para realizar a análise semântica
                        AnalisadorSemantico semantic = new AnalisadorSemantico();

                        // Executa a análise semântica
                        semantic.visitPrograma(programa); 

                        // Verifica se há erros semânticos
                        if(!LASemanticUtils.semanticErrors.isEmpty()){

                            //Percorre os erros semânticos e os imprime no arquivo
                            for(var s: LASemanticUtils.semanticErrors){
                                pw.write(s);
                            }
                            pw.write("Fim da compilacao\n");
                        }
                    }
                }   catch (ParseCancellationException e){
                    // Imprime a mensagem de erro no arquivo de saída
                    pw.println(e.getMessage());

                    // Imprime "Fim da compilacao" no arquivo de saída
                    pw.println("Fim da compilacao");
                }
            
            // Exceção quando o arquivo de saída não for encontrado
            }catch(FileNotFoundException fnfe) { 
                System.err.println("O arquivo/diretório não existe:"+args[1]);
            }
        // Exceção quando não for possível ler o arquivo de entrada ou de saída
        } catch (IOException e) { 
            // Imprime o stackTrace para caso os argumentos não sejam passados corretamente
            e.printStackTrace();
        }
    }
}
