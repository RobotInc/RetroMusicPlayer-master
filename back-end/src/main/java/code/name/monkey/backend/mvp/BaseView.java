package code.name.monkey.backend.mvp;

/**
 * Created by hemanths on 09/08/17.
 */

public interface BaseView<T> {
    void loading();

    void showData(T list);

    void showEmptyView();

    void completed();
}
