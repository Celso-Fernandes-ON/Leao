import javax.swing.*;
import java.awt.*;

public class JanelaPrincipal extends JFrame {
    private GerenciadorTransacoes gerenciador;
    private PersistenciaCSV persistencia;
    private JLabel labelSaldo;

    public JanelaPrincipal(GerenciadorTransacoes g, PersistenciaCSV p){
        this.gerenciador = g;
        this.persistencia = p;
        setTitle("Finanças Pessoais");
        setSize(950, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Extrato", new PainelExtrato(gerenciador, persistencia, this));
        abas.addTab("Relatórios", new PainelRelatorios(gerenciador));
        abas.addTab("Nova Trasação", new PainelFormulario(gerenciador, persistencia, this));
        add(abas, BorderLayout.CENTER);

        labelSaldo = new JLabel();
        JPanel panelStatus = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelStatus.add(labelSaldo);
        add(panelStatus, BorderLayout.SOUTH);
        atualizarSaldo();
    }
    public void atualizarSaldo(){
        double saldo = gerenciador.calcularSaldo();
        labelSaldo.setText("saldo atual: R$ "+ String.format("%.2f",saldo));
    }
}
