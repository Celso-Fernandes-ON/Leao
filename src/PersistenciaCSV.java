import java.io.*;
import java.util.ArrayList;

public class PersistenciaCSV {
    private static final String CAMINHO = "dados/transacoes.csv";
    public void salvarTransacoes(ArrayList<Transacao> lista){
        new File("dados").mkdirs();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(CAMINHO));

            for(Transacao t: lista){
                String descricaoSegura = t.getDescricao().replace(";", ",");
                out.write(t.getId() + ";" + t.getTipo() + ";" + t.getCategoria() + ";" + t.getData() + ";" + descricaoSegura + ";" + t.getValor());
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
                double valor = Double.parseDouble(partes[2]);
                String descricao = partes[3];
                String data = partes[4];
                String categoria = partes[5];
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