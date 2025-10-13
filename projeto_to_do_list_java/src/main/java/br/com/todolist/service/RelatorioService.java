package br.com.todolist.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import br.com.todolist.entity.Tarefa;
import br.com.todolist.util.GeradorDeRelatorio;

public class RelatorioService {
    private GerenteDeTarefas gerenteDeTarefas;

    public RelatorioService(GerenteDeTarefas gerenteDeTarefas) {
        this.gerenteDeTarefas = gerenteDeTarefas;
    }



    public String gerarRelatorioPDFTarefasDoDia(LocalDate dia) {
        List<Tarefa> tarefas = this.gerenteDeTarefas.listarTarefasPorDia(dia);
        if (tarefas == null || tarefas.isEmpty()) {
            return null; 
        }

        String nomeArquivo = "Relatorio_Tarefas_" + dia.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".pdf";
        String tituloRelatorio = "Relatório de Tarefas - " + dia.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        String[] cabecalhos = {"Título", "Descrição", "Prioridade"};
        List<String[]> dados = tarefas.stream()
            .map(t -> new String[]{t.getTitulo(), t.getDescricao(), String.valueOf(t.getPrioridade())})
            .collect(Collectors.toList());

        GeradorDeRelatorio.gerarPdf(nomeArquivo, tituloRelatorio, cabecalhos, dados);
        return nomeArquivo;
    }


    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        List<Tarefa> tarefasDoMes = this.gerenteDeTarefas.listarTodasTarefas().stream()
                .filter(t -> YearMonth.from(t.getDeadline()).equals(mes))
                .collect(Collectors.toList());
                
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
        GeradorDeRelatorio.gerarExcel(nomeArquivo, nomePlanilha, cabecalhos, dados, colunaExtra);
}
}
