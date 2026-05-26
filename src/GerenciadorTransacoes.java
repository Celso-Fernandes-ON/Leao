import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GerenciadorTransacoes {
    private ArrayList<Transacao> lista;
    private int proximoID;

    public GerenciadorTransacoes(ArrayList<Transacao> listaCarregada) {
        this.lista = listaCarregada;
        this.proximoID = 1;
        for (Transacao t : listaCarregada){
            if(t.getId() >= this.proximoID){
                this.proximoID = t.getId() + 1;
            }
        }
    }
    public void adicionarTransacao(String tipo, String categoria, String data, String descricao, double valor){
        Transacao t = new Transacao(proximoID, tipo, categoria, data, descricao, valor);
        lista.add(t);
        proximoID++;

    }

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
    public Transacao buscarPorId(int id){
        for (Transacao t: lista){
            if (t.getId() == id){
                return t;
            }
        }
        return null;
    }

    public ArrayList<Transacao> listarTodas(){
        return this.lista;
    }
    public boolean removerPorId(int id){
        Transacao t = buscarPorId(id);
        if(t == null){
            return false;
        }
        lista.remove(t);
        return true;
    }

    public boolean editarTransacao(int id, String tipo, String categoria, String data, String descricao,double valor){
        Transacao t = buscarPorId(id);
        if (t == null){
            return false;
        }
        t.setCategoria(categoria);
        t.setValor(valor);
        t.setData(data);
        t.setDescricao(descricao);
        return true;
    }
    public ArrayList<Transacao> filtrarPorTipo(String tipo){
        ArrayList<Transacao> resultado = new ArrayList<>();
        for (Transacao t: lista){
            if (t.getTipo().equals(tipo)){
                resultado.add(t);
            }
        }
        return resultado;
    }
    public ArrayList<Transacao> filtrarPorCategoria(String categoria){
        ArrayList<Transacao> resultado = new ArrayList<>();
        for (Transacao t: lista){
            if (t.getCategoria().equals(categoria)){
                resultado.add(t);
            }
        }
        return resultado;
    }

    public ArrayList<Transacao> filtrarPorPeriodo(String dataInicio, String dataFim) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate inicio = LocalDate.parse(dataInicio, fmt);
        LocalDate fim = LocalDate.parse(dataFim, fmt);

        ArrayList<Transacao> resultado = new ArrayList<>();
        for (Transacao t: lista){
            LocalDate tData = LocalDate.parse(t.getData(), fmt);
            if (!tData.isBefore(inicio) && !tData.isAfter(fim)){
                resultado.add(t);
            }
        }
        return resultado;
    }

    public Map<String, Double> calcularTotalPorCategoria(){
        HashMap<String, Double> mapa = new HashMap<>();
        for (Transacao t:lista){
            if (t.getTipo().equals("DESPESA")){
                String categoria = t.getCategoria();
                if (mapa.containsKey(categoria)){
                    mapa.put(categoria, mapa.get(categoria) + t.getValor());
                }
                else {
                    mapa.put(categoria, t.getValor());
                }
            }
        }
        return mapa;
    }
}
