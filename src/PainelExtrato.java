import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Painel de extrato financeiro — exibe todas as transações em uma tabela.
 * Permite filtrar por tipo e por período de datas.
 * Oferece botões para editar e excluir transações selecionadas.
 */
public class PainelExtrato extends JPanel {

    private JTable tabela;
    private DefaultTableModel modelo;

    private JComboBox<String> comboFiltroTipo;

    private JTextField campoDataInicio;
    private JTextField campoDataFim;

    private GerenciadorTransacoes gerenciador;
    private PersistenciaCSV persistencia;
    private JanelaPrincipal janelaPrincipal;

    /**
     * Cria o painel de extrato com tabela, filtros e botões de ação.
     *
     * @param gerenciador    fonte dos dados de transações
     * @param persistencia   responsável por salvar após edição ou exclusão
     * @param janelaPrincipal referência à janela principal para atualizar o saldo
     */
    public PainelExtrato( GerenciadorTransacoes gerenciador, PersistenciaCSV persistencia, JanelaPrincipal janelaPrincipal) {
        this.gerenciador = gerenciador;
        this.persistencia = persistencia;
        this.janelaPrincipal = janelaPrincipal;

        setLayout(new BorderLayout());


        // painel de filtros no topo
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        comboFiltroTipo = new JComboBox<>(new String[]{"Todos","RECEITA","DESPESA" }  );

        campoDataInicio = new JTextField(8);
        campoDataFim = new JTextField(8);

        JButton btnFiltrar = new JButton("Filtrar");

        painelFiltros.add(new JLabel("Tipo:"));
        painelFiltros.add(comboFiltroTipo);

        painelFiltros.add(new JLabel("Início:"));
        painelFiltros.add(campoDataInicio);

        painelFiltros.add(new JLabel("Fim:"));
        painelFiltros.add(campoDataFim);

        painelFiltros.add(btnFiltrar);
        add(painelFiltros, BorderLayout.NORTH);

        // tabela central com as transações
        modelo = new DefaultTableModel( new String[]{"ID", "Data", "Tipo", "Categoria", "Valor", "Descrição"},0 );
        tabela = new JTable(modelo);

        JScrollPane scroll =  new JScrollPane(tabela);

        add(scroll, BorderLayout.CENTER);

        // botões de ação no rodapé
        JPanel painelBotoes = new JPanel();

        JButton btnExcluir = new JButton("Excluir");
        JButton btnEditar = new JButton("Editar");

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(painelBotoes, BorderLayout.SOUTH);

        // conecta cada botão ao seu método correspondente
        btnFiltrar.addActionListener(e -> aplicarFiltro());
        btnExcluir.addActionListener(e -> excluirTransacao());
        btnEditar.addActionListener(e -> editarTransacao());

        // carrega todas as transações ao abrir o painel
        carregarTabela(gerenciador.listarTodas());
    }

    /**
     * Popula a tabela com a lista de transações fornecida.
     * Limpa o conteúdo anterior antes de inserir os novos dados.
     * Público para permitir atualização externa após cadastro de nova transação.
     *
     * @param lista lista de transações a exibir
     */
    public void carregarTabela(ArrayList<Transacao> lista ) {

        modelo.setRowCount(0);

        for (Transacao t : lista) {
            modelo.addRow(new Object[]{ t.getId(), t.getData(), t.getTipo(), t.getCategoria(), String.format("R$ %.2f", t.getValor()),t.getDescricao()});
        }
    }

