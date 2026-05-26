import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        PersistenciaCSV persistencia = new PersistenciaCSV();
        ArrayList<Transacao> lista = persistencia.carregarTransacoes();
        GerenciadorTransacoes gerenciador = new GerenciadorTransacoes(lista);
        SwingUtilities.invokeLater(() -> {
            new JanelaPrincipal(gerenciador, persistencia).setVisible(true);
        });
    }
}
