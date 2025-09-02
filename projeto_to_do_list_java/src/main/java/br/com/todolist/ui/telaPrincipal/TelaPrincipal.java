package br.com.todolist.ui.telaPrincipal;


import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import br.com.todolist.models.Evento;
import br.com.todolist.models.Tarefa;
import br.com.todolist.models.Usuario;
import br.com.todolist.service.*;

public class TelaPrincipal extends JFrame {

    private Orquestrador orquestrador;
    private PainelTarefas painelTarefas;
    private PainelEventos painelEventos;
    private JTabbedPane painelComAbas;
    private NotificacaoService notificacaoService;
    private String emailUsuario;

    public TelaPrincipal(Usuario usuarioLogado) {
        super("Usuário: " + usuarioLogado.getNome());
        GerenteDeDadosDoUsuario dadosDoUsuario = new GerenteDeDadosDoUsuario();
        this.emailUsuario = usuarioLogado.getEmail();

        GerenteDeTarefas gerenteDeTarefas = new GerenteDeTarefas(dadosDoUsuario, emailUsuario);
        GerenteDeEventos gerenteDeEventos = new GerenteDeEventos(dadosDoUsuario, emailUsuario);
        this.notificacaoService = new NotificacaoService(emailUsuario);
        RelatorioService relatorioService = new RelatorioService(gerenteDeTarefas);

        this.orquestrador = new Orquestrador(
                gerenteDeEventos,
                gerenteDeTarefas,
                relatorioService,
                emailUsuario
        );
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
        setJMenuBar(BarraFerramentas.criarBarraFerramentas(this, this.orquestrador, this.notificacaoService));

        criarPaineis();
        painelComAbas.setBounds(5, 5, 1270, 650);

        add(painelComAbas);
    }

    private void criarPaineis() {
        painelComAbas = new JTabbedPane();

        this.painelTarefas = new PainelTarefas(this.orquestrador, this.notificacaoService, this.emailUsuario);
        this.painelEventos = new PainelEventos(this.orquestrador, this.notificacaoService);

        painelComAbas.addTab("Tarefas", null, this.painelTarefas, "Gerenciador de Tarefas");
        painelComAbas.addTab("Eventos", null, this.painelEventos, "Gerenciador de Eventos");
    }

    public void atualizarPainelDeTarefas(List<Tarefa> tarefas) {
        painelComAbas.setSelectedComponent(painelTarefas);
        painelTarefas.exibirTarefasDoDia(tarefas);
    }

    public void atualizarPainelDeEventos(List<Evento> eventos) {
        painelComAbas.setSelectedComponent(painelEventos);
        painelEventos.exibirEventos(eventos);
    }
}