import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Classe utilitária responsável por validar os dados de entrada do sistema.
 * Todos os métodos são estáticos, não é necessário instanciar essa classe
 */
public class Validador {
    /**
     * Verifica se o valor informado é valido para uma transação.
     * Zero ou negativo não são validos
     * @param valor valor a ser validado
     * @return true se o valor for maior de zero e false caso contrário
     */
    public static boolean validarValor(double valor){

        return valor > 0 ;
    }

    /**
     * Verifica se a data informada está no formato correto (dd/MM/yyyy).
     * utiliza tentativas de parse para detectar datas inválidas como "32/13/2025" ou textos fora do padrão;
     * @param data a String de data a ser validada
     * @return true se a data for válida e estiver no formato dd/MM/yyyy, false caso contrário
     */
    public static boolean validarData(String data){
        try{
            // tenta converter a String para LocalDate usando o padrão definido, se o formato estiver errado, lança DateTimeParseException
            LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return true;
        }
        catch (DateTimeParseException e){
            // Caso a data seja inválida, retorna false
            return false;
        }
    }

    /**
     * Verifica se o campo obrigatório foi preenchido.
     * Campos nulos ou contendo apenas espaços são considerados inválidos.
     * @param campo a String do campo a ser verificada
     * @return true se houver no minimo um caracter, false se for null ou em branco
     */
    public static boolean validarCampoObrigatorio(String campo){
        // trim() remove espaços das bordas
        return campo != null && !campo.trim().isEmpty();
    }
}