package code.name.monkey.backend.mvp.presenter;

import android.support.annotation.NonNull;

import code.name.monkey.backend.model.Artist;
import code.name.monkey.backend.mvp.Presenter;
import code.name.monkey.backend.mvp.contract.ArtistContract;
import code.name.monkey.backend.providers.interfaces.Repository;

import java.util.ArrayList;


/**
 * Created by hemanths on 16/08/17.
 */

public class ArtistPresenter extends Presenter implements ArtistContract.Presenter {
    @NonNull
    private ArtistContract.ArtistView mView;

    public ArtistPresenter(@NonNull Repository repository,
                           @NonNull ArtistContract.ArtistView view) {
        super(repository);
        mView = view;
    }

    @Override
    public void subscribe() {
        loadArtists();
    }

    @Override
    public void unsubscribe() {
        disposable.clear();
    }

    private void showList(@NonNull ArrayList<Artist> songs) {
        if (songs.isEmpty()) {
            mView.showEmptyView();
        } else {
            mView.showData(songs);
        }
    }

    @Override
    public void loadArtists() {
        disposable.add(repository.getAllArtists()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(disposable1 -> mView.loading())
                .subscribe(this::showList,
                        throwable -> mView.showEmptyView(),
                        () -> mView.completed()));
    }
}
