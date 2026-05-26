import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validador {
    public static boolean validarValor(double valor){

        return valor > 0 ;
    }
    public static boolean validarData(String data){
        try{
            LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return true;
        }
        catch (DateTimeParseException e){
            return false;
        }
    }
    public static boolean validarCampoObrigatorio(String campo){
        return campo != null && !campo.trim().isEmpty();
    }
}
