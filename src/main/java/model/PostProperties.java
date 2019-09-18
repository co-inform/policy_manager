package model;

import java.util.*;

/**
 * A post has properties which are scores or metrics from different modules.
 * The post object will be handed into the @link{RuleEngine rule engine}.
 */
public class PostProperties implements Iterable<Map.Entry<String, Object>>{

        private Map<String, Object> properties = new HashMap();

        public PostProperties() {
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
