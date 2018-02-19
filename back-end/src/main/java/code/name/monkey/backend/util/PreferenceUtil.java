package code.name.monkey.backend.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import code.name.monkey.backend.helper.SortOrder;


public final class PreferenceUtil {
    public static final String GENRE_SORT_ORDER = "genre_sort_order";
    public static final String GENERAL_THEME = "general_theme";
    public static final String DEFAULT_START_PAGE = "default_start_page";
    public static final String LAST_PAGE = "last_start_page";
    public static final String LAST_MUSIC_CHOOSER = "last_music_chooser";
    public static final String NOW_PLAYING_SCREEN_ID = "now_playing_screen_id";
    public static final String ARTIST_SORT_ORDER = "artist_sort_order";
    public static final String ARTIST_SONG_SORT_ORDER = "artist_song_sort_order";
    public static final String ARTIST_ALBUM_SORT_ORDER = "artist_album_sort_order";
    public static final String ALBUM_SORT_ORDER = "album_sort_order";
    public static final String ALBUM_SONG_SORT_ORDER = "album_song_sort_order";
    public static final String SONG_SORT_ORDER = "song_sort_order";
    public static final String ALBUM_GRID_SIZE = "album_grid_size";
    public static final String ALBUM_GRID_SIZE_LAND = "album_grid_size_land";
    public static final String SONG_GRID_SIZE = "song_grid_size";
    public static final String SONG_GRID_SIZE_LAND = "song_grid_size_land";
    public static final String ARTIST_GRID_SIZE = "artist_grid_size";
    public static final String ARTIST_GRID_SIZE_LAND = "artist_grid_size_land";
    public static final String ALBUM_COLORED_FOOTERS = "album_colored_footers";
    public static final String SONG_COLORED_FOOTERS = "song_colored_footers";
    public static final String ARTIST_COLORED_FOOTERS = "artist_colored_footers";
    public static final String ALBUM_ARTIST_COLORED_FOOTERS = "album_artist_colored_footers";
    public static final String FORCE_SQUARE_ALBUM_COVER = "force_square_album_art";
    public static final String COLORED_NOTIFICATION = "colored_notification";
    public static final String CLASSIC_NOTIFICATION = "classic_notification";
    public static final String COLORED_APP_SHORTCUTS = "colored_app_shortcuts";
    public static final String AUDIO_DUCKING = "audio_ducking";
    public static final String GAPLESS_PLAYBACK = "gapless_playback";
    public static final String LAST_ADDED_CUTOFF = "last_added_interval";
    public static final String ALBUM_ART_ON_LOCKSCREEN = "album_art_on_lockscreen";
    public static final String BLURRED_ALBUM_ART = "blurred_album_art";
    public static final String LAST_SLEEP_TIMER_VALUE = "last_sleep_timer_value";
    public static final String NEXT_SLEEP_TIMER_ELAPSED_REALTIME = "next_sleep_timer_elapsed_real_time";
    public static final String IGNORE_MEDIA_STORE_ARTWORK = "ignore_media_store_artwork";
    public static final String LAST_CHANGELOG_VERSION = "last_changelog_version";
    public static final String INTRO_SHOWN = "intro_shown";
    public static final String AUTO_DOWNLOAD_IMAGES_POLICY = "auto_download_images_policy";
    public static final String START_DIRECTORY = "start_directory";
    public static final String SYNCHRONIZED_LYRICS_SHOW = "synchronized_lyrics_show";
    private static final String ADAPTIVE_COLOR_APP = "adaptive_color_app";
    private static final String LOCK_SCREEN = "lock_screen";
    private static final String USER_NAME = "user_name";
    private static final String USER_NAME_SKIPPED = "user_name_skipped";
    private static final String TOGGLE_FULL_SCREEN = "toggle_full_screen";
    private static final String START_COLOR = "start_color";
    private static final String END_COLOR = "end_color";
    private static final String PROFILE_IMAGE_PATH = "profile_image_path";
    private static final String INITIALIZED_BLACKLIST = "initialized_blacklist";
    private static final String ALBUM_DETAIL_SONG_SORT_ORDER = "album_detail_song_sort_order";
    private static final String ARTIST_DETAIL_SONG_SORT_ORDER = "artist_detail_song_sort_order";
    private static PreferenceUtil sInstance;
    private final SharedPreferences mPreferences;

    private PreferenceUtil(@NonNull final Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferenceUtil getInstance(@NonNull final Context context) {
        if (sInstance == null) {
            sInstance = new PreferenceUtil(context.getApplicationContext());
        }
        return sInstance;
    }

    public final String getGenreSortOrder() {
        return mPreferences.getString(GENRE_SORT_ORDER, SortOrder.GenreSortOrder.GENRE_A_Z);
    }

    public final String getArtistSortOrder() {
        return mPreferences.getString(ARTIST_SORT_ORDER, SortOrder.ArtistSortOrder.ARTIST_A_Z);
    }

    public final String getArtistSongSortOrder() {
        return mPreferences.getString(ARTIST_SONG_SORT_ORDER,
                SortOrder.ArtistSongSortOrder.SONG_A_Z);
    }

    public final String getArtistAlbumSortOrder() {
        return mPreferences.getString(ARTIST_ALBUM_SORT_ORDER,
                SortOrder.ArtistAlbumSortOrder.ALBUM_YEAR);
    }

    public final String getAlbumSortOrder() {
        return mPreferences.getString(ALBUM_SORT_ORDER, SortOrder.AlbumSortOrder.ALBUM_A_Z);
    }

    public final String getSongSortOrder() {
        return mPreferences.getString(SONG_SORT_ORDER, SortOrder.SongSortOrder.SONG_A_Z);
    }

    public long getLastAddedCutoff() {
        final CalendarUtil calendarUtil = new CalendarUtil();
        long interval;

        switch (mPreferences.getString(LAST_ADDED_CUTOFF, "")) {
            case "today":
                interval = calendarUtil.getElapsedToday();
                break;
            case "this_week":
                interval = calendarUtil.getElapsedWeek();
                break;
            case "past_three_months":
                interval = calendarUtil.getElapsedMonths(3);
                break;
            case "this_year":
                interval = calendarUtil.getElapsedYear();
                break;
            case "this_month":
            default:
                interval = calendarUtil.getElapsedMonth();
                break;
        }

        return (System.currentTimeMillis() - interval) / 1000;
    }

    public boolean getAdaptiveColor() {
        return mPreferences.getBoolean(ADAPTIVE_COLOR_APP, false);
    }

    public void setInitializedBlacklist() {
        final Editor editor = mPreferences.edit();
        editor.putBoolean(INITIALIZED_BLACKLIST, true);
        editor.apply();
    }

    public final boolean initializedBlacklist() {
        return mPreferences.getBoolean(INITIALIZED_BLACKLIST, false);
    }

    public String getAlbumDetailSongSortOrder() {
        return mPreferences.getString(ALBUM_DETAIL_SONG_SORT_ORDER, SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST);
    }


    public String getArtistDetailSongSortOrder() {
        return mPreferences.getString(ARTIST_DETAIL_SONG_SORT_ORDER, SortOrder.ArtistSongSortOrder.SONG_A_Z);
    }

}
