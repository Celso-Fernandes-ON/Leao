import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
/**
 * Painel de cadastro de novas transações financeiras.
 * Contém formulário com campos de tipo, categoria, valor, data e descrição.
 * As categorias disponíveis mudam automaticamente conforme o tipo selecionado.
 */
public class PainelFormulario extends JPanel {

    private JComboBox<String> comboTipo;
    private JComboBox<String> comboCategoria;

    private JTextField campoValor;
    private JTextField campoData;
    private JTextField campoDescricao;

    // categorias separadas por tipo para evitar combinações inválidas (ex: "Salário" como despesa)
    private final String[] categoriasReceita = {"Salário", "Renda fixa", "Investimentos", "Transferência", "Outros"};
    private final String[] categoriasDespesa = {"Alimentação", "Transferência", "Transporte", "Lazer", "Saúde", "Contas", "Outros"};

    private GerenciadorTransacoes gerenciador;
    private PersistenciaCSV persistencia;
    private JanelaPrincipal janelaPai;

    /**
     * Cria o painel de formulário e monta todos os componentes visuais.
     *
     * @param gerenciador responsável por adicionar a transação à lista
     * @param persistencia responsável por salvar os dados no arquivo após cada inserção
     * @param janelaPai   referência à janela principal para atualizar o saldo após salvar
     */
    public PainelFormulario(GerenciadorTransacoes gerenciador, PersistenciaCSV persistencia, JanelaPrincipal janelaPai) {

        // tenta criar campo de data com máscara visual dd/MM/yyyy
        // se falhar por algum motivo, usa um JTextField simples como alternativa
        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            campoData = new JFormattedTextField(mascaraData);
            campoData.setColumns(15);
        } catch (ParseException e) {
            campoData = new JTextField(15);
        }

        this.gerenciador = gerenciador;
        this.persistencia = persistencia;
        this.janelaPai = janelaPai;

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        comboTipo = new JComboBox<>(new String[]{"RECEITA", "DESPESA"});

        comboCategoria = new JComboBox<>();
        // atualiza as categorias sempre que o tipo for alterado
        comboTipo.addActionListener(e -> atualizarCategorias());
        atualizarCategorias();

        campoValor = new JTextField(15);

        campoDescricao = new JTextField(15);

        JButton btnSalvar = new JButton("Salvar Transação");


        // montagem do layout linha a linha usando GridBagConstraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        add(comboTipo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Categoria:"), gbc);

        gbc.gridx = 1;
        add(comboCategoria, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Valor:"), gbc);

        gbc.gridx = 1;
        add(campoValor, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Data:"), gbc);

        gbc.gridx = 1;
        add(campoData, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Descrição:"), gbc);

        gbc.gridx = 1;
        add(campoDescricao, gbc);

        // botão ocupa as duas colunas (gridwidth = 2)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        add(btnSalvar, gbc);


        btnSalvar.addActionListener(e -> salvarTransacao());
    }
    /**
     * Valida os campos e salva a transação ao clicar no botão.
     * A ordem de validação é: campo vazio → valor inválido → data inválida.
     * Se todas as validações passarem, adiciona, persiste e atualiza a interface.
     */
    private void salvarTransacao() {

        String tipo = comboTipo.getSelectedItem().toString();
        String categoria =  comboCategoria.getSelectedItem().toString();
        String valorTexto =  campoValor.getText();
        String data = campoData.getText();
        String descricao = campoDescricao.getText();

        if (!Validador.validarCampoObrigatorio(valorTexto)) {
            JOptionPane.showMessageDialog(this, "Digite um valor.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double valor = Double.parseDouble(valorTexto);

        if (!Validador.validarValor(valor)) {
            JOptionPane.showMessageDialog(this, "Valor deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // verifica o formato da data antes de tentar salvar
        if (!Validador.validarData(data)) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use 23/04/2025.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        gerenciador.adicionarTransacao(tipo, categoria, data, descricao, valor);
        persistencia.salvarTransacoes(gerenciador.listarTodas());

        // atualiza o saldo na barra inferior da janela principal
        janelaPai.atualizarSaldo();

        JOptionPane.showMessageDialog(this,"Transação salva!");
        limparCampos();
    }
    /**
     * Limpa os campos editáveis após salvar uma transação.
     * Os combos de tipo e categoria não são resetados intencionalmente —
     * o usuário provavelmente vai cadastrar outra transação do mesmo tipo.
     */
    private void limparCampos() {

        campoValor.setText("");
        campoData.setText("");
        campoDescricao.setText("");
    }
    /**
     * Atualiza as opções do comboCategoria conforme o tipo selecionado.
     * Chamado automaticamente ao trocar o tipo no comboTipo.
     */
    private void atualizarCategorias() {
        comboCategoria.removeAllItems();
        String tipoSelecionado = comboTipo.getSelectedItem().toString();

        // carrega o array de categorias correspondente ao tipo escolhido
        String[] categorias = tipoSelecionado.equals("RECEITA") ? categoriasReceita : categoriasDespesa;
        for (String cat : categorias) {
            comboCategoria.addItem(cat);
        }
    }
}