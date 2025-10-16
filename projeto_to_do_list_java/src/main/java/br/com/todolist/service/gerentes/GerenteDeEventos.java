package br.com.todolist.service.gerentes;

import br.com.todolist.dao.EventoDAO;
import br.com.todolist.entity.Evento;
import br.com.todolist.entity.Usuario;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class GerenteDeEventos {

    private final EventoDAO eventoDAO;
    private final Usuario usuario;

    public GerenteDeEventos(Usuario usuario) {
        this.eventoDAO = new EventoDAO();
        this.usuario = usuario;
    }

    public boolean cadastrarEvento(Evento novoEvento) {
        novoEvento.setUsuario(this.usuario);

        boolean dataDisponivel = listarTodosEventos().stream()
                .noneMatch(evento -> evento.getDeadline().isEqual(novoEvento.getDeadline()));

        if (dataDisponivel) {
            eventoDAO.salvar(novoEvento);
            return true;
        }

        System.out.println("Erro: Já existe um evento cadastrado para esta data para o usuário.");
        return false;
    }

    public List<Evento> listarTodosEventos() {
        return eventoDAO.buscarPorUsuario(this.usuario);
    }

    public void excluirEvento(Evento evento) {
        if (evento.getUsuario().equals(this.usuario)) {
            eventoDAO.deletar(evento);
        } else {
            System.err.println("Aviso: Tentativa de excluir evento de outro usuário.");
        }
    }

    public void editarEvento(Evento eventoOriginal, String novoTitulo, String novaDescricao, LocalDate novoDeadline) {
        if (eventoOriginal.getUsuario().equals(this.usuario)) {
            eventoOriginal.setTitulo(novoTitulo);
            eventoOriginal.setDescricao(novaDescricao);
            eventoOriginal.setDeadLine(novoDeadline);
            eventoDAO.salvar(eventoOriginal);
        } else {
            System.err.println("Aviso: Tentativa de editar evento de outro usuário.");
        }
    }

    public List<Evento> listarEventosPorDia(LocalDate dia) {
        return listarTodosEventos().stream()
                .filter(evento -> evento.getDeadline().isEqual(dia))
                .collect(Collectors.toList());
    }

    public List<Evento> listarEventosPorMes(YearMonth mes) {
        return listarTodosEventos().stream()
                .filter(evento -> YearMonth.from(evento.getDeadline()).equals(mes))
                .collect(Collectors.toList());
    }
}