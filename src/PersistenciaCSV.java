import java.io.*;
import java.util.ArrayList;

/**
 * Classe que cria e manipula os dados salvos no arquivo csv
 */
public class PersistenciaCSV {
    private static final String CAMINHO = "dados/transacoes.csv";

    /**
     * Salva todas as transações no arquivo CSV.
     * Cada transação é armazenada em uma linha separada.
     *
     * @param lista lista de transações a serem salvas
     */

    public void salvarTransacoes(ArrayList<Transacao> lista){
        new File("dados").mkdirs();
        // Cria um BufferedWriter para escrever os dados no arquivo
        try {
            BufferedWriter escrever = new BufferedWriter(new FileWriter(CAMINHO));
            // Loop para escrever as todas as linha no arquivo
            for(Transacao t: lista){
                String descricaoSegura = t.getDescricao().replace(";", ",");
                escrever.write(t.getId() + ";" + t.getTipo() + ";" + t.getCategoria() + ";" + t.getData() + ";" + descricaoSegura + ";" + t.getValor());
                escrever.newLine();
            }
            // Finalizar o Buffer escrever
            escrever.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Carrega as Transações salvas no arquivo
     * @return lista
     */
    public ArrayList<Transacao> carregarTransacoes(){
        ArrayList<Transacao> lista = new ArrayList<>();
        // Se não existir o caminho indicado retorna uma lista vazia.
        if (!new File(CAMINHO).exists()){
            return lista;
        }
        try {
            // Criará um Buffer para ler o conteudo do arquivo.
            BufferedReader lido = new BufferedReader(new FileReader(CAMINHO));
            String linha;
            // Lê o arquivo linha por linha
            // Cada linha é convertida em um objeto Transacao
            while ((linha = lido.readLine()) != null) {
                String [] partes = linha.split(";");
                int id = Integer.parseInt(partes[0]);
                String tipo = partes[1];
                String categoria = partes[2];
                String data = partes[3];
                String descricao = partes[4];
                double valor = Double.parseDouble(partes[5]);
                lista.add(new Transacao(id, tipo, categoria, data, descricao, valor));
            }
            //finaliza e fecha o Buffer.
            lido.close();
        }
        // Exceção caso o arquivo exista mas ocorra erro na leitura
        catch (IOException e){
            e.printStackTrace();
        }
        return lista;
    }
}