    /**
     * Aplica os filtros selecionados (tipo e/ou período) e recarrega a tabela.
     * Filtros em branco são ignorados — se nenhum for informado, exibe tudo.
     */
    private void aplicarFiltro() {

        String tipo = comboFiltroTipo.getSelectedItem().toString();
        String dataInicio = campoDataInicio.getText().trim();
        String dataFim = campoDataFim.getText().trim();

        ArrayList<Transacao> lista = gerenciador.listarTodas();

        // aplica filtro de período apenas se ambas as datas forem preenchidas e válidas
        if (!dataInicio.isEmpty() && !dataFim.isEmpty()) {
            if (Validador.validarData(dataInicio) && Validador.validarData(dataFim)) {
                lista = gerenciador.filtrarPorPeriodo(dataInicio, dataFim);
            } else {
                JOptionPane.showMessageDialog(this, "Datas inválidas para filtro. Use dd/MM/yyyy.");
                return;
            }
        }
        // aplica filtro de tipo sobre o resultado já filtrado por período
        if (!tipo.equals("Todos")) {
            ArrayList<Transacao> filtrada = new ArrayList<>();
            for (Transacao t : lista) {
                if (t.getTipo().equals(tipo)) {
                    filtrada.add(t);
                }
            }
            lista = filtrada;
        }
        carregarTabela(lista);
    }

    /**
     * Exclui a transação da linha selecionada na tabela após confirmação do usuário.
     * Atualiza o arquivo e o saldo após a remoção.
     */
    private void excluirTransacao() {
        int linha =tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this,"Selecione uma linha.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        int resposta = JOptionPane.showConfirmDialog(this,"Deseja excluir?","Confirmação", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION) {
            gerenciador.removerPorId(id);
            persistencia.salvarTransacoes(gerenciador.listarTodas());

            carregarTabela(gerenciador.listarTodas());
            janelaPrincipal.atualizarSaldo();

        }
    }
    /**
     * Abre um diálogo para editar a transação da linha selecionada.
     * Os campos são pré-preenchidos com os dados atuais da transação.
     * Valida os novos dados antes de confirmar a edição.
     */
    private void editarTransacao() {
        int linha = tabela.getSelectedRow();

        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para editar.");
            return;
        }

        int id = (int) modelo.getValueAt(linha, 0);
        Transacao t = gerenciador.buscarPorId(id);

        if (t != null) {
            // cria campos do diálogo pré-preenchidos com os dados atuais
            JTextField campoValor = new JTextField(String.valueOf(t.getValor()));
            JTextField campoData = new JTextField(t.getData());
            JTextField campoDescricao = new JTextField(t.getDescricao());
            JComboBox<String> comboCategoria = new JComboBox<>();

            // carrega categorias compatíveis com o tipo da transação
            if(t.getTipo().equals("RECEITA")){
                comboCategoria.setModel(new DefaultComboBoxModel<>(new String[]{"Salário", "Renda fixa", "Investimentos", "Transferência", "Outros"}));
            }
            else{
                comboCategoria.setModel(new DefaultComboBoxModel<>(new String[]{"Alimentação", "Transferência", "Transporte", "Lazer", "Saúde", "Contas", "Outros"}));
            }
            comboCategoria.setSelectedItem(t.getCategoria());

            Object[] mensagem = {"Categoria:", comboCategoria, "Valor:", campoValor, "Data (dd/MM/yyyy):", campoData, "Descrição:", campoDescricao};

            int opcao = JOptionPane.showConfirmDialog(this, mensagem, "Editar Transação", JOptionPane.OK_CANCEL_OPTION);

            if (opcao == JOptionPane.OK_OPTION) {
                try {
                    double novoValor = Double.parseDouble(campoValor.getText());
                    String novaData = campoData.getText();

                    if (!Validador.validarData(novaData)) {
                        JOptionPane.showMessageDialog(this, "Data inválida.");
                        return;
                    }

                    gerenciador.editarTransacao(id, t.getTipo(), comboCategoria.getSelectedItem().toString(), novaData, campoDescricao.getText(), novoValor);

                    persistencia.salvarTransacoes(gerenciador.listarTodas());
                    carregarTabela(gerenciador.listarTodas());
                    janelaPrincipal.atualizarSaldo();

                } catch (NumberFormatException ex) {
                    // captura entrada de texto no campo valor (ex: "abc")
                    JOptionPane.showMessageDialog(this, "Valor digitado é inválido.");
                }
            }
        }
    }
}