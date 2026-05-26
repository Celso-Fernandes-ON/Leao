import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PainelRelatorios extends JPanel {
    private GerenciadorTransacoes gerenciador;

    private JTextField dataInicio;
    private JTextField dataFim;

    private JLabel totalDespesas;
    private JLabel totalReceitas;
    private JLabel saldo;


    public PainelRelatorios(GerenciadorTransacoes g){
        this.gerenciador = g;
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();

        abas.addTab("Periodo", criarPainelPeriodo());
        abas.addTab("Categoria", criarPainelCategoria());
        abas.addTab("Evolução", criarPainelEvolucao());

        add(abas, BorderLayout.CENTER);
    }

    private JPanel criarPainelPeriodo(){
        JPanel painel = new JPanel(new GridLayout(6,2,10,10));

        dataInicio = new JTextField();
        dataFim = new JTextField();

        totalReceitas = new JLabel("Receitas: R$ 0,00");
        totalDespesas = new JLabel("Despesas: R$ 0,00");
        saldo= new JLabel("Saldo: R$ 0,00");

        JButton btnGerar = new JButton("Gerar");
        btnGerar.addActionListener(e -> gerarRelatorioPeriodo());

        painel.add(new JLabel("Data início:"));
        painel.add(dataInicio);
        painel.add(new JLabel("Data Fim:"));
        painel.add(dataFim);

        painel.add(btnGerar);

        painel.add(new JLabel(""));

        painel.add(totalReceitas);
        painel.add(totalDespesas);
        painel.add(saldo);

        return painel;
    }

    private void gerarRelatorioPeriodo() {
        String inicio = dataInicio.getText();
        String fim    = dataFim.getText();

        if (!Validador.validarData(inicio) || !Validador.validarData(fim)) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use o formato dd/MM/yyyy.");
            return;
        }

        ArrayList<Transacao> resultado = gerenciador.filtrarPorPeriodo(inicio, fim);

        double receitas  = 0;
        double despesas  = 0;

        for (Transacao t : resultado) {
            if (t.getTipo().equals("RECEITA")) {
                receitas += t.getValor();
            } else {
                despesas += t.getValor();
            }
        }

        totalReceitas.setText("Receitas: R$ " + String.format("%.2f", receitas));
        totalDespesas.setText("Despesas: R$ " + String.format("%.2f", despesas));
        saldo.setText("Saldo: R$ " + String.format("%.2f", receitas - despesas));
    }

    private JPanel criarPainelCategoria(){
        JPanel painel = new JPanel();
        painel.add(new JLabel("Gráfico por categoria"));
        return painel;
    }

    private JPanel criarPainelEvolucao(){
        JPanel painel = new JPanel();
        painel.add(new JLabel("Evolução do saldo"));
        return painel;
    }
}