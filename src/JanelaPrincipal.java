import javax.swing.*;
import java.awt.*;

public class JanelaPrincipal extends JFrame {
    private GerenciadorTransacoes gerenciador;
    private PersistenciaCSV persistencia;
    private JLabel labelSaldo;

    /**
     * Cria a janela principal do sistema.
     *
     * @param g gerenciador das transações
     * @param p responsável pela persistência dos dados
     */
    public JanelaPrincipal(GerenciadorTransacoes g, PersistenciaCSV p){
        this.gerenciador = g;
        this.persistencia = p;
        setTitle("Finanças Pessoais");
        setSize(1920, 1080);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Extrato", new PainelExtrato(gerenciador, persistencia, this));
        abas.addTab("Relatórios", new PainelRelatorios(gerenciador));
        abas.addTab("Nova Transação", new PainelFormulario(gerenciador, persistencia, this));
        add(abas, BorderLayout.CENTER);

        // Cria e mostra o saldo mostra no canto inferior esquerdo
        labelSaldo = new JLabel();
        JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelStatus.add(labelSaldo);
        add(panelStatus, BorderLayout.SOUTH);
        atualizarSaldo();
    }

    /**
     * Chamado para atualizar o saldo
     */
    public void atualizarSaldo(){
        double saldo = gerenciador.calcularSaldo();
        labelSaldo.setText("saldo atual: R$ "+ String.format("%.2f",saldo));
    }
}
