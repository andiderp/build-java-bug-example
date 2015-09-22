package monto.service.error;

import monto.service.completion.Completion;
import monto.service.message.ParseException;
import monto.service.message.ProductMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Errors {

    public static List<Error> decode(ProductMessage message) throws ParseException {
        return decode(message.getContents().getReader());
    }

    public static List<Error> decode(Reader reader) throws ParseException {
        try {
            JSONArray array = (JSONArray) JSONValue.parse(reader);
            List<Error> errors = new ArrayList<>();
            for (Object obj : array) {
                JSONObject encoding = (JSONObject) obj;
                errors.add(new Error(
                        (int) encoding.get("offset"),
                        (int) encoding.get("length"),
                        (String) encoding.get("level"),
                        (String) encoding.get("category"),
                        (String) encoding.get("description")));
            }
            return errors;
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static JSONArray encode(Stream<Error> errors) {
        Iterator<Error> iter = errors.iterator();
        JSONArray array = new JSONArray();
        while (iter.hasNext()) {
            array.add(encode(iter.next()));
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject encode(Error error) {
        JSONObject object = new JSONObject();
        object.put("offset", error.getOffset());
        object.put("length", error.getLength());
        object.put("level", error.getLevel());
        object.put("category", error.getCategory());
        object.put("description", error.getDescription());
        return object;
    }
}
