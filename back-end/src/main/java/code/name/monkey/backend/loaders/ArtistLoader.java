package code.name.monkey.backend.loaders;

import android.content.Context;
import android.provider.MediaStore.Audio.AudioColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import code.name.monkey.backend.model.Album;
import code.name.monkey.backend.model.Artist;
import code.name.monkey.backend.util.PreferenceUtil;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by hemanths on 14/08/17.
 */

public class ArtistLoader {
    public static String getSongLoaderSortOrder(Context context) {
        return PreferenceUtil.getInstance(context).getArtistSortOrder() + ", " +
                PreferenceUtil.getInstance(context).getArtistAlbumSortOrder() + ", " +
                PreferenceUtil.getInstance(context).getAlbumDetailSongSortOrder() + ", " +
                PreferenceUtil.getInstance(context).getArtistDetailSongSortOrder();
    }

    @NonNull
    public static Observable<Artist> getArtist(@NonNull final Context context, int artistId) {
        return Observable.create(e -> SongLoader.getSongs(SongLoader.makeSongCursor(
                context,
                AudioColumns.ARTIST_ID + "=?",
                new String[]{String.valueOf(artistId)},
                getSongLoaderSortOrder(context)))
                .subscribe(songs -> {
                    Artist artist = new Artist(AlbumLoader.splitIntoAlbums(songs));
                    e.onNext(artist);
                    e.onComplete();
                }));
    }

    @NonNull
    public static Observable<ArrayList<Artist>> getAllArtists(@NonNull final Context context) {
        return Observable.create(e -> SongLoader
                .getSongs(SongLoader.makeSongCursor(
                        context,
                        null,
                        null,
                        getSongLoaderSortOrder(context))
                ).subscribe(songs -> {
                    e.onNext(splitIntoArtists(AlbumLoader.splitIntoAlbums(songs)));
                    e.onComplete();
                }));

    }

    @NonNull
    public static Observable<ArrayList<Artist>> getArtists(@NonNull final Context context, String query) {
        return Observable.create(e -> SongLoader.getSongs(SongLoader.makeSongCursor(
                context,
                AudioColumns.ARTIST + " LIKE ?",
                new String[]{"%" + query + "%"},
                getSongLoaderSortOrder(context))
        ).subscribe(songs -> {
            e.onNext(splitIntoArtists(AlbumLoader.splitIntoAlbums(songs)));
            e.onComplete();
        }));
    }

    @NonNull
    public static ArrayList<Artist> splitIntoArtists(@Nullable final ArrayList<Album> albums) {
        ArrayList<Artist> artists = new ArrayList<>();
        if (albums != null) {
            for (Album album : albums) {
                getOrCreateArtist(artists, album.getArtistId()).albums.add(album);
            }
        }
        return artists;
    }

    private static Artist getOrCreateArtist(ArrayList<Artist> artists, int artistId) {
        for (Artist artist : artists) {
            if (!artist.albums.isEmpty() && !artist.albums.get(0).songs.isEmpty() && artist.albums.get(0).songs.get(0).artistId == artistId) {
                return artist;
            }
        }
        Artist album = new Artist();
        artists.add(album);
        return album;
    }

    public static Observable<ArrayList<Artist>> splitIntoArtists(Observable<ArrayList<Album>> albums) {
        return Observable.create(e -> {
            ArrayList<Artist> artists = new ArrayList<>();
            albums.subscribe(localAlbums -> {
                if (localAlbums != null) {
                    for (Album album : localAlbums) {
                        getOrCreateArtist(artists, album.getArtistId()).albums.add(album);
                    }
                }
                e.onNext(artists);
                e.onComplete();
            });
        });
    }

   /* public static Observable<ArrayList<Artist>> getAllArtists(Context context) {
        return getArtistsForCursor(makeArtistCursor(context, null, null));
    }

    public static Observable<Artist> getArtist(Context context, long id) {
        return getArtist(makeArtistCursor(context, "_id=?", new String[]{String.valueOf(id)}));
    }

    public static Observable<ArrayList<Artist>> getArtists(Context context, String paramString) {
        return getArtistsForCursor(makeArtistCursor(context, "artist LIKE ?", new String[]{"%" + paramString + "%"}));
    }

    private static Cursor makeArtistCursor(Context context, String selection, String[] paramArrayOfString) {
        final String artistSortOrder = PreferenceUtil.getInstance(context).getArtistSortOrder();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, new String[]{"_id", "artist", "number_of_albums", "number_of_tracks"}, selection, paramArrayOfString, artistSortOrder);
        return cursor;
    }*/
}
