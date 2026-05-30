/**
 * Representa uma movimentação financeira do sistema.
 * Pode ser uma receita (entrada) ou despesa (saída).
 * É o modelo central do sistema — todas as outras classes operam sobre objetos desta classe.
 */
public class Transacao {
    private int id;
    private String tipo;
    private String categoria;
    private String data;
    private String descricao;
    private double valor;
    /**
     * Cria uma nova transação com todos os dados obrigatórios.
     *
     * @param id        identificador único gerado pelo GerenciadorTransacoes
     * @param tipo      "RECEITA" para entradas ou "DESPESA" para saídas
     * @param categoria categoria da transação (ex: "Alimentação", "Salário")
     * @param data      data no formato dd/MM/yyyy
     * @param descricao descrição opcional da transação
     * @param valor     valor em reais, deve ser maior que zero
     */

    public Transacao(int id,String tipo, String categoria, String data, String descricao, double valor){
        this.id = id;
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
    }
    /** @return identificador único da transação */
    public int getId(){
        return this.id;
    }

    /** @return "RECEITA" ou "DESPESA" */
    public String getTipo(){
        return this.tipo;
    }

    /** @return categoria da transação */
    public String getCategoria(){
        return this.categoria;
    }

    /** @return data no formato dd/MM/yyyy */
    public String getData(){
        return this.data;
    }

    /** @return descrição opcional da transação */
    public String getDescricao(){
        return this.descricao;
    }

    /** @return valor em reais */
    public double getValor(){
        return this.valor;
    }

    /** @param valor novo valor — deve ser maior que zero */
    public void setValor(double valor) {
        this.valor = valor;
    }

    /** @param tipo novo tipo — "RECEITA" ou "DESPESA" */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /** @param descricao nova descrição */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /** @param data nova data no formato dd/MM/yyyy */
    public void setData(String data) {
        this.data = data;
    }

    /** @param categoria nova categoria */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Retorna uma representação textual da transação.
     * Útil para debug e para exibição no terminal.
     *
     * @return String formatada com id, data, tipo, categoria, valor e descrição
     */
    @Override
    public String toString(){

        return "[" + id + "] " + data + " | " + tipo + " | " + categoria + " | R$ " + String.format("%.2f", valor) + " | " + descricao;
    }
}
