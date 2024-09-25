package org.shadowmaster435.gooeyeditor.screen.elements.action.editor;

import org.jetbrains.annotations.NotNull;
import org.shadowmaster435.gooeyeditor.util.ArrangeableList;

import java.util.*;

public class ActionBuffer<T extends EditorAction> implements List<T> {

    private final ArrangeableList<T> actions = new ArrangeableList<>();
    private int current_index = 0;

    public void undo() {
        if (current_index - 1 >= 0) {
            getPrevious().act(false);

            current_index -= 1;

        }
    }

    @Override
    public String toString() {
        return actions.toString();
    }

    public void redo() {
        if (current_index + 1 < size()) {
            getNext().act(true);

            current_index += 1;

        }
    }

    public boolean cache(T action) {
        current_index += 1;
        actions.add(action);
        if (size() > 0) {
            actions.resize(current_index + 1);
        }
        actions.clearNullElements();
        return false;
    }

    public T getPrevious() {
        return (isEmpty() && current_index - 1 < 0) ? null : get(current_index - 1);
    }

    public T getCurrent() {
        return (current_index < (actions.size() - 1) && !isEmpty()) ? get(current_index) : null;
    }

    public T getNext() {
        return (current_index + 1 < (actions.size()) && !isEmpty()) ? get(current_index + 1) : null;
    }

    @Override
    public int size() {
        return actions.size();
    }

    @Override
    public boolean isEmpty() {
        return actions.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return actions.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return actions.iterator();
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        return actions.toArray();
    }

    @NotNull
    @Override
    public <T1> T1 @NotNull [] toArray(@NotNull T1[] a) {
        return actions.toArray(a);
    }

    @Override
    public boolean add(T t) {
        return cache(t);
    }

    @Override
    public boolean remove(Object o) {
        return actions.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return actions.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        c.forEach(this::cache);
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        c.forEach(this::cache);

        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return actions.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return actions.retainAll(c);
    }

    @Override
    public void clear() {
        actions.clear();
    }

    @Override
    public T get(int index) {
        return actions.get(index);
    }

    @Override
    public T set(int index, T element) {
        return actions.set(index, element);
    }

    @Override
    public void add(int index, T element) {

        cache(element);
    }

    @Override
    public T remove(int index) {
        return actions.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return actions.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return actions.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return actions.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return actions.listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return actions.subList(fromIndex, toIndex);
    }
}
