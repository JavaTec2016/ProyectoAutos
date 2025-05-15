package ErrorHandlin;

import java.util.HashMap;

public class ErrorHandler {
    public static final int OK = 0;
    public static final int LOGIN_NO_USER = 1;
    public static final int OBJECT_NOT_EXISTS = -204;
    public static final int BAD_SQL = -84;
    public static final int SQL_TOO_BIG = -101;
    public static final int SQL_ILLEGAL_SYMBOL = -104;
    public static final int SQL_COLUMN_MISMATCH = -117;
    public static final int SQL_MISSING_PERMISSION = -552;
    public static final int SQL_DUPLICATE_ENTRY = -803;
    public static final int SQL_UNKNOWN_FOREIGN = -530;
    public static final int SQL_CONSTRAINT_FAIL = -801;
    public static final int RESULT_SET_OUT_OF_BOUNDS = -4476;

    static int AUTH_FAILED = -30082;

    static HashMap<Integer, Call> handlers = new HashMap<>();
    static HashMap<Integer, String> errorMsg = new HashMap<>();
    public static void registrarHandler(Integer codigo, Call fn){
        handlers.put(codigo, fn);
    }
    public static void ejecutarHandler(Integer codigo, Object ...args){
        if(!handlers.containsKey(codigo)){
            System.out.println("HANDLER: Codigo desconocido " + codigo);
            return;
        }
        handlers.get(codigo).run(args);
    }

}
