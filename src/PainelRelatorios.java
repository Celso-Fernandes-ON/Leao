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
        String fim = dataFim.getText();

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

    private JPanel criarPainelCategoria() {
        JPanel painel = new JPanel(new BorderLayout());

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JButton btnAtualizar = new JButton("Atualizar Gastos por Categoria");

        btnAtualizar.addActionListener(e -> {
            var mapa = gerenciador.calcularTotalPorCategoria();
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== TOTAL DE DESPESAS POR CATEGORIA ===\n\n");

            if(mapa.isEmpty()) {
                relatorio.append("Nenhuma despesa registrada.");
            } else {
                for (String categoria : mapa.keySet()) {
                    relatorio.append(String.format("%-15s : R$ %.2f\n", categoria, mapa.get(categoria)));
                }
            }
            areaTexto.setText(relatorio.toString());
        });

        painel.add(btnAtualizar, BorderLayout.NORTH);
        painel.add(new JScrollPane(areaTexto), BorderLayout.CENTER);

        return painel;
    }
    private JPanel criarPainelEvolucao() {
        JPanel painel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JButton btn = new JButton("Ver Evolução");
        btn.addActionListener(e -> {
            double saldo = 0;
            StringBuilder sb = new StringBuilder("=== EVOLUÇÃO DO SALDO ===\n\n");
            for (Transacao t : gerenciador.listarTodas()) {
                if (t.getTipo().equals("RECEITA")) saldo += t.getValor();
                else saldo -= t.getValor();
                sb.append(t.getData()).append(" | ").append(t.getTipo())
                        .append(" | R$ ").append(String.format("%.2f", t.getValor()))
                        .append(" → Saldo: R$ ").append(String.format("%.2f", saldo)).append("\n");
            }
            area.setText(sb.toString());
        });

        painel.add(btn, BorderLayout.NORTH);
        painel.add(new JScrollPane(area), BorderLayout.CENTER);
        return painel;
    }
}