package ErrorHandlin;

import java.util.HashMap;

public class ErrorHandler {
    static int OBJECT_NOT_EXISTS = -204;
    static int BAD_SQL = -84;
    static int SQL_TOO_BIG = -101;
    static int SQL_ILLEGAL_SYMBOL = -104;
    static int SQL_MISSING_PERMISSION = -552;
    static int SQL_DUPLICATE_ENTRY = -601;

    static HashMap<Integer, Call> handlers = new HashMap<>();

    static void registrarHandler(Integer codigo, Call fn){
        handlers.put(codigo, fn);
    }
    static void ejecutarHandler(Integer codigo, Object ...args){
        handlers.get(codigo).run(args);
    }
}
