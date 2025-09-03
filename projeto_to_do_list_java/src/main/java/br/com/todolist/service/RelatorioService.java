package br.com.todolist.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import br.com.todolist.models.Tarefa;
import br.com.todolist.util.GeradorDeExcel;
import br.com.todolist.util.GeradorDePDF;

public class RelatorioService {
    private GerenteDeTarefas gerenteDeTarefas;

    public RelatorioService(GerenteDeTarefas gerenteDeTarefas) {
        this.gerenteDeTarefas = gerenteDeTarefas;
    }

    public String gerarRelatorioPDFTarefasDoDia(LocalDate dia) {
        List<Tarefa> tarefas = this.gerenteDeTarefas.listarTarefasPorDia(dia);

        String nomeArquivo = "Relatorio_Tarefas_" + dia.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".pdf";
        String tituloRelatorio = "Relatório de Tarefas - " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String[] cabecalhos = {"Título", "Descrição", "Prioridade"};

        List<String[]> dados = gerenteDeTarefas.listarTarefasPorDia(dia);
        
        GeradorDePDF.gerarPdf(nomeArquivo, tituloRelatorio, cabecalhos, dados);
        return nomeArquivo;
    }


    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        tarefasDoMes = gerenteDeTarefas.listarTarefasPorMes(mes);

        String[] cabecalhos = {"Título", "Descrição", "Prioridade", "Prazo", "Conclusão (%)"};
        
        List<String[]> dados = tarefasDoMes.stream()
            .map(t -> new String[]{
                t.getTitulo(),
                t.getDescricao(),
                String.valueOf(t.getPrioridade()),
                t.getDeadline().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            })
            .collect(Collectors.toList());
            
        List<String> colunaExtra = tarefasDoMes.stream()
            .map(t -> String.format("%.0f%%", t.obterPercentual()))
            .collect(Collectors.toList());

        String nomePlanilha = "Tarefas de " + mes.format(DateTimeFormatter.ofPattern("MM-yyyy"));
        GeradorDeExcel.gerarExcel(nomeArquivo, nomePlanilha, cabecalhos, dados, colunaExtra);
}
}
