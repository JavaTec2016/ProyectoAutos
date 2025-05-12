package ErrorHandlin;

import FormTools.FormHook;

import java.util.LinkedHashMap;

public class ErrorMessageList {
    public LinkedHashMap<String, LinkedHashMap<Integer, String>> lista;

    public ErrorMessageList(){lista = new LinkedHashMap<>();}
    public ErrorMessageList(FormHook form){
        lista = new LinkedHashMap<>();
        String[] campos = form.obtenerCamposNombres();
        for (String campo : campos) {
            lista.put(campo, new LinkedHashMap<Integer, String>());
        }
    }
    public void addMessage(String campo, Integer codigo, String msg){
        if(!lista.containsKey(campo)) lista.put(campo, new LinkedHashMap<Integer, String>());
        lista.get(campo).put(codigo, msg);
        //System.out.println(campo + ">> " +msg + ": " + codigo);
    }

    public String getMessage(String campo, Integer codigo){

        if(!lista.containsKey(campo)) return null;
        if(!lista.get(campo).containsKey(codigo)) return null;
        return lista.get(campo).get(codigo);
    }
    public static String formatearMensajes(String[] partes){
        StringBuilder msg = new StringBuilder();
        int i = 0;
        for (; i < partes.length-1; i++) {
            msg.append(partes[i]).append(", ");
        }
        if(!msg.isEmpty()) msg.substring(0, msg.length()-2);
        if(i > 1) msg.append(" o ").append(partes[i]);

        return msg.toString();
    }
}
