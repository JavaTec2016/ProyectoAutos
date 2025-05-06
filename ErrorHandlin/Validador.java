package ErrorHandlin;

import java.util.regex.Pattern;

public class Validador {
    public static final int NULL = 1;
    public static final int TOO_SHORT = 2;
    public static final int TOO_LONG = 3;
    public static final int REGEX_FAIL = 4;

    public static int probarString(String ent, String regex, int longitud, boolean noNulo, int umbral){
        if(ent == null || ent.isEmpty() && noNulo) return NULL;
        if(ent.length() > longitud && longitud > -1) return TOO_LONG;
        if(ent.length() < umbral) return TOO_SHORT;
        if(regex != null && !regex.isEmpty()){
            if(!Pattern.compile(regex).matcher(ent).matches()) return REGEX_FAIL;
        }
        return 0;
    }
    public static int probarString(String ent, String regex, int longitud, boolean noNulo, String tipoSQL){
        if(ent == null || ent.isEmpty()) return NULL;
        if(ent.length() > longitud && longitud > -1) return TOO_LONG;
        if(regex != null && !regex.isEmpty()){
            if(!Pattern.compile(regex).matcher(ent).matches()) return REGEX_FAIL;
        }
        return 0;
    }
}
