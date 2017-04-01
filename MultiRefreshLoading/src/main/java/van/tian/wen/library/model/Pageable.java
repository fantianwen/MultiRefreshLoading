package van.tian.wen.library.model;

import java.util.List;

/**
 * Indicate that this class is pageable
 */
public interface Pageable<T> {

    /**
     * data in page
     *
     * @return
     */
    public List<T> getPageList();

    /**
     * total page number
     * <p>
     * As this can usually be gotten from you server API
     *
     * @return
     */
    public int getTotalPage();

    /**
     * the last page number of all the data
     * <p>
     * As this can usually be gotten from you server API
     *
     * @return
     */
    public boolean isLastPage();
}
