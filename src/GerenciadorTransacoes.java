import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Camada de negócio do sistema — centraliza toda a lógica de operação sobre as transações.
 * É responsável por adicionar, remover, editar, filtrar e calcular dados financeiros.
 * Não acessa arquivos diretamente — isso é responsabilidade da PersistenciaCSV.
 */

public class GerenciadorTransacoes {
    private ArrayList<Transacao> lista;
    private int proximoID;
    /**
     * Inicializa o gerenciador com uma lista de transações já carregadas.
     * Calcula o próximo ID disponível a partir do maior ID existente na lista,
     * garantindo que nenhum ID seja repetido após recarregar os dados do arquivo.
     *
     * @param listaCarregada lista de transações lida do arquivo CSV
     */

    public GerenciadorTransacoes(ArrayList<Transacao> listaCarregada) {
        this.lista = listaCarregada;
        this.proximoID = 1;
        // percorre a lista para encontrar o maior ID existente
        for (Transacao t : listaCarregada){
            if(t.getId() >= this.proximoID){
                this.proximoID = t.getId() + 1;
            }
        }
    }
    /**
     * Adiciona uma nova transação à lista com ID gerado automaticamente.
     * O ID é incrementado a cada nova transação para garantir unicidade.
     *
     * @param tipo      "RECEITA" ou "DESPESA"
     * @param categoria categoria da transação
     * @param data      data no formato dd/MM/yyyy
     * @param descricao descrição opcional
     * @param valor     valor em reais, deve ser maior que zero
     */

    public void adicionarTransacao(String tipo, String categoria, String data, String descricao, double valor){
        Transacao t = new Transacao(proximoID, tipo, categoria, data, descricao, valor);
        lista.add(t);
        proximoID++;

    }
    /**
     * Calcula o saldo atual somando todas as receitas e subtraindo todas as despesas.
     *
     * @return saldo total como double — pode ser negativo se as despesas superarem as receitas
     */

    public double calcularSaldo(){
        double saldo = 0;
        for(Transacao t: lista){
            if (t.getTipo().equals("RECEITA")){
                saldo += t.getValor();
            }
            else {
                saldo -= t.getValor();
            }
        }
        return saldo;
    }
    /**
     * Busca uma transação pelo seu identificador único.
     *
     * @param id o identificador da transação
     * @return a transação encontrada, ou null se não existir
     */

    public Transacao buscarPorId(int id){
        for (Transacao t: lista){
            if (t.getId() == id){
                return t;
            }
        }
        return null;
    }
    /**
     * Retorna uma cópia da lista completa de transações.
     * Retorna cópia para evitar que alterações externas modifiquem a lista interna.
     *
     * @return nova ArrayList com todas as transações
     */

    public ArrayList<Transacao> listarTodas(){
        return new ArrayList<>(lista);
    }
    /**
     * Remove uma transação da lista pelo seu identificador.
     *
     * @param id o identificador da transação a ser removida
     * @return true se removida com sucesso, false se o ID não foi encontrado
     */

    public boolean removerPorId(int id){
        Transacao t = buscarPorId(id);
        // retorna false se nenhuma transação com esse ID for encontrada
        if(t == null){
            return false;
        }
        lista.remove(t);
        return true;
    }
    /**
     * Edita os dados de uma transação existente pelo ID.
     * O tipo e o ID não podem ser alterados após a criação.
     *
     * @param id        identificador da transação a editar
     * @param tipo      tipo original da transação (não alterado)
     * @param categoria nova categoria
     * @param data      nova data no formato dd/MM/yyyy
     * @param descricao nova descrição
     * @param valor     novo valor em reais
     * @return true se editada com sucesso, false se o ID não foi encontrado
     */

    public boolean editarTransacao(int id, String tipo, String categoria, String data, String descricao,double valor){
        Transacao t = buscarPorId(id);
        if (t == null){
            return false;
        }
        // atualiza apenas os campos editáveis — o id não muda

        t.setCategoria(categoria);
        t.setValor(valor);
        t.setData(data);
        t.setDescricao(descricao);
        return true;
    }
    /**
     * Filtra as transações pelo tipo (RECEITA ou DESPESA).
     *
     * @param tipo tipo a filtrar — "RECEITA" ou "DESPESA"
     * @return nova lista contendo apenas as transações do tipo informado
     */

    public ArrayList<Transacao> filtrarPorTipo(String tipo){
        ArrayList<Transacao> resultado = new ArrayList<>();
        for (Transacao t: lista){
            if (t.getTipo().equals(tipo)){
                resultado.add(t);
            }
        }
        return resultado;
    }
    /**
     * Filtra as transações por categoria.
     *
     * @param categoria nome da categoria a filtrar
     * @return nova lista contendo apenas as transações da categoria informada
     */

    public ArrayList<Transacao> filtrarPorCategoria(String categoria){
        ArrayList<Transacao> resultado = new ArrayList<>();
        for (Transacao t: lista){
            if (t.getCategoria().equals(categoria)){
                resultado.add(t);
            }
        }
        return resultado;
    }
    /**
     * Filtra as transações dentro de um intervalo de datas (inclusive).
     * Utiliza LocalDate para comparação correta de datas no formato dd/MM/yyyy.
     *
     * @param dataInicio data inicial do período no formato dd/MM/yyyy
     * @param dataFim    data final do período no formato dd/MM/yyyy
     * @return nova lista com as transações dentro do período informado
     */

    public ArrayList<Transacao> filtrarPorPeriodo(String dataInicio, String dataFim) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate inicio = LocalDate.parse(dataInicio, fmt);
        LocalDate fim = LocalDate.parse(dataFim, fmt);

        ArrayList<Transacao> resultado = new ArrayList<>();
        for (Transacao t: lista){
            // converte a data da transação para LocalDate para permitir comparação
            LocalDate tData = LocalDate.parse(t.getData(), fmt);
            // isBefore/isAfter com negação garante inclusão das datas limite
            if (!tData.isBefore(inicio) && !tData.isAfter(fim)){
                resultado.add(t);
            }
        }
        return resultado;
    }
    /**
     * Calcula o total gasto em cada categoria de despesa.
     * Ignora transações do tipo RECEITA.
     *
     * @return Map onde a chave é o nome da categoria e o valor é o total gasto nela
     */

    public Map<String, Double> calcularTotalPorCategoria(){
        HashMap<String, Double> mapa = new HashMap<>();
        for (Transacao t:lista){
            if (t.getTipo().equals("DESPESA")){
                String categoria = t.getCategoria();
                if (mapa.containsKey(categoria)){
                    // categoria já existe no mapa — soma o novo valor ao acumulado
                    mapa.put(categoria, mapa.get(categoria) + t.getValor());
                }
                else {
                    // primeira ocorrência da categoria — insere com o valor inicial
                    mapa.put(categoria, t.getValor());
                }
            }
        }
        return mapa;
    }
}