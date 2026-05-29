/**
 * Representa uma movimentação financeira do sistema.
 * Pode ser uma receita (entrada) ou despesa (saída).
 */
public class Transacao {
    private int id;
    private String tipo;
    private String categoria;
    private String data;
    private String descricao;
    private double valor;

    public Transacao(int id,String tipo, String categoria, String data, String descricao, double valor){
        this.id = id;
        this.tipo = tipo;
        this.categoria = categoria;
        this.data = data;
        this.descricao = descricao;
        this.valor = valor;
    }
    public int getId(){
        return this.id;
    }
    public String getTipo(){
        return this.tipo;
    }
    public String getCategoria(){
        return this.categoria;
    }
    public String getData(){
        return this.data;
    }
    public String getDescricao(){
        return this.descricao;
    }
    public double getValor(){
        return this.valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Sobrescreve o método toString da classe
     * @return retorna uma String formatada contendo id, data, tipo, categoria, valordescricao
     */
    @Override
    public String toString(){

        return "[" + id + "] " + data + " | " + tipo + " | " + categoria + " | R$ " + String.format("%.2f", valor) + " | " + descricao;
    }
}
