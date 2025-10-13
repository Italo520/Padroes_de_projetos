package br.com.todolist.ui.views_ui.telaprincipal;

import br.com.todolist.entity.Evento;
import br.com.todolist.ui.core_ui.PainelBase;
import br.com.todolist.ui.controllers_ui.telaprincipal.PainelEventosController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PainelEventosView extends PainelBase {

    private DefaultListModel<Evento> modeloListaEventos;
    private JList<Evento> listaDeEventos;
    private JLabel valorDescricao;
    private JLabel valorTempoRestante;

    // Botões
    private JButton botaoNovoEvento;
    private JButton botaoEditarEvento;
    private JButton botaoExcluirEvento;

    public PainelEventosView() {
        super();
        inicializarLayout();
    }

    @Override
    protected JPanel criarPainelDeBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botaoNovoEvento = new JButton("Novo Evento");
        botaoEditarEvento = new JButton("Editar Evento");
        botaoExcluirEvento = new JButton("Excluir Evento");

        painel.add(botaoNovoEvento);
        painel.add(botaoEditarEvento);
        painel.add(botaoExcluirEvento);

        return painel;
    }

    @Override
    protected JPanel criarPainelDeConteudo() {
        modeloListaEventos = new DefaultListModel<>();
        listaDeEventos = new JList<>(modeloListaEventos);
        listaDeEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaDeEventos.setBorder(BorderFactory.createTitledBorder("Eventos"));

        JPanel painelDetalhes = new JPanel(new BorderLayout());
        painelDetalhes.setBorder(BorderFactory.createTitledBorder("Detalhes do Evento"));

        JPanel painelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
        painelCampos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        valorDescricao = new JLabel();
        valorTempoRestante = new JLabel();

        painelCampos.add(new JLabel("Descrição:"));
        painelCampos.add(valorDescricao);
        painelCampos.add(new JLabel("Tempo Restante:"));
        painelCampos.add(valorTempoRestante);

        painelDetalhes.add(painelCampos, BorderLayout.NORTH);

        JSplitPane painelDividido = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(listaDeEventos),
                painelDetalhes);
        painelDividido.setDividerLocation(300);

        JPanel painelDeConteudo = new JPanel(new BorderLayout());
        painelDeConteudo.add(painelDividido, BorderLayout.CENTER);

        return painelDeConteudo;
    }

    public void setController(PainelEventosController controller) {
        botaoNovoEvento.addActionListener(e -> controller.novoEvento());
        botaoEditarEvento.addActionListener(e -> controller.editarEvento());
        botaoExcluirEvento.addActionListener(e -> controller.excluirEvento());
        listaDeEventos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                controller.selecionarEvento();
            }
        });
    }

    public void atualizarDetalhesEvento(Evento evento) {
        if (evento != null) {
            valorDescricao.setText("<html>" + evento.getDescricao() + "</html>");

            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), evento.getDeadline());
            String textoTempo;
            if (diasRestantes > 1) {
                textoTempo = "Faltam " + diasRestantes + " dias";
            } else if (diasRestantes == 1) {
                textoTempo = "Falta 1 dia";
            } else if (diasRestantes == 0) {
                textoTempo = "É hoje!";
            } else {
                textoTempo = "Atrasado";
            }
            valorTempoRestante.setText(textoTempo);
        } else {
            valorDescricao.setText("Selecione um evento");
            valorTempoRestante.setText("-");
        }
    }

    public void popularListaEventos(List<Evento> eventos) {
        modeloListaEventos.clear();
        if (eventos != null) {
            eventos.forEach(modeloListaEventos::addElement);
        }
        atualizarDetalhesEvento(null);
    }

    public Evento getEventoSelecionado() {
        return listaDeEventos.getSelectedValue();
    }
}