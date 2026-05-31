import javax.swing.*;
import java.awt.*;
/**
 * Janela raiz da aplicação — é a primeira tela exibida ao usuário.
 * Organiza os três painéis principais em abas (Extrato, Relatórios, Nova Transação)
 * e exibe o saldo atual na barra de status inferior.
 */
public class JanelaPrincipal extends JFrame {
    private GerenciadorTransacoes gerenciador;
    private PersistenciaCSV persistencia;

    /** Label de saldo exibido na barra inferior — atualizado por outros painéis */
    private JLabel labelSaldo;

    /**
     * Cria e configura a janela principal com todas as abas e a barra de status.
     *
     * @param g gerenciador de transações com os dados já carregados
     * @param p responsável pela persistência dos dados em arquivo
     */
    public JanelaPrincipal(GerenciadorTransacoes g, PersistenciaCSV p){
        this.gerenciador = g;
        this.persistencia = p;
        setTitle("Finanças Pessoais");
        setSize(950, 620);
        // Inicializar maximizado
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // cada aba recebe os objetos que precisa para funcionar de forma independente
        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Extrato", new PainelExtrato(gerenciador, persistencia, this));
        abas.addTab("Relatórios", new PainelRelatorios(gerenciador));
        abas.addTab("Nova Transação", new PainelFormulario(gerenciador, persistencia, this));
        add(abas, BorderLayout.CENTER);

        // barra de status no rodapé com o saldo atual
        labelSaldo = new JLabel();
        JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelStatus.add(labelSaldo);
        add(panelStatus, BorderLayout.SOUTH);

        // calcula e exibe o saldo inicial ao abrir a janela
        atualizarSaldo();
    }
    /**
     * Recalcula e atualiza o saldo exibido na barra de status.
     * Deve ser chamado sempre que uma transação for adicionada, editada ou removida.
     */
    public void atualizarSaldo(){
        double saldo = gerenciador.calcularSaldo();
        labelSaldo.setText("saldo atual: R$ "+ String.format("%.2f",saldo));
    }
}
