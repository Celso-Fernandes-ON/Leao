import javax.swing.*;
import javax.swing.border.EmptyBorder;
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

    private JLabel valorDespesas;
    private JLabel valorReceitas;
    private JLabel valorSaldo;

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
        JPanel externo = new JPanel(new BorderLayout());
        externo.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel conteudo = new JPanel();
        conteudo.setLayout(new BoxLayout(conteudo, BoxLayout.Y_AXIS));
        conteudo.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));

        JLabel lblInicio = new JLabel("Data início:");
        lblInicio.setFont(lblInicio.getFont().deriveFont(Font.BOLD, 13f));
        lblInicio.setAlignmentX(Component.LEFT_ALIGNMENT);

        dataInicio = new JTextField();
        dataInicio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        dataInicio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblFim = new JLabel("Data Fim:");
        lblFim.setFont(lblInicio.getFont().deriveFont(Font.BOLD, 13f));
        lblFim.setAlignmentX(Component.LEFT_ALIGNMENT);

        dataFim = new JTextField();
        dataFim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        dataFim.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnGerar = new JButton("Gerar");
        btnGerar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnGerar.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGerar.setFont(btnGerar.getFont().deriveFont(Font.BOLD, 14f));

        btnGerar.addActionListener(e -> gerarRelatorioPeriodo());


        JPanel cardReceitas = criarCard("Receitas","R$ 0,00", new Color(39, 174, 96));
        JPanel cardDespesas = criarCard("Despesas","R$ 0,00", new Color(192, 57, 43));
        JPanel cardSaldo= criarCard("Saldo","R$ 0,00", new Color(41, 128, 185));

        valorReceitas = (JLabel) ((JPanel) cardReceitas.getComponent(0)).getComponent(1);
        valorDespesas = (JLabel) ((JPanel) cardDespesas.getComponent(0)).getComponent(1);
        valorSaldo = (JLabel) ((JPanel) cardSaldo.getComponent(0)).getComponent(1);

        conteudo.add(lblInicio);
        conteudo.add(Box.createVerticalStrut(6));
        conteudo.add(dataInicio);
        conteudo.add(Box.createVerticalStrut(16));

        conteudo.add(lblFim);
        conteudo.add(Box.createVerticalStrut(6));
        conteudo.add(dataFim);
        conteudo.add(Box.createVerticalStrut(20));

        conteudo.add(btnGerar);
        conteudo.add(Box.createVerticalStrut(24));

        conteudo.add(cardReceitas);
        conteudo.add(Box.createVerticalStrut(10));
        conteudo.add(cardDespesas);
        conteudo.add(Box.createVerticalStrut(10));
        conteudo.add(cardSaldo);

        JPanel wrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 40, 0, 40);
        wrapper.add(conteudo, gbc);

        externo.add(wrapper, BorderLayout.NORTH);
        return externo;
    }
    /**
     * Cria um card visual com título e valor colorido para exibir resultados.
     *
     * @param titulo       texto do título exibido à esquerda
     * @param valorInicial valor inicial antes de gerar o relatório
     * @param corValor     cor do texto do valor
     * @return JPanel estilizado como card de resultado
     */
    private JPanel criarCard(String titulo, String valorInicial, Color corValor){
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1, true), new EmptyBorder(12, 16, 12, 16)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel interno = new JPanel(new BorderLayout());
        interno.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.PLAIN, 14f));

        JLabel lblValor = new JLabel(valorInicial);
        lblValor.setFont(lblTitulo.getFont().deriveFont(Font.PLAIN, 15f));
        lblValor.setForeground(corValor);
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        interno.add(lblTitulo, BorderLayout.WEST);
        interno.add(lblValor, BorderLayout.EAST);

        card.add(interno, BorderLayout.CENTER);

        return card;
    }
    /**
     * Calcula os totais do período informado e atualiza os labels de resultado.
     * Chamado ao clicar no botão Gerar do painel de período.
     */
    private void gerarRelatorioPeriodo() {
        String inicio = dataInicio.getText().trim();
        String fim = dataFim.getText().trim();

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
        double saldoPeriodo = receitas - despesas;

        valorReceitas.setText("R$ " + String.format("%.2f", receitas));
        valorDespesas.setText("R$ " + String.format("%.2f", despesas));
        valorSaldo.setText("R$ " + String.format("%.2f", saldoPeriodo));

        valorSaldo.setForeground(saldoPeriodo >= 0 ? new Color(41,128,185) : new Color(192,57,43));
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