package org.shadowmaster435.gooeyeditor.screen.elements.container;

import net.minecraft.client.gui.DrawContext;
import org.shadowmaster435.gooeyeditor.screen.elements.GuiButton;
import org.shadowmaster435.gooeyeditor.screen.elements.SealedGuiElement;
import org.shadowmaster435.gooeyeditor.util.ArrangeableList;

import java.util.ArrayList;

/**
 * A paged container.
 */
public class PaginatedContainer extends BaseContainer {

    private final ArrangeableList<ToggleContainer> pages = new ArrangeableList<>();
    private int current_page = 0;
    private int last_page = 0;

    public int page_count = 1;
    private int lastPageCount = 0;

    public PaginatedContainer(int x, int y, int w, int h, boolean editMode) {
        super(x, y, w, h, editMode);
    }

    public PaginatedContainer(int x, int y, boolean editMode) {
        super(x, y, 0, 0, editMode);
    }

    /**
     * Add the provided page.
     * @return Index of added page.
     */
    public int addPage(ToggleContainer page) {
        pages.add(page);
        return pages.size() - 1;
    }

    /**
     * Adds a page.
     * @return Index of created page.
     */
    public int addPage() {
        var container = new ToggleContainer(0,0,0,0, isEditMode());
        pages.add(container);
        container.name = "page" + (pages.size() - 1);
        container.selectable = false;

        addElement(container);
        return pages.size() - 1;
    }
    /**
     * Adds a page. While adding provided elements to the page.
     * @return Index of created page.
     */
    public int addPageWith(SealedGuiElement... elements) {
        var container = new ToggleContainer(0,0,0,0, isEditMode());
        pages.add(container);
        container.selectable = false;

        container.addElements(elements);
        container.name = "page" + (pages.size() - 1);

        addElement(container);
        return pages.size() - 1;
    }

    public int getPageIndex(ToggleContainer container) {
        return pages.indexOf(container);
    }


    public ArrayList<? extends SealedGuiElement> getPageElements(int index) {
        return getPage(index).getElements();
    }

    public ToggleContainer getPage(int index) {
        return pages.get(index);
    }

    public void bindButtonToPage(GuiButton button, ToggleContainer container) {
        button.setPressFunction((a) -> setCurrentPage(getPageIndex(container)));
    }

    /**
     * @param button Button to bind
     * @param index Page to change to when button is pressed.
     */
    public void bindButtonToPage(GuiButton button, int index) {
        button.setPressFunction((a) -> setCurrentPage(index));
    }
    /**
     * @param button Button to bind
     */
    public void bindButtonToPrevious(GuiButton button) {
        button.setPressFunction((a) -> previous());
    }
    /**
     * @param button Button to bind
     */
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
    public void addElement(SealedGuiElement element) {
        super.addElement(element);
    }

    @Override
    public void preTransform(DrawContext context, int mouseX, int mouseY, float delta) {
        page_count = Math.max(page_count, 1);
        checkPageChange();
        checkPageCountChange();
        super.preTransform(context, mouseX, mouseY, delta);
    }

    public int getCurrentPage() {
        return current_page;
    }

    public void setCurrentPage(int current_page) {
        this.current_page = current_page;
    }

    private void checkPageCountChange() {
        if (page_count < lastPageCount) {
            pages.resize(page_count);
            lastPageCount = page_count;
        }
        if (page_count > lastPageCount) {
            pages.resize(page_count, (i) -> {
                var page = new ToggleContainer(0,0,0,0, isEditMode());
                addPage(page);
                page.name = "page" + i;
                addElement(page);
                return page;
            });
            lastPageCount = page_count;
        }
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
    public void arrange() {
    }

    @Override
    public Property[] getProperties() {
        var current_page = new Property("Current Page", "setCurrentPage", "getCurrentPage", Integer.class);
        var page_count = new Property("Page Count", "page_count", "page_count", Integer.class);

        return mergeProperties(super.getProperties(), current_page, page_count);
    }
}
