package code.name.monkey.backend.loaders;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import code.name.monkey.backend.model.Album;
import code.name.monkey.backend.model.Artist;
import code.name.monkey.backend.model.Song;
import code.name.monkey.backend.util.PreferenceUtil;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by hemanths on 16/08/17.
 */

public class LastAddedSongsLoader {

    @NonNull
    public static Observable<ArrayList<Song>> getLastAddedSongs(@NonNull Context context) {
        return SongLoader.getSongs(makeLastAddedCursor(context));
    }

    public static Cursor makeLastAddedCursor(@NonNull final Context context) {
        long cutoff = PreferenceUtil.getInstance(context).getLastAddedCutoff();

        return SongLoader.makeSongCursor(
                context,
                MediaStore.Audio.Media.DATE_ADDED + ">?",
                new String[]{String.valueOf(cutoff)},
                MediaStore.Audio.Media.DATE_ADDED + " DESC");
    }

    @NonNull
    public static Observable<ArrayList<Album>> getLastAddedAlbums(@NonNull Context context) {
        return AlbumLoader.splitIntoAlbums(getLastAddedSongs(context));
    }

    @NonNull
    public static Observable<ArrayList<Artist>> getLastAddedArtists(@NonNull Context context) {
        return ArtistLoader.splitIntoArtists(getLastAddedAlbums(context));
    }
}
