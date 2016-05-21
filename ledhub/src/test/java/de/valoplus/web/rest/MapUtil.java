package de.valoplus.web.rest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tom on 17.02.16.
 */
final public class MapUtil<TK, TS> {
    private final Map<TK, TS> map;

    private MapUtil(TK key, TS value) {
        this.map = new HashMap<>();
        map.put(key, value);
    }

    public MapUtil<TK, TS> put(TK key, TS value) {
        map.put(key, value);
        return this;
    }

    public Map<TK, TS> last(TK key, TS value) {
        map.put(key, value);
        return map;
    }

    public Map<TK, TS> get() {
        return map;
    }

    public static <K, S> MapUtil<K, S> map(K key, S value) {
        return new MapUtil<>(key, value);
    }

    public static MapUtil<Object, Object> oMap(Object key, Object value) {
        return new MapUtil<>(key, value);
    }

}
