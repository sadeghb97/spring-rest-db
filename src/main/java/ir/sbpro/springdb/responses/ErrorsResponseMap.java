package ir.sbpro.springdb.responses;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class ErrorsResponseMap extends HashMap<String, String> {
    public ErrorsResponseMap(String field, String message){
        super();
        this.put(field, message);
    }

    public ResponseEntity<Object> getEntityResponse(){
        HashMap<String, Object> h = new HashMap<>();
        h.put("errors", this);
        return new ResponseEntity<Object>(h, HttpStatus.BAD_REQUEST);
    }
}
