import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
/**
 * Painel de relatórios financeiros organizado em três abas:
 * - Período: totais de receitas e despesas em um intervalo de datas
 * - Categoria: total gasto por cada categoria de despesa
 * - Evolução: saldo acumulado transação a transação em ordem cronológica
 */
public class PainelRelatorios extends JPanel {
    private GerenciadorTransacoes gerenciador;

    private JTextField dataInicio;
    private JTextField dataFim;

    private JLabel totalDespesas;
    private JLabel totalReceitas;
    private JLabel saldo;

    /**
     * Cria o painel de relatórios com as três abas disponíveis.
     *
     * @param g gerenciador que fornece os dados para os cálculos
     */
    public PainelRelatorios(GerenciadorTransacoes g){
        this.gerenciador = g;
        setLayout(new BorderLayout());

        JTabbedPane abas = new JTabbedPane();

        abas.addTab("Periodo", criarPainelPeriodo());
        abas.addTab("Categoria", criarPainelCategoria());
        abas.addTab("Evolução", criarPainelEvolucao());

        add(abas, BorderLayout.CENTER);
    }
    /**
     * Cria o painel de relatório por período com campos de data e labels de resultado.
     *
     * @return JPanel configurado com os componentes do relatório de período
     */
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
    /**
     * Calcula os totais do período informado e atualiza os labels de resultado.
     * Chamado ao clicar no botão Gerar do painel de período.
     */
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

        // soma receitas e despesas separadamente para exibir os três valores
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
    /**
     * Cria o painel de relatório por categoria com área de texto formatada.
     * Exibe o total gasto em cada categoria de despesa ao clicar no botão.
     *
     * @return JPanel configurado com o relatório de categorias
     */
    private JPanel criarPainelCategoria() {
        JPanel painel = new JPanel(new BorderLayout());

        JTextArea areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        // fonte monoespaçada para alinhar os valores corretamente
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JButton btnAtualizar = new JButton("Atualizar Gastos por Categoria");

        btnAtualizar.addActionListener(e -> {
            var mapa = gerenciador.calcularTotalPorCategoria();
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== TOTAL DE DESPESAS POR CATEGORIA ===\n\n");

            if(mapa.isEmpty()) {
                relatorio.append("Nenhuma despesa registrada.");
            } else {
                // %-15s alinha o nome da categoria em 15 caracteres para facilitar leitura
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
    /**
     * Cria o painel de evolução do saldo ao longo das transações.
     * Exibe cada transação com o saldo acumulado após ela.
     *
     * @return JPanel configurado com o relatório de evolução
     */
    private JPanel criarPainelEvolucao() {
        JPanel painel = new JPanel(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JButton btn = new JButton("Ver Evolução");
        btn.addActionListener(e -> {
            double saldo = 0;
            StringBuilder sb = new StringBuilder("=== EVOLUÇÃO DO SALDO ===\n\n");
            // percorre as transações em ordem de cadastro calculando o saldo progressivo
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