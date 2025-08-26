package br.com.todolist.service;

import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Usuario;
import br.com.todolist.util.GeradorDeRelatorio;

import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Orquestrador {

    private final GerenteDeTarefas gerenteDeTarefas;
    private final GerenteDeEventos gerenteDeEventos;
    private final NotificacaoService notificacaoService;
    private final RelatorioService relatorioService;
    private final String emailUsuario;

    public Orquestrador(Usuario usuario) {
        GerenteDeDadosDoUsuario dadosDoUsuario = new GerenteDeDadosDoUsuario();
        this.emailUsuario = usuario.getEmail();
        this.gerenteDeTarefas = new GerenteDeTarefas(dadosDoUsuario, this.emailUsuario);
        this.gerenteDeEventos = new GerenteDeEventos(dadosDoUsuario, this.emailUsuario);
        this.notificacaoService = new NotificacaoService(this.emailUsuario);
        this.relatorioService = new RelatorioService(this.gerenteDeTarefas);
    }

    public void cadastrarTarefa(String titulo, String descricao, LocalDate deadline, int prioridade) {
        Tarefa novaTarefa = new Tarefa(titulo, descricao, this.emailUsuario, deadline, prioridade);
        this.gerenteDeTarefas.cadastrarTarefa(novaTarefa);
        this.notificacaoService.notificarCriacaoTarefa(novaTarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return this.gerenteDeTarefas.listarTodasTarefas();
    }

    public void excluirTarefa(Tarefa tarefa) {
        this.gerenteDeTarefas.excluirTarefa(tarefa);
        this.notificacaoService.notificarexclusaoTarefa(tarefa);
    }

    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) {
        this.gerenteDeTarefas.editarTarefa(tarefaOriginal, novoTitulo, novaDescricao, novoDeadline, novaPrioridade);
        this.notificacaoService.notificarEdicaoTarefa(tarefaOriginal);
    }

    public void atualizarTarefa(Tarefa tarefa) {
        this.gerenteDeTarefas.atualizarTarefa(tarefa);
    }

    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return this.gerenteDeTarefas.listarTarefasPorDia(dia);
    }

    public List<Tarefa> listarTarefasCriticas() {
        return this.gerenteDeTarefas.listarTarefasCriticas();
    }

    public boolean cadastrarEvento(String titulo, String descricao, LocalDate deadline) {
        Evento novoEvento = new Evento(titulo, descricao, this.emailUsuario, deadline);
        boolean sucesso = this.gerenteDeEventos.cadastrarEvento(novoEvento);
        if (sucesso) {
            this.notificacaoService.notificarCriacaoEvento(novoEvento);
        }
        return sucesso;
    }

    public List<Evento> listarTodosEventos() {
        return this.gerenteDeEventos.listarTodosEventos();
    }

    public void excluirEvento(Evento evento) {
        this.gerenteDeEventos.excluirEvento(evento);
        this.notificacaoService.notificarExclusaoEvento(evento);
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        this.gerenteDeEventos.editarEvento(eventoOriginal, novoTitulo, novaDescricao, novoDeadline);
        this.notificacaoService.notificarEdicaoEvento(eventoOriginal);
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return this.gerenteDeEventos.listarEventosPorDia(dia);
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return this.gerenteDeEventos.listarEventosPorMes(mes);
    }

    public boolean enviarRelatorioTarefasDoDiaPorEmail(LocalDate dia) {
        String nomeArquivoPDF = relatorioService.enviarRelatorioTarefasDoDiaPorEmail(dia);
        if (nomeArquivoPDF == null) {
            return false;
        }
        return relatorioService.enviarRelatorioTarefasDoDiaPorEmail(dia, nomeArquivoPDF);
    }

    public void gerarRelatorioTarefasPorMes(YearMonth mes, String nomeArquivo) {
        RelatorioService relatorioService = new RelatorioService(this.gerenteDeTarefas);
        relatorioService.gerarRelatorioTarefasPorMes(mes, nomeArquivo);
    }

}