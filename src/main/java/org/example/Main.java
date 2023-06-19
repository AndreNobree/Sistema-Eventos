package org.example;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner entrada = new Scanner(System.in);

//        chama Classes
        Respostas chamaResposta = new Respostas();
        Comandos chamaComandos = new Comandos();

        //CONFIGURAÇÕES INICIAIS
        chamaResposta.autoDeleteCompromisso();
        chamaResposta.saudacoes();
        chamaResposta.contagemCasamento();
        chamaResposta.autoSelectCompromisso();

        while(true){
            System.out.print("\u001B[34;1m\nO QUE DESEJA?, (se desejar sair, digite: \u001B[0m\u001B[33msair\u001B[0m\u001B[34;1m || se desejar menu, digite: \u001B[0m\u001B[33mmenu\u001B[0m)\n>> \u001B[0m");
            String pergunta = entrada.nextLine().toLowerCase();

            if (pergunta.equalsIgnoreCase("sair")) break;

            else if (pergunta.contains("menu") || pergunta.contains("cmd")) chamaComandos.linhaComando();

            else if (pergunta.contains("ver compr")) chamaResposta.retornaCompromisso();

            else if (pergunta.contains("criar compr")) chamaResposta.cadastraComprimisso(entrada);

            else if(pergunta.contains("notic")) chamaResposta.mostraNoticias(entrada);

            else if(pergunta.contains("ping")) chamaResposta.pingSite(entrada);

            else if(pergunta.contains("film") || pergunta.contains("cinem")) chamaResposta.filmesCartaz();

            else if(pergunta.contains("edit")) chamaResposta.editaCompromisso(entrada);

            else if(pergunta.contains("previs")) chamaResposta.previsaoTemporal();

            else System.out.println("\u001B[31mCOMANDO INVÁLIDO\u001B[0m");

        }

        entrada.close();
    }
}