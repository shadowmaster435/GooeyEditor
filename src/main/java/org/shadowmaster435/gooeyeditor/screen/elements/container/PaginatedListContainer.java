package org.shadowmaster435.gooeyeditor.screen.elements.container;

import net.minecraft.client.gui.DrawContext;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiButton;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiElement;
import org.shadowmaster435.gooeyeditor.screen.elements.ParentableWidgetBase;
import org.shadowmaster435.gooeyeditor.util.ArrangeableList;

import java.util.ArrayList;
import java.util.HashMap;

public class PaginatedListContainer extends BaseContainer {

    private final ArrangeableList<ToggleContainer> pages = new ArrangeableList<>();
    private int current_page = 0;
    private int last_page = -1;
    private HashMap<ToggleContainer, Integer> heights = new HashMap<>();
    public int page_count = 1;
    public boolean vertical = true;
    public boolean repositionElements = false;
    private ArrayList<GuiElement> references = new ArrayList<>();

    public PaginatedListContainer(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }

    public PaginatedListContainer(int x, int y, boolean editMode) {
        super(x, y, 0, 0, editMode);
    }


    private int addPage(ToggleContainer page) {
        pages.add(page);
        return pages.size() - 1;
    }

    private int addPage() {
        var container = new ToggleContainer(0,0,0,0, isEditMode());
        pages.add(container);
        container.selectable = false;

        container.name = "page" + (pages.size() - 1);
        addElement(container);
        return pages.size() - 1;
    }

    private int addPageWith(GuiElement... elements) {
        var container = new ToggleContainer(0,0,0,0, isEditMode());
        pages.add(container);
        container.name = "page" + (pages.size() - 1);
        container.selectable = false;
        container.scissor(false);
        container.addElements(elements);
        addElement(container);
        return pages.size() - 1;
    }

    public int getPageIndex(ToggleContainer container) {
        return pages.indexOf(container);
    }

    public ArrayList<? extends GuiElement> getPageElements(int index) {
        return getPage(index).getElements();
    }

    public ToggleContainer getPage(int index) {
        return pages.get(index);
    }

    public void bindButtonToPage(GuiButton button, ToggleContainer container) {
        button.setPressFunction((a) -> setCurrentPage(getPageIndex(container)));
    }

    public boolean shouldAddOnNextPage(GuiElement element, int page) {
        var i = (pages.getOrDefault(page, null) != null) ? pages.get(page) : null;
        var i2 = heights.getOrDefault(i, null);
        var height = (i != null && !heights.isEmpty()) ? (i2 != null) ? i2 : 0 : 0;
        var v = height + element.getHeight() > getHeight();
        var h = height + element.getWidth() > getWidth();
        return (vertical) ? v : h;
    }

    public void bindButtonToPage(GuiButton button, int index) {
        button.setPressFunction((a) -> setCurrentPage(index));
    }

    public void bindButtonToPrevious(GuiButton button) {
        button.setPressFunction((a) -> previous());
    }

    public void bindButtonToNext(GuiButton button) {
        button.setPressFunction((a) -> next());
    }
    public void previous() {
        current_page -= 1;
    }
    public void next() {
        current_page += 1;
    }

    @Override
    public void addElement(GuiElement element) {
        if (element instanceof ToggleContainer e) {
            super.addElement(e);
        } else {
            if (!pages.has(0)) {
                addPage();
            }
            pages.getFirst().addElement(element);
            if (pages.has(current_page)) {
                pages.get(current_page).open();
            }
        }
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        page_count = Math.max(page_count, 1);
        current_page = Math.clamp(current_page, 0, page_count);
        checkPageChange();
        super.preTransform(context, mouseX, mouseY, delta);
    }

    public int getCurrentPage() {
        return current_page;
    }

    @Override
    public void removeElement(GuiElement element) {
        references.remove(element);
        if (element.parent instanceof ToggleContainer t && t.parent == this) {
            t.orphanize();
        }
        super.removeElement(element);
    }

    @Override
    public void removeElement(int index) {
        var e = getElements().getOrDefault(index, null);
        if (e != null && e.parent instanceof ToggleContainer t && t.parent == this) {
            t.orphanize();
        }
        references.remove(index);

        super.removeElement(index);
    }

    public void setCurrentPage(int current_page) {
        this.current_page = current_page;
    }

    private void checkPageChange() {
        if (last_page != current_page) {
            for (int i = 0; i < pages.size(); ++i) {
                var page = pages.get(i);
                if (i == current_page) {
                    page.open();
                } else {
                    page.close();
                }
            }
            last_page = current_page;
        }
    }

    @Override
    public GuiElement getElement(int index) {
        return getElements().get(index);
    }


    @Override
    public ArrangeableList<GuiElement> getElements() {
        ArrangeableList<GuiElement> elements = new ArrangeableList<>();
        for (ToggleContainer container : pages) {
            elements.addAll(container.getElements());
        }
        return elements;
    }

    @Override
    public void arrange() {
        var y_offset = 0;
        var page = 0;
        var size = getElements().size();
        for (int i = 0; i < size; ++i) {

            var element = (ParentableWidgetBase) getElement(i);
            if (vertical) {
                element.setX((repositionElements) ? -element.getWidth() : 0);
                element.setY(y_offset);
                y_offset += getElementSpacing() + element.getHeight();
            } else {
                element.setX(y_offset);
                element.setY((repositionElements) ? -element.getHeight() : 0);
                y_offset += getElementSpacing() + element.getWidth();
            }
            if (pages.isIndexNull(page)) {
                addPage();
            }
            var addToNext = false;
            heights.put(pages.get(page), y_offset);

            if (shouldAddOnNextPage(element, page)) {
                y_offset = 0;
                addToNext = true;
            }



            var reparent_page = pages.get(page);

            if (element.parent != reparent_page || addToNext) {
                element.reparent(reparent_page);
            }

            if (addToNext) {
                page += 1;
            }
        }
      //  heights.clear();

        page_count = page;
    }

    @Override
    public Property[] getProperties() {
        var vertical = new Property("Vertical", "vertical", "vertical", Boolean.class);
        var current_page = new Property("Current Page", "setCurrentPage", "getCurrentPage", Integer.class);

        return mergeProperties(super.getProperties(), vertical, current_page);
    }
}