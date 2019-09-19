package model;

import java.util.*;

public class ModelProperties implements Iterable<Map.Entry<String, Object>> {

    protected Map<String, Object> properties = new HashMap();

    public ModelProperties(){

    }

    public Object put(String name, Object property) {
        Objects.requireNonNull(name);
        return this.properties.put(name, property);
    }

    public Object remove(String name) {
        Objects.requireNonNull(name);
        return this.properties.remove(name);
    }

    public Object get(String name) {
        Objects.requireNonNull(name);
        return this.properties.get(name);
    }

    public Map<String, Object> asMap() {
        return this.properties;
    }

    public Iterator<Map.Entry<String, Object>> iterator() {
        return this.properties.entrySet().iterator();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        List<Map.Entry<String, Object>> entries = new ArrayList(this.properties.entrySet());

        for(int i = 0; i < entries.size(); ++i) {
            Map.Entry<String, Object> entry = (Map.Entry)entries.get(i);
            stringBuilder.append(String.format(" { %s : %s } ", entry.getKey(), String.valueOf(entry.getValue())));
            if (i < entries.size() - 1) {
                stringBuilder.append(",");
            }
        }

        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
