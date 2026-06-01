import javax.swing.*;
import java.util.ArrayList;
import com.formdev.flatlaf.FlatDarculaLaf;

/**
 * Ponto de entrada do sistema de Gerenciamento de Finanças Pessoais.
 * Responsável por inicializar as dependências e abrir a interface gráfica.
 */
public class Main {
    /**
     * Método principal — executa a inicialização do sistema na ordem correta:
     * 1. Carrega os dados salvos do arquivo CSV
     * 2. Instancia o gerenciador com os dados carregados
     * 3. Abre a janela principal na thread do Swing (EDT)
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try {
            FlatDarculaLaf.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PersistenciaCSV persistencia = new PersistenciaCSV();
        ArrayList<Transacao> lista = persistencia.carregarTransacoes();
        GerenciadorTransacoes gerenciador = new GerenciadorTransacoes(lista);

        // invokeLater garante que a interface seja criada na Event Dispatch Thread (EDT)
        // rodar Swing fora da EDT pode causar travamentos e comportamento imprevisível
        SwingUtilities.invokeLater(() -> {
            new JanelaPrincipal(gerenciador, persistencia).setVisible(true);
        });
    }
}