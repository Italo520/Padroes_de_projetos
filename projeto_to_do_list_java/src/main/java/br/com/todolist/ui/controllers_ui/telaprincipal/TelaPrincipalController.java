// Em: src/main/java/br/com/todolist/ui/controllers/telaprincipal/TelaPrincipalController.java
package br.com.todolist.ui.controllers.telaprincipal;

import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;
import br.com.todolist.service.*;
import br.com.todolist.ui.views.telaprincipal.TelaPrincipalView;

import javax.swing.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class TelaPrincipalController {

    private final TelaPrincipalView view;
    private final Orquestrador orquestrador;
    private final NotificacaoService notificacaoService;
    private final PainelTarefasController painelTarefasController;
    private final PainelEventosController painelEventosController;

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATADOR_MES_ANO = DateTimeFormatter.ofPattern("MM/yyyy");

    public TelaPrincipalController(TelaPrincipalView view, Usuario usuarioLogado) {
        this.view = view;

        // Inicialização dos serviços e do orquestrador
        String emailUsuario = usuarioLogado.getEmail();
        GerenteDeDadosDoUsuario dadosDoUsuario = new GerenteDeDadosDoUsuario();
        GerenteDeTarefas gerenteDeTarefas = new GerenteDeTarefas(dadosDoUsuario, emailUsuario);
        GerenteDeEventos gerenteDeEventos = new GerenteDeEventos(dadosDoUsuario, emailUsuario);
        this.notificacaoService = new NotificacaoService(emailUsuario);
        RelatorioService relatorioService = new RelatorioService(gerenteDeTarefas);
        this.orquestrador = new Orquestrador(gerenteDeEventos, gerenteDeTarefas, relatorioService, emailUsuario);

        // Inicialização dos controllers dos painéis
        this.painelTarefasController = new PainelTarefasController(view.getPainelTarefas(), orquestrador, notificacaoService, emailUsuario);
        this.painelEventosController = new PainelEventosController(view.getPainelEventos(), orquestrador, notificacaoService);

        // Conecta este controller à view
        this.view.setController(this);
    }

    // --- Métodos da Barra de Ferramentas ---

    public void sair() {
        view.dispose();
        System.exit(0);
    }

    public void mostrarSobre() {
        JOptionPane.showMessageDialog(view,
                "Aplicação de Lista de Tarefas\nVersão 1.0\nCriado Por: Italo, Rickson e Marcus",
                "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }

    public void listarTarefasPorDia() {
        obterDataDoUsuario("Digite a data para listar as tarefas (dd/MM/yyyy):")
                .ifPresent(dia -> {
                    List<Tarefa> tarefas = orquestrador.listarTarefasPorDia(dia);
                    if (tarefas.isEmpty()) {
                        JOptionPane.showMessageDialog(view, "Nenhuma tarefa encontrada para esta data.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    }
                    view.alternarParaPainelTarefas();
                    view.getPainelTarefas().popularListaTarefas(tarefas);
                });
    }

    public void listarTarefasCriticas() {
        List<Tarefa> tarefas = orquestrador.listarTarefasCriticas();
        if (tarefas.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nenhuma tarefa crítica encontrada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
        view.alternarParaPainelTarefas();
        view.getPainelTarefas().popularListaTarefas(tarefas);
    }

    public void gerarPdfTarefasDoDia() {
        obterDataDoUsuario("Digite a data para gerar o PDF (dd/MM/yyyy):")
                .ifPresent(dia -> {
                    String nomeArquivo = orquestrador.gerarRelatorioPDFTarefasDoDia(dia);
                    if (nomeArquivo != null) {
                        JOptionPane.showMessageDialog(view, "PDF '" + nomeArquivo + "' gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "Não foi possível gerar o PDF. Verifique se existem tarefas para a data.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                });
    }

    public void enviarEmailTarefasDoDia() {
        obterDataDoUsuario("Digite a data para o envio do relatório (dd/MM/yyyy):")
                .ifPresent(dia -> {
                    String nomeArquivoPDF = orquestrador.gerarRelatorioPDFTarefasDoDia(dia);
                    if (nomeArquivoPDF == null) {
                        JOptionPane.showMessageDialog(view, "Não foi possível gerar o relatório em PDF.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    boolean sucesso = notificacaoService.enviarRelatorioTarefasDoDiaPorEmail(dia, nomeArquivoPDF);
                    if (sucesso) {
                        JOptionPane.showMessageDialog(view, "Email com o relatório em anexo enviado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "Não foi possível enviar o email.\nVerifique se existem tarefas para a data informada.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                });
    }

    public void gerarRelatorioTarefasPorMes() {
        obterMesAnoDoUsuario("Digite o mês e ano para o relatório (MM/yyyy):")
                .ifPresent(mes -> {
                    String nomeArquivo = "Relatorio_Tarefas_" + mes.format(DateTimeFormatter.ofPattern("MM_yyyy")) + ".xlsx";
                    orquestrador.gerarRelatorioTarefasPorMes(mes, nomeArquivo);
                    JOptionPane.showMessageDialog(view, "Relatório Excel '" + nomeArquivo + "' gerado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                });
    }

    public void listarEventosPorDia() {
        obterDataDoUsuario("Digite a data para listar os eventos (dd/MM/yyyy):")
                .ifPresent(dia -> {
                    List<Evento> eventos = orquestrador.listarEventosPorDia(dia);
                    if (eventos.isEmpty()) {
                        JOptionPane.showMessageDialog(view, "Nenhum evento encontrado para esta data.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    }
                    view.alternarParaPainelEventos();
                    view.getPainelEventos().popularListaEventos(eventos);
                });
    }

    public void listarEventosPorMes() {
        obterMesAnoDoUsuario("Digite o mês e ano (MM/yyyy):")
                .ifPresent(mes -> {
                    List<Evento> eventos = orquestrador.listarEventosPorMes(mes);
                    if (eventos.isEmpty()) {
                        JOptionPane.showMessageDialog(view, "Nenhum evento encontrado para este mês.", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    }
                    view.alternarParaPainelEventos();
                    view.getPainelEventos().popularListaEventos(eventos);
                });
    }

    // --- Métodos Utilitários de Input ---

    private Optional<LocalDate> obterDataDoUsuario(String mensagem) {
        String dataInput = JOptionPane.showInputDialog(view, mensagem);
        if (dataInput == null || dataInput.trim().isEmpty()) return Optional.empty();
        try {
            return Optional.of(LocalDate.parse(dataInput, FORMATADOR_DATA));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(view, "Formato de data inválido! Use dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }

    private Optional<YearMonth> obterMesAnoDoUsuario(String mensagem) {
        String mesAnoInput = JOptionPane.showInputDialog(view, mensagem);
        if (mesAnoInput == null || mesAnoInput.trim().isEmpty()) return Optional.empty();
        try {
            return Optional.of(YearMonth.parse(mesAnoInput, FORMATADOR_MES_ANO));
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(view, "Formato de data inválido! Use MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }
    }
}