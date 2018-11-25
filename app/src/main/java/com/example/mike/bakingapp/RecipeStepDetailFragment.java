package com.example.mike.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mike.bakingapp.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single RecipeStep detail screen.
 * This fragment is either contained in a {@link RecipeInfoActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {
    public static final String STEP = "step";
    private static final String POSITION = "position";
    private static final String PLAY_WHEN_RDY = "play_when_rdy";

    @BindView(R.id.instructions_container)
    NestedScrollView mInstructionsContainer;
    @BindView(R.id.exo_player_view)
    SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.step_thumbnail_image)
    ImageView mThumbnailIv;
    @BindView(R.id.instruction_text)
    TextView mInstructionTv;

    private SimpleExoPlayer mExoPlayer;
    private Step mStep;

    private long mCurrPosition = 0;
    private boolean mPlayWhenRdy = true;
    public RecipeStepDetailFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null&&getArguments().containsKey(STEP)){
            mStep = getArguments().getParcelable(STEP);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.recipe_step_detail,container,false);
        if(savedInstanceState!=null&&savedInstanceState.containsKey(POSITION)){
            mCurrPosition=savedInstanceState.getInt(POSITION);
            mPlayWhenRdy=savedInstanceState.getBoolean(PLAY_WHEN_RDY);
        }
        ButterKnife.bind(this,rootView);
        mInstructionTv.setText(mStep.getDescription());
        if(!mStep.getThumbnailURL().isEmpty()){
            Picasso.with(getContext())
                    .load(mStep.getThumbnailURL())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mThumbnailIv);
            mThumbnailIv.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(mStep.getVideoURL())) initializePlayer(Uri.parse(mStep.getVideoURL()));
        else mInstructionsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(POSITION,mCurrPosition);
        outState.putBoolean(PLAY_WHEN_RDY,mPlayWhenRdy);
    }

    private void releasePlayer() {
        if(mExoPlayer!=null){
            mPlayWhenRdy=mExoPlayer.getPlayWhenReady();
            mCurrPosition=mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer=null;
        }
    }

    private void initializePlayer(Uri uri) {
        if(mExoPlayer==null){
            DefaultBandwidthMeter bandwidthMeter=new DefaultBandwidthMeter();
            TrackSelection.Factory videoTSFactory=new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector=new DefaultTrackSelector(videoTSFactory);
            mExoPlayer=ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector);
            mExoPlayerView.setPlayer(mExoPlayer);
            DataSource.Factory dSFactory=new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(),getString(R.string.app_name)),bandwidthMeter);
            MediaSource videoSource=new ExtractorMediaSource.Factory(dSFactory).createMediaSource(uri);
            mExoPlayer.prepare(videoSource);
            if(mCurrPosition!=0) mExoPlayer.seekTo(mCurrPosition); //onRestore
            mExoPlayer.setPlayWhenReady(mPlayWhenRdy);
            mExoPlayerView.setVisibility(View.VISIBLE);
        }
    }
}
