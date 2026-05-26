import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PainelExtrato extends JPanel {

    private JTable tabela;
    private DefaultTableModel modelo;

    private JComboBox<String> comboFiltroTipo;

    private JTextField campoDataInicio;
    private JTextField campoDataFim;

    private GerenciadorTransacoes gerenciador;
    private PersistenciaCSV persistencia;

    public PainelExtrato( GerenciadorTransacoes gerenciador, PersistenciaCSV persistencia, JanelaPrincipal janelaPrincipal) {

        this.gerenciador = gerenciador;
        this.persistencia = persistencia;

        setLayout(new BorderLayout());


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
        modelo = new DefaultTableModel( new String[]{"ID", "Data", "Tipo", "Categoria", "Valor", "Descrição"},0 );
        tabela = new JTable(modelo);

        JScrollPane scroll =  new JScrollPane(tabela);

        add(scroll, BorderLayout.CENTER);


        JPanel painelBotoes = new JPanel();

        JButton btnExcluir = new JButton("Excluir");
        JButton btnEditar = new JButton("Editar");

        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);

        add(painelBotoes, BorderLayout.SOUTH);


        btnFiltrar.addActionListener(e -> aplicarFiltro());
        btnExcluir.addActionListener(e -> excluirTransacao());
        btnEditar.addActionListener(e -> editarTrasacao());


        carregarTabela(gerenciador.listarTodas());
    }


    public void carregarTabela(ArrayList<Transacao> lista ) {

        modelo.setRowCount(0);

        for (Transacao t : lista) {
            modelo.addRow(new Object[]{ t.getId(), t.getData(), t.getTipo(), t.getCategoria(), String.format("R$ %.2f", t.getValor()),t.getDescricao()});
        }
    }


    private void aplicarFiltro() {

        String tipo = comboFiltroTipo.getSelectedItem().toString();

        ArrayList<Transacao> lista = gerenciador.listarTodas();

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
        }
    }
    private void editarTrasacao(){

    }
}