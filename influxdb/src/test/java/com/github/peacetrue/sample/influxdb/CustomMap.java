package com.github.peacetrue.samples.influxdb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 非线程安全
 *
 * @author : xiayx
 * @since : 2021-01-04 06:10
 **/
public class CustomMap<K, V> implements Map<K, V> {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CustomEntry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomEntry<?, ?> that = (CustomEntry<?, ?>) o;
            return Objects.equals(key, that.key) &&
                    Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    private final List<CustomEntry<K, V>>[] lists;
    private int size;

    @SuppressWarnings("unchecked")
    public CustomMap(int initCapacity) {
        if (initCapacity < 0) throw new IllegalArgumentException("initCapacity must > 0");
        this.lists = new LinkedList[initCapacity];
        this.size = 0;
    }

    protected int index(Object key) {
        return key == null ? 0 : Math.abs(key.hashCode() % lists.length);
    }

    protected void resize() {

    }

    private Stream<Map.Entry<K, V>> entries() {
        return Stream
                .of(lists)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                ;
    }

    protected Optional<List<CustomEntry<K, V>>> list(Object key) {
        return Optional.ofNullable(lists[index(key)]);
    }

    protected Optional<Map.Entry<K, V>> entry(Object key) {
        return entries()
                .filter(entry -> Objects.equals(entry.getKey(), key))
                .findAny()
                ;
    }

    protected Optional<V> value(Object key) {
        return entry(key)
                .map(Map.Entry::getValue)
                ;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(Object key) {
        return list(key).isPresent();
    }

    public boolean containsValue(Object value) {
        return entries()
                .anyMatch(kvEntry -> Objects.equals(kvEntry.getValue(), value))
                ;
    }

    public V get(Object key) {
        return value(key)
                .orElse(null)
                ;
    }

    public V put(K key, V value) {
        int index = index(key);
        List<CustomEntry<K, V>> list = lists[index];
        //不存在链表
        if (list == null) {
            list = new LinkedList<>();
            lists[index] = list;
        } else {
            Optional<CustomEntry<K, V>> matched = list
                    .stream()
                    .filter(kvEntry -> kvEntry.getKey().equals(key))
                    .findAny();
            //已存在节点
            if (matched.isPresent()) {
                CustomEntry<K, V> entry = matched.get();
                V oldValue = entry.getValue();
                entry.setValue(value);
                return oldValue;
            }
        }
        list.add(new CustomEntry<>(key, value));
        size++;
        return null;
    }

    @Override
    public V remove(Object key) {
        Optional<List<CustomEntry<K, V>>> list = list(key);
        if (list.isPresent()) {
            Iterator<CustomEntry<K, V>> iterator = list.get().iterator();
            while (iterator.hasNext()) {
                CustomEntry<K, V> next = iterator.next();
                if (next.getKey().equals(key)) {
                    V value = next.getValue();
                    iterator.remove();
                    size--;
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        Arrays.fill(lists, null);
        size = 0;
    }

    private class CustomSet<T> implements Set<T> {

        private final Function<Map.Entry<K, V>, T> mapper;

        public CustomSet(Function<Entry<K, V>, T> mapper) {
            this.mapper = Objects.requireNonNull(mapper);
        }

        public int size() {
            return CustomMap.this.size();
        }

        public boolean isEmpty() {
            return CustomMap.this.isEmpty();
        }

        @Override
        public Iterator<T> iterator() {
            return entries()
                    .map(mapper)
                    .iterator()
                    ;
        }

        @Override
        public Object[] toArray() {
            return entries()
                    .map(mapper)
                    .toArray()
                    ;
        }

        @SuppressWarnings("unchecked")
        protected <T> T[] max(T[] a) {
            if (a.length >= size()) return a;
            return (T[]) Array.newInstance(a.getClass(), size);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <S> S[] toArray(S[] a) {
            S[] max = max(a);
            int[] index = {0};
            entries()
                    .map(mapper)
                    .forEach(entry -> max[index[0]++] = (S) entry);
            return max;
        }

        @Override
        public boolean contains(Object o) {
            return entries()
                    .map(mapper)
                    .anyMatch(kvEntry -> kvEntry.equals(o))
                    ;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            if (c.isEmpty()) return true;
            List<?> list = c.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
            return list.isEmpty() || entries().map(mapper).anyMatch(s -> list.remove(s) && list.isEmpty());
        }

        @Override
        public boolean add(T k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }


    @Override
    public Set<K> keySet() {
        return new CustomSet<>(Entry::getKey);
    }

    @Override
    public Collection<V> values() {
        return entries().map(Entry::getValue).collect(Collectors.toList());
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return new CustomSet<>(Function.identity());
    }


}
