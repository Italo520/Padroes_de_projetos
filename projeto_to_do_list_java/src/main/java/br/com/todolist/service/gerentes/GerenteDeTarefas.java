package br.com.todolist.service.gerentes;

import br.com.todolist.dao.TarefaDAO;
import br.com.todolist.entity.Tarefa;
import br.com.todolist.entity.Usuario;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class GerenteDeTarefas {

    private final TarefaDAO tarefaDAO;
    private final Usuario usuario;

    public GerenteDeTarefas(Usuario usuario) {
        this.tarefaDAO = new TarefaDAO();
        this.usuario = usuario;
    }

    public void cadastrarTarefa(Tarefa tarefa) {
        tarefa.setUsuario(this.usuario);
        tarefaDAO.salvar(tarefa);
    }

    public List<Tarefa> listarTodasTarefas() {
        return tarefaDAO.buscarPorUsuario(this.usuario);
    }

    public void excluirTarefa(Tarefa tarefa) {
        if (tarefa.getUsuario().equals(this.usuario)) {
            tarefaDAO.deletar(tarefa);
        } else {
            System.err.println("Aviso: Tentativa de excluir tarefa de outro usuário.");
        }
    }

    public void editarTarefa(Tarefa tarefaOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline,
            int novaPrioridade) {
        if (tarefaOriginal.getUsuario().equals(this.usuario)) {
            tarefaOriginal.setTitulo(novoTitulo);
            tarefaOriginal.setDescricao(novaDescricao);
            tarefaOriginal.setDeadLine(novoDeadline);
            tarefaOriginal.setPrioridade(novaPrioridade);
            tarefaDAO.salvar(tarefaOriginal);
        } else {
            System.err.println("Aviso: Tentativa de editar tarefa de outro usuário.");
        }
    }

    public List<Tarefa> listarTarefasPorDia(LocalDate dia) {
        return listarTodasTarefas().stream()
                .filter(tarefa -> tarefa.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    public List<Tarefa> listarTarefasCriticas() {
        LocalDate hoje = LocalDate.now();
        return listarTodasTarefas().stream()
                .filter(tarefa -> {
                    long diasRestantes = ChronoUnit.DAYS.between(hoje, tarefa.getDeadline());
                    return (diasRestantes - tarefa.getPrioridade()) < 0;
                })
                .collect(Collectors.toList());
    }

    public void atualizarTarefa(Tarefa tarefa) {
        if (tarefa.getUsuario().equals(this.usuario)) {
            tarefaDAO.salvar(tarefa);
            System.out.println("Tarefa atualizada e dados salvos.");
        } else {
            System.err.println("Aviso: Tentativa de atualizar tarefa de outro usuário.");
        }
    }
    
    public List<Tarefa> listarTarefasPorMes(YearMonth mes) {
        return listarTodasTarefas().stream()
                .filter(t -> YearMonth.from(t.getDeadline()).equals(mes))
                .collect(Collectors.toList());
    }
}