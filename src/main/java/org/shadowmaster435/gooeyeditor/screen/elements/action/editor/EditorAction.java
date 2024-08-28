package org.shadowmaster435.gooeyeditor.screen.elements.action.editor;

import java.util.ArrayList;

public interface EditorAction<E> {
    EditorActionEntry<?> history = new EditorActionEntry<>();
    void redo();
    void undo();
    void create(E e);

    final class EditorActionEntry<E> {
        public E previous = null;
        public E current = null;
        public E next = null;
    }
}
