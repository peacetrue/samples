package com.github.peacetrue.samples.influxdb;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author : xiayx
 * @since : 2021-01-04 08:14
 **/
@Slf4j
public class CustomMapTest {

    @Test
    void basic() {
        int key = Integer.MAX_VALUE, value = 1;

        CustomMap<Integer, Object> customMap = new CustomMap<>(10);
        Assertions.assertEquals(0, customMap.size());
        Assertions.assertTrue(customMap.isEmpty());
        Assertions.assertFalse(customMap.containsKey(key));
        Assertions.assertFalse(customMap.containsValue(value));
        Assertions.assertFalse(customMap.keySet().contains(key));
        Assertions.assertFalse(customMap.values().contains(value));
        Assertions.assertFalse(customMap.entrySet().contains(new CustomMap.CustomEntry<>(key, value)));
        Assertions.assertNull(customMap.remove(key));

        Object oldValue = customMap.put(key, value);
        Assertions.assertEquals(value, customMap.get(key));
        Assertions.assertNull(oldValue);
        Assertions.assertEquals(1, customMap.size());
        Assertions.assertFalse(customMap.isEmpty());
        Assertions.assertTrue(customMap.containsKey(key));
        Assertions.assertTrue(customMap.containsValue(value));
        Assertions.assertTrue(customMap.keySet().contains(key));
        Assertions.assertTrue(customMap.values().contains(value));
        Assertions.assertTrue(customMap.entrySet().contains(new CustomMap.CustomEntry<>(key, value)));
        oldValue = customMap.put(key, value);
        Assertions.assertEquals(value, oldValue);

        key = Integer.MIN_VALUE;
        value = -1;
        oldValue = customMap.put(key, value);
        Assertions.assertEquals(value, customMap.get(key));
        Assertions.assertNull(oldValue);
        Assertions.assertEquals(2, customMap.size());
        Assertions.assertFalse(customMap.isEmpty());
    }

    @Test
    void put() {
        log.info("固定容量，且不考虑 hash 碰撞 和 并发");
        CustomMap<Integer, Object> simpleMap = new CustomMap<>(10);

        simpleMap.put(Integer.MIN_VALUE, -1);
        Assertions.assertEquals(-1, simpleMap.get(Integer.MIN_VALUE));

        simpleMap.put(0, 0);
        Assertions.assertEquals(0, simpleMap.get(0));

        simpleMap.put(Integer.MAX_VALUE, 1);
        Assertions.assertEquals(1, simpleMap.get(Integer.MAX_VALUE));

        Assertions.assertNull(simpleMap.get(1));

        Object oldValue = simpleMap.put(10, 2);
        Assertions.assertEquals(2, simpleMap.get(10));
        Assertions.assertNull(oldValue);
    }


}
