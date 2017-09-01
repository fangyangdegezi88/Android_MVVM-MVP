package com.focustech.android.photo.myalbum.util.mediastore;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Video.Media;

/**
 * Query the given URI, returning a {@link Cursor} over the result set
 * with optional support for cancellation.
 *
 * @author ycy
 * @version [版本号, 2017/6/26]
 * @see [相关类/方法]
 * @since [V1]
 */
public class VideoDirectoryLoader extends CursorLoader {
    final String[] VIDEO_PROJECTION = {
            Media._ID,
            Media.DATA,
            Media.BUCKET_ID,
            Media.BUCKET_DISPLAY_NAME,
            Media.DATE_ADDED,
            Media.SIZE,
            Media.DURATION
    };

    public VideoDirectoryLoader(Context context) {
        super(context);

        setProjection(VIDEO_PROJECTION);
        setUri(Media.EXTERNAL_CONTENT_URI);
        setSortOrder(Media.DATE_ADDED + " DESC");

        StringBuilder selections = new StringBuilder();
        setSelection(selections.toString());
        String[] selectionArgs = new String[]{};
        setSelectionArgs(selectionArgs);
    }

    public VideoDirectoryLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
