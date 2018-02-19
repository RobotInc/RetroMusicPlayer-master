package code.name.monkey.backend.mvp;

/**
 * Created by hemanths on 09/08/17.
 */

public interface BasePresenter<T> {

    void subscribe();

    void unsubscribe();
}
