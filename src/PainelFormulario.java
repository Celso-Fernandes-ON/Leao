import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;

public class PainelFormulario extends JPanel {

    private JComboBox<String> comboTipo;
    private JComboBox<String> comboCategoria;

    private JTextField campoValor;
    private JTextField campoData;
    private JTextField campoDescricao;

    private final String[] categoriasReceita = {"Salário", "Renda fixa", "Investimentos", "Transferência", "Outros"};
    private final String[] categoriasDespesa = {"Alimentação", "Transferência", "Transporte", "Lazer", "Saúde", "Contas", "Outros"};

    private GerenciadorTransacoes gerenciador;
    private PersistenciaCSV persistencia;
    private JanelaPrincipal janelaPai;

    public PainelFormulario(GerenciadorTransacoes gerenciador, PersistenciaCSV persistencia, JanelaPrincipal janelaPai) {

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
        comboTipo.addActionListener(e -> atualizarCategorias());
        atualizarCategorias();

        campoValor = new JTextField(15);

        campoData = new JTextField("dd/MM/yyyy", 15);

        campoDescricao = new JTextField(15);

        JButton btnSalvar = new JButton("Salvar Transação");


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


        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;

        add(btnSalvar, gbc);


        btnSalvar.addActionListener(e -> salvarTransacao());
    }

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

        if (!Validador.validarData(data)) {
            JOptionPane.showMessageDialog(this, "Data inválida. Use dd/MM/yyyy.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        gerenciador.adicionarTransacao(tipo, categoria, data, descricao, valor);

        persistencia.salvarTransacoes(gerenciador.listarTodas());
        janelaPai.atualizarSaldo();

        JOptionPane.showMessageDialog(this,"Transação salva!");

        limparCampos();
    }

    private void limparCampos() {

        campoValor.setText("");
        campoData.setText("");
        campoDescricao.setText("");
    }
    private void atualizarCategorias() {
        comboCategoria.removeAllItems();

        String tipoSelecionado = comboTipo.getSelectedItem().toString();

        if (tipoSelecionado.equals("RECEITA")) {
            for (String cat : categoriasReceita) {
                comboCategoria.addItem(cat);
            }
        } else {
            for (String cat : categoriasDespesa) {
                comboCategoria.addItem(cat);
            }
        }
    }
}