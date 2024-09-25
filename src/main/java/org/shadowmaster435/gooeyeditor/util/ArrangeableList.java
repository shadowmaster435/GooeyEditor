package org.shadowmaster435.gooeyeditor.util;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;

public class ArrangeableList<E> extends ArrayList<E> {


    /**
     * Resizes a list. Discards any entries with indexes out of bounds of the resized list. New indexes will be whatever is provided by the supplied function.
     * @param size New size of the list.
     */
    public void resize(int size, Function<Integer, E> supplier) {
        var previous = size();
        var newSize = Math.max(0, size);
        ArrayList<E> toRemove = new ArrayList<>();
        if (newSize < previous) {
            for (int i = 0; i < previous; ++i) {
                if (i >= newSize) {
                    toRemove.add(get(i));
                }
            }
        }
        if (newSize > previous) {
            for (int i = 0; i < newSize; ++i) {
                if (i >= previous) {
                    add(supplier.apply(i));
                }
            }
        }
        toRemove.forEach(this::remove);
    }

    /**
     * Resizes a list. Discards any entries with indexes out of bounds of the resized list. New indexes will be null.
     * @param size New size of the list.
     */
    public void resize(int size) {
        var previous = size();
        var newSize = Math.max(0, size);
        ArrayList<E> toRemove = new ArrayList<>();
        if (newSize < previous) {
            for (int i = 0; i < previous; ++i) {
                if (i >= newSize) {
                    toRemove.add(get(i));
                }
            }
        }
        if (newSize > previous) {
            for (int i = 0; i < newSize; ++i) {
                if (i >= previous) {
                    add(null);
                }
            }
        }
        toRemove.forEach(this::remove);
    }

    public void swap(int index_a, int index_b) {
        var a = get(index_a);
        var b = get(index_b);
        set(index_b, a);
        set(index_a, b);
    }

    public void swap(E a, E b) {
        var index_a = indexOf(a);
        var index_b = indexOf(b);
        set(index_b, a);
        set(index_a, b);
    }

    public void swap(E a, int index_b) {
        var index_a = indexOf(a);
        var b = get(index_b);
        set(index_b, a);
        set(index_a, b);
    }


    public void swap(int index_a, E b) {
        var index_b = indexOf(b);
        var a = get(index_a);
        set(index_b, a);
        set(index_a, b);
    }

    /**
     * Checks if an index is not present.
     * @return false if index doesn't exist.
     */
    public boolean has(int index) {
        return !isEmpty() && index >= 0 && index < size();
    }

    /**
     * Removes duplicates of {@code value} leaving the only first match found in the list.
     */
    public void removeDuplicates(E value) {
        ArrayList<Integer> toRemove = new ArrayList<>();
        var found_first = false;
        for (int i = 0; i < size(); ++i) {
            E e = get(i);
            if (e.equals(value)) {
                if (found_first) {
                    toRemove.add(i);
                } else {
                    found_first = true;
                }
            }
        }
        for (int i : toRemove) {
            remove(i);
        }
    }

    /**
     * Checks if the entry at the provided index returns null.
     * @return true {@link ArrayList#get(int)} returns null or if index doesn't exist.
     */
    public boolean isIndexNull(int index) {
        return !has(index) || has(index) && (get(index)) == null;
    }

    /**
     * Removes all null values.
     */
    public void clearNullElements() {
        removeIf(Objects::isNull);
    }

    public E getOrDefault(int index, E fallback) {
        return (has(index)) ? get(index) : fallback;
    }

    /**
     * Shifts elements by the given amount. wraps entries front to back.
     * @param amount index amount to shift by.
     */
    public void shift(int amount) {
        ArrayList<E> shifted = new ArrayList<>(size());
        for (E e : this) {
            var index = (indexOf(e) + amount) % size();
            shifted.set(index, e);
        }
        clear();
        addAll(shifted);
    }



}
