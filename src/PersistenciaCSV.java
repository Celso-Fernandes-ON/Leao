import java.io.*;
import java.util.ArrayList;

public class PersistenciaCSV {
    private static final String CAMINHO = "dados/transacoes.csv";
    public void salvarTransacoes(ArrayList<Transacao> lista){
        new File("dados").mkdirs();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(CAMINHO));

            for(Transacao t: lista){
                out.write(t.getId() + ";" + t.getTipo() + ";" + t.getValor() + ";" + t.getDescricao() + ";" + t.getData() + ";" + t.getCategoria());
                out.newLine();
            }
            out.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
    public ArrayList<Transacao> carregarTransacoes(){
        ArrayList<Transacao> lista = new ArrayList<>();
        if (!new File(CAMINHO).exists()){
            return lista;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(CAMINHO));
            String linha;

            while ((linha = reader.readLine()) != null) {
                String [] partes = linha.split(";");
                int id = Integer.parseInt(partes[0]);
                String tipo = partes[1];
                String categoria = partes[5];
                String data = partes[3];
                String descricao = partes[4];
                double valor = Double.parseDouble(partes[2]);
                lista.add(new Transacao(id, tipo, categoria, data, descricao, valor));
            }
            reader.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return lista;
    }
}