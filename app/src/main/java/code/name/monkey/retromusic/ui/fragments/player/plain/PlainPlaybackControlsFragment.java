package code.name.monkey.retromusic.ui.fragments.player.plain;

import android.animation.ObjectAnimator;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;
import code.name.monkey.appthemehelper.util.TintHelper;
import code.name.monkey.backend.misc.SimpleOnSeekbarChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.ui.fragments.base.AbsPlayerControlsFragment;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.backend.views.PlayPauseDrawable;

/**
 * @author Hemanth S (h4h13).
 */

public class PlainPlaybackControlsFragment extends AbsPlayerControlsFragment {
    @BindView(R.id.player_play_pause_fab)
    ImageButton playPauseFab;
    @BindView(R.id.player_prev_button)
    ImageButton prevButton;
    @BindView(R.id.player_next_button)
    ImageButton nextButton;
    @BindView(R.id.player_repeat_button)
    ImageButton repeatButton;
    @BindView(R.id.player_shuffle_button)
    ImageButton shuffleButton;
    @BindView(R.id.player_progress_slider)
    SeekBar progressSlider;
    @BindView(R.id.player_song_total_time)
    TextView songTotalTime;
    @BindView(R.id.player_song_current_progress)
    TextView songCurrentProgress;
    @BindView(R.id.volume_fragment_container)
    View mVolumeContainer;
    private Unbinder mUnbinder;
    private PlayPauseDrawable mPlayerFabPlayPauseDrawable;
    private int mLastPlaybackControlsColor;
    private int mLastDisabledPlaybackControlsColor;
    private MusicProgressViewUpdateHelper mProgressViewUpdateHelper;

    @Override
    public void onPlayStateChanged() {
        updatePlayPauseDrawableState(true);
    }

    @Override
    public void onRepeatModeChanged() {
        updateRepeatState();
    }

    @Override
    public void onShuffleModeChanged() {
        updateShuffleState();
    }

