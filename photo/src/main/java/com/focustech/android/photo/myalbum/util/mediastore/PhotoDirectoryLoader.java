package com.focustech.android.photo.myalbum.util.mediastore;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;

/**
 * Query the given URI, returning a {@link Cursor} over the result set
 * with optional support for cancellation.
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/14]
 * @see [相关类/方法]
 * @since [V1]
 */
public class PhotoDirectoryLoader extends CursorLoader {
    final String[] IMAGE_PROJECTION = {
            Media._ID,
            Media.DATA,
            Media.BUCKET_ID,
            Media.BUCKET_DISPLAY_NAME,
            Media.DATE_ADDED,
            Media.SIZE
    };

    public PhotoDirectoryLoader(Context context, boolean showGif, String[] like) {
        super(context);

        setProjection(IMAGE_PROJECTION);
        setUri(Media.EXTERNAL_CONTENT_URI);
        setSortOrder(Media.DATE_ADDED + " DESC");

        StringBuilder selections = new StringBuilder();
        selections.append("(");
        selections.append(Media.MIME_TYPE);
        selections.append("=? or ");
        selections.append(Media.MIME_TYPE);
        selections.append("=? or ");
        selections.append(Media.MIME_TYPE);
        selections.append("=?");
        if (showGif) {
            selections.append("or ");
            selections.append(Media.MIME_TYPE);
            selections.append("=? ");
        }
        selections.append(") ");
        if (like.length > 0) {
            for (int i = 0; i < like.length; i++) {
                if (i == 0) {
                    selections.append(" and (");
                } else {
                    selections.append(" or ");
                }
                selections.append("(");
                selections.append(Media.DATA);
                selections.append(" like '");
                selections.append(like[i]);
                selections.append("%'");
                selections.append(")");

            }
            /*
             根目录下的图片
             */
            selections.append(" or (");
            selections.append("(");
            selections.append(Media.DATA);
            selections.append(" like '");
            selections.append(Environment.getExternalStorageDirectory());
            selections.append("/");
            selections.append("%') and (");
            selections.append(Media.DATA);
            selections.append(" not like '");
            selections.append(Environment.getExternalStorageDirectory());
            selections.append("/");
            selections.append("%/%')");
            selections.append(")");

            selections.append(")");
        }
        setSelection(selections.toString());

        String[] selectionArgs;
        if (showGif) {
            selectionArgs = new String[] { "image/jpeg", "image/png", "image/jpg","image/gif" };
        } else {
            selectionArgs = new String[] { "image/jpeg", "image/png", "image/jpg" };
        }
        setSelectionArgs(selectionArgs);
    }

    public PhotoDirectoryLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
