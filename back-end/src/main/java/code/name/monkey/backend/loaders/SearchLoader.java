package code.name.monkey.backend.loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import code.name.monkey.backend.R;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by hemanths on 20/08/17.
 */

public class SearchLoader {
    private static final String TAG = "SearchLoader";

    public static Observable<ArrayList<Object>> searchAll(@NonNull Context context, @NonNull String query) {
        ArrayList<Object> results = new ArrayList<>();
        return Observable.create(e -> {
            if (!TextUtils.isEmpty(query)) {
                SongLoader.getSongs(context, query)
                        .subscribe(songs -> {
                            if (!songs.isEmpty()) {
                                results.add(context.getResources().getString(R.string.songs));
                                results.addAll(songs);
                            }
                        });

                ArtistLoader.getArtists(context, query)
                        .subscribe(artists -> {
                            if (!artists.isEmpty()) {
                                results.add(context.getResources().getString(R.string.artists));
                                results.addAll(artists);
                            }
                        });
                AlbumLoader.getAlbums(context, query)
                        .subscribe(albums -> {
                            if (!albums.isEmpty()) {
                                results.add(context.getResources().getString(R.string.albums));
                                results.addAll(albums);
                            }
                        });
            }
            e.onNext(results);
            e.onComplete();
        });
    }
}
