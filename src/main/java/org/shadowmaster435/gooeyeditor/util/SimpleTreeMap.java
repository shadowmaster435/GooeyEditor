package org.shadowmaster435.gooeyeditor.util;

import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

@SuppressWarnings({"unchecked", "rawtypes", "DataFlowIssue", "unused"})
public final class SimpleTreeMap<K, V> {


    private final HashMap<K, SimpleTreeEntry<HashMap, K, V>> tree = new HashMap<>();

    /**
     * Gets a value from the tree.
     * <p>
     * If any branches in the sequence do not exist or the value isn't present this method will return null.
     *
     * @param first_key Top level key.
     * @param key_sequence Keys to navigate down the tree from first key leave empty if not needed.
     */
    @SafeVarargs
    public final V get(K first_key, K... key_sequence) {
        var current = tree.get(first_key);
        for (int i = 0; i < key_sequence.length - 1; ++i) {
            if (current.get_sub_map() instanceof HashMap<?,?> map) {
                current = (SimpleTreeEntry<HashMap, K, V>) map.get(key_sequence[i]);

            } else {
                return null;
            }

        }
        return current.get_value();
    }



    /**
     * Iterates over all entries within the entire tree. <P>
     * Consumer Parameters are as follows <P>
     * {@code a} Current branch's depth. <P>
     * {@code b} Current entry's key. <P>
     * {@code c} Current entry's value.
     */
    public void forEach(TriConsumer<Integer, K, V> consumer) {
        for (K key : tree.keySet()) {
            var val = tree.get(key);
            if (val.isSubTree()) {
                val.forEach(consumer, key, 1);
            } else {
                consumer.accept(0, val.key, val.value);
            }
        }
    }
    public final void clear() {
        tree.clear();
    }

    /**
     * Adds a value to the tree. <p>
     * If any branches in the sequence do not exist they will be created.
     *
     * @param first_key Top level key.
     * @param key_sequence Keys to navigate down the tree from first key leave empty if not needed.
     */
    @SafeVarargs
    public final void put(V value, K first_key, K... key_sequence) {
        if (key_sequence.length == 0) {
            tree.put(first_key, new SimpleTreeEntry<>(null, first_key, value));
        } else {
            var current = tree.get(first_key).get_sub_map();
            for (int i = 0; i < key_sequence.length - 1; ++i) {
                assert current != null;
                if (current.containsKey(key_sequence[i])) {
                    current = (HashMap) current.get(key_sequence[i]);
                } else {
                    HashMap<K, SimpleTreeEntry<HashMap, K, V>> new_map = new HashMap<>();
                    current.put(key_sequence[i], new_map);
                    current = new_map;
                }
            }
            current.put(key_sequence[key_sequence.length - 1], value);
        }
    }


    public record SimpleTreeEntry<M extends HashMap, K,V>(@Nullable M sub_map, K key, @Nullable V value) {

        public boolean isSubTree() {
            return sub_map != null;
        }

        public void forEach(TriConsumer<Integer, K, V> consumer, K key, int depth) {
            if (isSubTree()) {
                for (Object subkey : sub_map.keySet()) {
                    ((SimpleTreeEntry<HashMap, K, V>) sub_map.get(subkey)).forEach(consumer, (K) subkey, depth + 1);
                }
            } else {
                consumer.accept(depth, key, value);
            }
        }

        public V get_value() {
            return value;
        }

        public M get_sub_map() {
            return sub_map;
        }

    }



}