    @Override
    public void onServiceConnected() {
        updatePlayPauseDrawableState(false);
        updateRepeatState();
        updateShuffleState();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressViewUpdateHelper = new MusicProgressViewUpdateHelper(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plain_controls_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mProgressViewUpdateHelper.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mProgressViewUpdateHelper.stop();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpMusicControllers();
        if (PreferenceUtil.getInstance(getContext()).getVolumeToggle()) {
            mVolumeContainer.setVisibility(View.VISIBLE);
        } else {
            mVolumeContainer.setVisibility(View.GONE);
        }
    }

    private void setUpMusicControllers() {
        setUpPlayPauseFab();
        setUpPrevNext();
        setUpRepeatButton();
        setUpShuffleButton();
        setUpProgressSlider();
    }

    private void setUpPrevNext() {
        updatePrevNextColor();
        nextButton.setOnClickListener(v -> MusicPlayerRemote.playNextSong());
        prevButton.setOnClickListener(v -> MusicPlayerRemote.back());
    }

    private void updatePrevNextColor() {
        nextButton.setColorFilter(mLastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
        prevButton.setColorFilter(mLastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
    }

    private void setUpShuffleButton() {
        shuffleButton.setOnClickListener(v -> MusicPlayerRemote.toggleShuffleMode());
    }

    @Override
    protected void updateShuffleState() {
        switch (MusicPlayerRemote.getShuffleMode()) {
            case MusicService.SHUFFLE_MODE_SHUFFLE:
                shuffleButton.setColorFilter(mLastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            default:
                shuffleButton.setColorFilter(mLastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }

    private void setUpRepeatButton() {
        repeatButton.setOnClickListener(v -> MusicPlayerRemote.cycleRepeatMode());
    }

    @Override
    protected void updateRepeatState() {
        switch (MusicPlayerRemote.getRepeatMode()) {
            case MusicService.REPEAT_MODE_NONE:
                repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                repeatButton.setColorFilter(mLastDisabledPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_ALL:
                repeatButton.setImageResource(R.drawable.ic_repeat_white_24dp);
                repeatButton.setColorFilter(mLastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
            case MusicService.REPEAT_MODE_THIS:
                repeatButton.setImageResource(R.drawable.ic_repeat_one_white_24dp);
                repeatButton.setColorFilter(mLastPlaybackControlsColor, PorterDuff.Mode.SRC_IN);
                break;
        }
    }


    @Override
    protected void show() {
        playPauseFab.animate()
                .scaleX(1f)
                .scaleY(1f)
                .rotation(360f)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    @Override
    protected void hide() {
        if (playPauseFab != null) {
            playPauseFab.setScaleX(0f);
            playPauseFab.setScaleY(0f);
            playPauseFab.setRotation(0f);
        }
    }

    @Override
    protected void setUpProgressSlider() {
        progressSlider.setOnSeekBarChangeListener(new SimpleOnSeekbarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    MusicPlayerRemote.seekTo(progress);
                    onUpdateProgressViews(MusicPlayerRemote.getSongProgressMillis(), MusicPlayerRemote.getSongDurationMillis());
                }
            }
        });
    }

    public void showBouceAnimation() {
        playPauseFab.clearAnimation();

        playPauseFab.setScaleX(0.9f);
        playPauseFab.setScaleY(0.9f);
        playPauseFab.setVisibility(View.VISIBLE);
        playPauseFab.setPivotX(playPauseFab.getWidth() / 2);
        playPauseFab.setPivotY(playPauseFab.getHeight() / 2);

        playPauseFab.animate()
                .setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .scaleX(1.1f)
                .scaleY(1.1f)
                .withEndAction(() -> playPauseFab.animate()
                        .setDuration(200)
                        .setInterpolator(new AccelerateInterpolator())
                        .scaleX(1f)
                        .scaleY(1f)
                        .alpha(1f)
                        .start())
                .start();
    }

    @OnClick(R.id.player_play_pause_fab)
    void showAnimation() {
        if (MusicPlayerRemote.isPlaying()) {
            MusicPlayerRemote.pauseSong();
        } else {
            MusicPlayerRemote.resumePlaying();
        }
        showBouceAnimation();
    }

    @Override
    public void onUpdateProgressViews(int progress, int total) {
        progressSlider.setMax(total);

        ObjectAnimator animator = ObjectAnimator.ofInt(progressSlider, "progress", progress);
        animator.setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();

        songTotalTime.setText(MusicUtil.getReadableDurationString(total));
        songCurrentProgress.setText(MusicUtil.getReadableDurationString(progress));
    }

    @Override
    public void setDark(int dark) {
        int color = ATHUtil.resolveColor(getActivity(), android.R.attr.colorBackground);
        if (ColorUtil.isColorLight(color)) {
            mLastPlaybackControlsColor = MaterialValueHelper
                    .getSecondaryTextColor(getActivity(), true);
            mLastDisabledPlaybackControlsColor = MaterialValueHelper
                    .getSecondaryDisabledTextColor(getActivity(), true);
        } else {
            mLastPlaybackControlsColor = MaterialValueHelper
                    .getPrimaryTextColor(getActivity(), false);
            mLastDisabledPlaybackControlsColor = MaterialValueHelper
                    .getPrimaryDisabledTextColor(getActivity(), false);
        }

        if (PreferenceUtil.getInstance(getContext()).getAdaptiveColor()) {
            TintHelper.setTintAuto(playPauseFab, dark, true);
            setProgressBarColor(progressSlider, dark);
        }
        updateRepeatState();
        updateShuffleState();
        updatePrevNextColor();
    }

    public void setProgressBarColor(SeekBar progressBar, int newColor) {
        LayerDrawable ld = (LayerDrawable) progressBar.getProgressDrawable();
        ClipDrawable clipDrawable = (ClipDrawable) ld.findDrawableByLayerId(android.R.id.progress);
        clipDrawable.setColorFilter(newColor, PorterDuff.Mode.SRC_IN);
    }

    private void setUpPlayPauseFab() {
        mPlayerFabPlayPauseDrawable = new PlayPauseDrawable(getActivity());

        playPauseFab.setImageDrawable(mPlayerFabPlayPauseDrawable); // Note: set the drawable AFTER TintHelper.setTintAuto() was called
        //playPauseFab.setColorFilter(MaterialValueHelper.getPrimaryTextColor(getContext(), ColorUtil.isColorLight(fabColor)), PorterDuff.Mode.SRC_IN);
        //playPauseFab.setOnClickListener(new PlayPauseButtonOnClickHandler());
        playPauseFab.post(() -> {
            if (playPauseFab != null) {
                playPauseFab.setPivotX(playPauseFab.getWidth() / 2);
                playPauseFab.setPivotY(playPauseFab.getHeight() / 2);
            }
        });
    }

    protected void updatePlayPauseDrawableState(boolean animate) {
        if (MusicPlayerRemote.isPlaying()) {
            mPlayerFabPlayPauseDrawable.setPause(animate);
        } else {
            mPlayerFabPlayPauseDrawable.setPlay(animate);
        }
    }
}
