// Em: src/main/java/br/com/todolist/ui/views/telaprincipal/TelaPrincipalView.java
package br.com.todolist.ui.views.telaprincipal;

import br.com.todolist.entity.Usuario;
import br.com.todolist.ui.controllers.telaprincipal.TelaPrincipalController;
import javax.swing.*;

public class TelaPrincipalView extends JFrame {

    private JTabbedPane painelComAbas;
    private PainelTarefasView painelTarefas;
    private PainelEventosView painelEventos;

    // Itens de Menu que o Controller precisa acessar
    private JMenuItem itemSair;
    private JMenuItem listarTarefasPorDia;
    private JMenuItem listarTarefasCriticas;
    private JMenuItem pdfDoDia;
    private JMenuItem enviarEmailTarefas;
    private JMenuItem relatorioTarefasPorMes;
    private JMenuItem listarEventosPorDia;
    private JMenuItem listarEventosMesEspecifico;
    private JMenuItem itemSobre;

    public TelaPrincipalView(Usuario usuarioLogado) {
        super("Usuário: " + usuarioLogado.getNome());
        configurarJanela();
        montarLayout();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
    }

    private void montarLayout() {
        setJMenuBar(criarBarraFerramentas());

        painelComAbas = new JTabbedPane();
        this.painelTarefas = new PainelTarefasView();
        this.painelEventos = new PainelEventosView();

        painelComAbas.addTab("Tarefas", null, this.painelTarefas, "Gerenciador de Tarefas");
        painelComAbas.addTab("Eventos", null, this.painelEventos, "Gerenciador de Eventos");
        painelComAbas.setBounds(5, 5, 1270, 650);
        add(painelComAbas);
    }

    private JMenuBar criarBarraFerramentas() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArquivo = new JMenu("Arquivo");
        JMenu menuTarefas = new JMenu("Tarefas");
        JMenu menuEventos = new JMenu("Eventos");
        JMenu menuAjuda = new JMenu("Ajuda");

        itemSair = new JMenuItem("Sair");
        listarTarefasPorDia = new JMenuItem("Listar Tarefas por Dia");
        listarTarefasCriticas = new JMenuItem("Listar Tarefas Críticas");
        pdfDoDia = new JMenuItem("Gerar PDF das Tarefas do Dia");
        enviarEmailTarefas = new JMenuItem("Enviar Tarefas do Dia por Email");
        relatorioTarefasPorMes = new JMenuItem("Relatório de Tarefas por Mês (Excel)");
        listarEventosPorDia = new JMenuItem("Listar Eventos por Dia");
        listarEventosMesEspecifico = new JMenuItem("Listar Eventos por Mês");
        itemSobre = new JMenuItem("Sobre");

        menuArquivo.add(itemSair);
        menuTarefas.add(listarTarefasPorDia);
        menuTarefas.add(listarTarefasCriticas);
        menuTarefas.addSeparator();
        menuTarefas.add(pdfDoDia);
        menuTarefas.add(enviarEmailTarefas);
        menuTarefas.add(relatorioTarefasPorMes);
        menuEventos.add(listarEventosPorDia);
        menuEventos.add(listarEventosMesEspecifico);
        menuAjuda.add(itemSobre);

        menuBar.add(menuArquivo);
        menuBar.add(menuTarefas);
        menuBar.add(menuEventos);
        menuBar.add(menuAjuda);

        return menuBar;
    }

    public void setController(TelaPrincipalController controller) {
        itemSair.addActionListener(e -> controller.sair());
        itemSobre.addActionListener(e -> controller.mostrarSobre());
        // Ações de Tarefas
        listarTarefasPorDia.addActionListener(e -> controller.listarTarefasPorDia());
        listarTarefasCriticas.addActionListener(e -> controller.listarTarefasCriticas());
        pdfDoDia.addActionListener(e -> controller.gerarPdfTarefasDoDia());
        enviarEmailTarefas.addActionListener(e -> controller.enviarEmailTarefasDoDia());
        relatorioTarefasPorMes.addActionListener(e -> controller.gerarRelatorioTarefasPorMes());
        // Ações de Eventos
        listarEventosPorDia.addActionListener(e -> controller.listarEventosPorDia());
        listarEventosMesEspecifico.addActionListener(e -> controller.listarEventosPorMes());
    }

    public PainelTarefasView getPainelTarefas() {
        return this.painelTarefas;
    }

    public PainelEventosView getPainelEventos() {
        return this.painelEventos;
    }

    public void alternarParaPainelTarefas() {
        painelComAbas.setSelectedComponent(painelTarefas);
    }

    public void alternarParaPainelEventos() {
        painelComAbas.setSelectedComponent(painelEventos);
    }
}