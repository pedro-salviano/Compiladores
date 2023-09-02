// importando as bibliotecas necessárias
package br.ufscar.dc.compiladores.csvValidator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.IOException;

import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import br.ufscar.dc.compiladores.csvValidator.csvValidatorParser.ScriptContext;

public class Principal {
    public static void main(String args[]) {

        try {
            String arquivoSaida = args[1];
            CharStream cs = CharStreams.fromFileName(args[0]);

            try (PrintWriter pw = new PrintWriter(arquivoSaida)) { //permite escrita em arquivo
                try{
                    csvValidatorLexer lex = new csvValidatorLexer(cs); //define o lexico como a LA
                    Token t = null;
                    boolean procede = true;
                    while ((t = lex.nextToken()).getType() != Token.EOF && procede) {
                        String nomeToken = csvValidatorLexer.VOCABULARY.getDisplayName(t.getType());
                        //pw.println(nomeToken);
                        if(nomeToken.equals("ERRO")) { //simbolo nao identificado
                            procede = false;
                            throw new ParseCancellationException("Linha " + t.getLine() + ": " + t.getText() + " - simbolo nao identificado");
                            
                        } else if(nomeToken.equals("CADEIA_NAO_FECHADA")) { //falta o fecha aspas
                            procede = false;
                            throw new ParseCancellationException("Linha " + t.getLine() + ": cadeia literal nao fechada");
                            
                        } else if(nomeToken.equals("COMENTARIO_NAO_FECHADO")) { // comentario nao fechado
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
                        csvValidatorParser parser = new csvValidatorParser(tokens);

                        // Inicializa o listener sintático
                        SyntaxErrorListener mcel = new SyntaxErrorListener();  
                        parser.removeErrorListeners();

                        // Adiciona o listener sintatico
                        parser.addErrorListener(mcel);    

                        // Executa a análise sintática, construindo a árvore de análise
                        ScriptContext programa = parser.script();  
                        
                        // Inicializa o Visitor Semântico, para realizar a análise semântica
                        csvVisitor semantic = new csvVisitor();

                        // Executa a análise semântica
                        semantic.visitScript(programa); 

                        // Verifica se há erros semânticos
                        if(!csvValidatorSemanticUtils.semanticErrors.isEmpty()){

                            //Percorre os erros semânticos e os imprime no arquivo
                            for(String s: csvValidatorSemanticUtils.semanticErrors){
                                pw.write(s);
                            }
                            pw.write("Fim da compilacao\n");
                        }
                    }
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
