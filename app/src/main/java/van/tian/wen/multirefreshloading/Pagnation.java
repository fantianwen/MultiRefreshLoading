package van.tian.wen.multirefreshloading;

import java.util.List;

import van.tian.wen.library.model.Pageable;

/**
 * Created by RadAsm on 17/4/1.
 */
public class Pagnation<T> implements Pageable<T> {

    /**
     * last : false
     * totalPages : 8
     * totalElements : 30
     * first : true
     * numberOfElements : 4
     * size : 4
     * number : 0
     */

    private boolean last;
    private int totalPages;
    private int totalElements;
    private boolean first;
    private int numberOfElements;
    private int size;
    private int number;

    private List<T> content;

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public List<T> getPageList() {
        return content;
    }

    @Override
    public int getTotalPage() {
        return totalPages;
    }

    @Override
    public boolean isLastPage() {
        return isLast();
    }
}
