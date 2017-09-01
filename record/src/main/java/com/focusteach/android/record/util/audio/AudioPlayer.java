package com.focusteach.android.record.util.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 音频播放
 *
 * @author zhangxu
 */
public class AudioPlayer {
    public static final String DEFAULT_ID = "default";

    private Map<String, MediaPlayer> plays = new HashMap<>();
    private final AudioManager audioManager;
    private float volume = 0f;

    public AudioPlayer(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public boolean start(String url, boolean looping, int streamType, AudioPlayerCallback audioPlayerCallback) throws IOException {
        return start(DEFAULT_ID, url, looping, streamType, audioPlayerCallback);
    }

    public boolean start(String audioId, File file, boolean looping, int streamType, AudioPlayerCallback audioPlayerCallback) throws IOException {
        return start(audioId, file.getAbsolutePath(), looping, streamType, audioPlayerCallback);
    }

    public boolean start(final String audioId, String path, boolean looping, int streamType, final AudioPlayerCallback audioPlayerCallback) throws IOException {
        int result = audioManager.requestAudioFocus(afChangeListener,streamType,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        Log.d("audioFocusChange-result",result+"");

        refreshVolume();
        stop(audioId);

        MediaPlayer player = new MediaPlayer();
        player.setLooping(looping);
        player.setAudioStreamType(streamType);

        player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (null != audioPlayerCallback) {
                    audioPlayerCallback.onAudioBufferingUpdated(audioId, percent);
                }
            }
        });

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop(audioId);
                if (null != audioPlayerCallback) {
                    audioManager.abandonAudioFocus(afChangeListener);
                    audioPlayerCallback.onAudioPlayCompleted(audioId);
                }
            }
        });

        player.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                if (null != audioPlayerCallback) {
                    audioPlayerCallback.onAudioSeekCompleted(audioId);
                }
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                String msg;
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                        msg = "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK";
                        break;
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        msg = "MEDIA_ERROR_SERVER_DIED";
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        msg = "MEDIA_ERROR_UNKNOWN";
                        break;
                    default:
                        msg = "MEDIA_ERROR_UNKNOWN";
                        break;
                }

                if (null != audioPlayerCallback) {
                    audioPlayerCallback.onError(audioId, what, extra, msg);
                }
                return false;
            }
        });

        player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                String msg;
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                        msg = "MEDIA_INFO_BAD_INTERLEAVING";
                        break;
                    case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                        msg = "MEDIA_INFO_METADATA_UPDATE";
                        break;
                    case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                        msg = "MEDIA_INFO_NOT_SEEKABLE";
                        break;
                    case MediaPlayer.MEDIA_INFO_UNKNOWN:
                        msg = "MEDIA_INFO_UNKNOWN";
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                        msg = "MEDIA_INFO_VIDEO_TRACK_LAGGING";
                        break;
                    default:
                        msg = "MEDIA_INFO_UNKNOWN";
                        break;
                }

                if (null != audioPlayerCallback) {
                    audioPlayerCallback.onInfo(audioId, what, extra, msg);
                }
                return false;
            }
        });

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });

        player.setDataSource(path);
        player.prepareAsync();
        plays.put(audioId, player);
        return true;
    }

    public void stop() {
        stop(DEFAULT_ID);
    }

    public void stop(String audioId) {
        MediaPlayer player = plays.get(audioId);

        if (player == null) return;

        player.stop();
        player.release();
        plays.remove(audioId);

        audioManager.abandonAudioFocus(afChangeListener);
    }

    public void stopAll() {
        Set<String> ids = plays.keySet();
        Iterator<String> iterator = ids.iterator();

        while (iterator.hasNext()) {
            MediaPlayer player = plays.get(iterator.next());
            try {
                player.stop();
                player.release();
                player = null;
            } catch (Exception e) {
            }
        }

        plays.clear();

        audioManager.abandonAudioFocus(afChangeListener);
    }

    public void pause() {
        pause(DEFAULT_ID);
    }

    public void pause(String audioId) {
        MediaPlayer player = plays.get(audioId);

        if (player == null) return;

        player.pause();
    }

    public void resume() {
        resume(DEFAULT_ID);
    }

    public void resume(String audioId) {
        MediaPlayer player = plays.get(audioId);

        if (player == null) return;

        player.start();
    }

    public void seekTo(int milliseconds) {
        seekTo(DEFAULT_ID, milliseconds);
    }

    public void seekTo(String audioId, int milliseconds) {
        MediaPlayer player = plays.get(audioId);

        if (player == null) return;

        player.seekTo(milliseconds);
    }

    public int getDuration() {
        return getDuration(DEFAULT_ID);
    }

    public int getDuration(String audioId) {
        MediaPlayer player = plays.get(audioId);

        if (player == null) return -1;

        return player.getDuration();
    }

    public int getCurrentPosition() {
        return getCurrentPosition(DEFAULT_ID);
    }

    public int getCurrentPosition(String audioId) {
        MediaPlayer player = plays.get(audioId);

        if (player == null) return -1;

        return player.getCurrentPosition();
    }

    public boolean isLooping() {
        return isLooping(DEFAULT_ID);
    }

    public boolean isLooping(String audioId) {
        MediaPlayer player = plays.get(audioId);
        if (player == null) return false;
        return player.isLooping();
    }

    public boolean isPlaying() {
        return isPlaying(DEFAULT_ID);
    }

    public boolean isPlaying(String audioId) {
        MediaPlayer player = plays.get(audioId);

        if (player == null) return false;

        return player.isPlaying();
    }

    public boolean setVolume(float volume) {
        if (volume < 0 || volume > 1) {
            return false;
        }
        this.volume = volume;
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.round((audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume)), 0);

        return true;
    }

    private boolean refreshVolume(){
        if(volume > 0){
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, Math.round((audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) * volume)), 0);
            return true;
        }
        return false;
    }

    //系统音频焦点监听
    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.d("audioFocusChange-begin",focusChange+"");
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.d("audioFocusChange","AUDIOFOCUS_LOSS");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.d("audioFocusChange","AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_GAIN://获得焦点
                    Log.d("audioFocusChange","AUDIOFOCUS_GAIN");
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT://获取短暂焦点：
                    Log.d("audioFocusChange","AUDIOFOCUS_GAIN_TRANSIENT");
                    break;

            }
        }
    };
}
