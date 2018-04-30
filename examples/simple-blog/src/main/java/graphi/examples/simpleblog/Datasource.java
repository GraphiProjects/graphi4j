package graphi.examples.simpleblog;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unchecked")
public class Datasource {

  private static final Map<String, Map<Integer, Map<String, Object>>> DATABASE = new HashMap<>();
  private static final Map<String, AtomicInteger> PRIMARY_KEY_SEQUENCE = new HashMap<>();

  public static void init(Map<String, List<Map<String, Object>>> initialData) {
    initialData.forEach((tableName, rowList) -> {
      DATABASE.put(tableName, new HashMap<>());
      rowList.forEach(row -> {
        AtomicInteger idx = PRIMARY_KEY_SEQUENCE.get(tableName);
        if (idx == null) PRIMARY_KEY_SEQUENCE.put(tableName, new AtomicInteger(0));
        else idx.incrementAndGet();
        DATABASE.get(tableName).put((Integer)row.get("id"), row);
      });
    });
  }

  public static <T> T save(String tableName, T entity) {
    Map<String, Object> row = new ObjectMapper().convertValue(entity, Map.class);
    Integer id = (Integer)row.get("id");
    Map<String, Object> existingRow = DATABASE.get(tableName).get(id);
    if (existingRow != null) {
      existingRow.putAll(row);
    } else {
      id = PRIMARY_KEY_SEQUENCE.get(tableName).incrementAndGet();
      row.put("id", id);
      DATABASE.get(tableName).put(id, row);
    }
    return (T)new ObjectMapper().convertValue(row, entity.getClass());
  }

  public static Map<String, Object> delete(String tableName, Integer id) {
    return DATABASE.get(tableName).remove(id);
  }

}
