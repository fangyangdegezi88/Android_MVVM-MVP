package com.focustech.android.photo.myalbum.util.mediastore;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import com.focustech.android.commonlibs.application.BaseApplication;
import com.focustech.android.commonlibs.bridge.cache.localstorage.LocalFileStorageManager;
import com.focustech.android.photo.R;
import com.focustech.android.photo.myalbum.myalbum.bean.Path;
import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaDirectory;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


/**
 * <命名/筛选/排序 参数>
 *
 * @author zhangzeyu
 * @version [版本号, 2016/7/21]
 * @see [相关类/方法]
 * @since [V1]
 */
public class MediaOptions {

    /**
     * 所有图片的文件夹标识
     */
    public final static String KEY_DIRALL = "ALL";

    /**
     * 所有视频的文件夹标识
     */
    public final static String KEY_DIR_VIDEOS = "VIDEOS";
    /**
     * 不筛选，全部显示
     */
    public final static int INCLUDE_ALL = 0xff;
    /**
     * 显示系统的
     */
    public final static int INCLUDE_ENVIRONMENT = 0x01;
    /**
     * 显示私有的
     */
    public final static int INCLUDE_PRIVATE = 0x02;
    /**
     * 显示第三方的
     */
    public final static int INCLUDE_THIRD_PARTY = 0x04;

    /**
     * 旧文件保存根目录
     * deprecated at 2016.8.4
     */
    public static final String FOCUS_TEACH = "Focus Teach";

    /**
     * CHN = CHINESE中文, LET = LETTER字母
     */
    public final static int ORDER_CHN_LET = 10;
    public final static int ORDER_LET_CHN = 11;

    private final static String EXTRA_TRANSLATE = "translate";
    private final static String EXTRA_NAMEMAP = "nameMap";
    private final static String EXTRA_SHOW_DIRALL = "showAllPhoto";
    private final static String EXTRA_DIRALL_NAME = "dirAllName";
    private final static String EXTRA_DIRALL_FILTER = "filterPhotoDirAll";
    private final static String EXTRA_NORMAL_FILTER = "filterPhotoDirNormal";
    private final static String EXTRA_DESC = "desc";
    private final static String EXTRA_ORDER_MODE = "order_mode";
    /**
     * true翻译成中文,false保持英文不动
     */
    private boolean translate;
    /**
     * 中英文对照
     */
    private List<NameMap> nameMaps;
    /**
     * 是否显示“全部照片文件夹”
     */
    private boolean includeDirAll;
    /**
     * “全部照片”文件夹名称
     */
    private String dirAllName;
    /**
     * “全部照片”文件夹的筛选模式
     */
    private int photoDirAllFilter;
    /**
     * 一般文件夹的筛选模式
     */
    private int photoDirNormalFilter;
    /**
     * true=descend降序；false=ascend升序
     */
    private boolean desc;
    /**
     * 数字、中文、字母优先级
     */
    private int orderMode;

    public boolean china() {
        return translate;
    }

    public void setTranslate(boolean translate) {
        this.translate = translate;
    }

    public List<NameMap> nameMaps() {
        return nameMaps;
    }

    public void setNameMaps(List<NameMap> nameMaps) {
        this.nameMaps = nameMaps;
    }

    public boolean includeDirAll() {
        return includeDirAll;
    }

    public void setIncludeDirAll(boolean includeDirAll) {
        this.includeDirAll = includeDirAll;
    }

    public String dirAllName() {
        return dirAllName;
    }

    public void setDirAllName(String dirAllName) {
        this.dirAllName = dirAllName;
    }

    int photoDirAllFilter() {
        return photoDirAllFilter;
    }

    public void setPhotoDirAllFilter(int photoDirAllFilter) {
        this.photoDirAllFilter = photoDirAllFilter;
    }

    int photoDirNormalFilter() {
        return photoDirNormalFilter;
    }

    public void setPhotoDirNormalFilter(int photoDirNormalFilter) {
        this.photoDirNormalFilter = photoDirNormalFilter;
    }

    boolean desc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    int order() {
        return orderMode;
    }

    public void setOrderMode(int orderMode) {
        this.orderMode = orderMode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(Bundle bundle) {
        return new Builder(bundle);
    }

    /**
     * <获得参数>
     *
     * @param bundle
     * @return 默认值或设置值
     */
    public static MediaOptions getOptions(Bundle bundle) {
        MediaOptions options = new MediaOptions();
        options.setTranslate(bundle.getBoolean(EXTRA_TRANSLATE, true));
        ArrayList<NameMap> maps = bundle.getParcelableArrayList(EXTRA_NAMEMAP);
        options.setNameMaps(maps != null ? maps : new ArrayList<NameMap>());
        options.setIncludeDirAll(bundle.getBoolean(EXTRA_SHOW_DIRALL, true));
        options.setDirAllName(bundle.getString(EXTRA_DIRALL_NAME, "最近照片"));
        options.setPhotoDirAllFilter(bundle.getInt(EXTRA_DIRALL_FILTER, INCLUDE_ENVIRONMENT));
        options.setPhotoDirNormalFilter(bundle.getInt(EXTRA_NORMAL_FILTER, INCLUDE_ALL));
        options.setDesc(bundle.getBoolean(EXTRA_DESC, false));
        options.setOrderMode(bundle.getInt(EXTRA_ORDER_MODE, ORDER_CHN_LET));
        return options;
    }

    /**
     * <根据配置重命名文件夹>
     *
     * @param name
     * @param path
     * @return
     */
    public String getNewName(String name, String path) {
        // 重命名第三方应用图片文件夹
        String newname = renameThirdPartyDir(name, path);
        if (newname.equals(name)) {
            // 匹配文件夹名并替换
            if (china() && nameMaps().size() > 0) {
                for (NameMap nameMap : nameMaps()) {
                    if (path != null && path.length() > nameMap.parentDir.length() + 1
                            && path.startsWith(nameMap.parentDir) && !nameMap.chinese.isEmpty()) {
                        return nameMap.chinese;
                    }
                }
            }
        }
        return newname;
    }

    /**
     * <重命名非系统文件夹>
     * 重命名name跟图片的英文沾边的非系统文件夹
     *
     * @param name
     * @param path
     * @return
     */
    String renameThirdPartyDir(String name, String path) {
        File sdCardDir = Environment.getExternalStorageDirectory(); // 式样例如/storage/emulated/0
        if (path != null && path.length() > sdCardDir.getAbsolutePath().length()
                && path.startsWith(sdCardDir.getAbsolutePath())) { // 不在SD卡中的图片不考虑，直接屏蔽
            String relativePath = path.substring(
                    sdCardDir.getAbsolutePath().length() + 1); // 相对Sd卡的路径(例如Baidu/Image/xx.jpg), ps:substring(index) 包括index及以后的
            String[] folders = relativePath.split(File.separator);
            String dirDocuments;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                dirDocuments = Environment.DIRECTORY_DOCUMENTS;
            } else {
                dirDocuments = "Documents";
            }
            if (folders.length == 1) {
                return "手机目录";
            }
            if (!folders[0].equalsIgnoreCase(Environment.DIRECTORY_DCIM)
                    && !folders[0].equalsIgnoreCase(dirDocuments)
                    && !folders[0].equalsIgnoreCase(Environment.DIRECTORY_PICTURES)
                    && !folders[0].equalsIgnoreCase(Environment.DIRECTORY_DOWNLOADS)) {
                if (relateToImage(name)) {
                    return new File(relativePath).getParent();
                }
            } else if (folders[0].equalsIgnoreCase(Environment.DIRECTORY_DCIM)
                    || folders[0].equalsIgnoreCase(Environment.DIRECTORY_PICTURES)) {
                /*
                 重命名DCIM或者PICTURE文件下Screenshots和Camera子文件夹
                 */
                if (folders.length > 2) {
                    for (int i = 1; i < folders.length - 1; i++) {
                        if (folders[i].equals("Screenshots")) {
                            return BaseApplication.getContext().getString(R.string.folder_name_screenshots);
                        } else if (folders[i].equals("Camera")) {
                            return BaseApplication.getContext().getString(R.string.folder_name_dcim);
                        }
                    }
                }
            } else if (folders[0].equalsIgnoreCase(Environment.DIRECTORY_DOWNLOADS)) {
                if (folders.length == 2) {
                    return BaseApplication.getContext().getString(R.string.folder_name_download);
                }
            } else if (folders[0].equalsIgnoreCase(Environment.DIRECTORY_DOCUMENTS)) {
                if (folders.length == 2) {
                    return BaseApplication.getContext().getString(R.string.folder_name_documents);
                }
            }
        }

        return name;
    }

    /**
     * 显示包括的文件夹
     *
     * @return
     */
    public String[] getIncludeDirPath() {
        File sdCardDir = Environment.getExternalStorageDirectory(); // 式样例如/storage/emulated/0
        List<String> array = new ArrayList<>();
        array.add(LocalFileStorageManager.SYS_DIR_DCIM);
        array.add(LocalFileStorageManager.SYS_DIR_PIC);
        array.add(LocalFileStorageManager.SYS_DIR_DOWNLOADS);
        array.add(Path.weiboPhoto);
        array.add(Path.weixinPhoto);
        array.add(sdCardDir.getAbsolutePath() + "/" + FOCUS_TEACH + "/");
        array.add(LocalFileStorageManager.getInstance().getCacheImgFilePath());

        return array.toArray(new String[array.size()]);
    }
    /*
     * <包含判断>
     *
     * @param dirAll 是不是“全部照片”的文件夹
     * @param path 图片所指向路径
     * @return true表示没有被过滤， false表示过滤掉了
     *
    @Deprecated
    public boolean include(boolean dirAll, String path) {
        File sdCardDir = Environment.getExternalStorageDirectory(); // 式样例如/storage/emulated/0
        int mode = dirAll ? photoDirAllFilter() : photoDirNormalFilter();
        boolean include_all     = false;
        boolean include_system  = false;
        boolean include_private = false;
        boolean include_third   = false;
        switch (mode) {
            case INCLUDE_ALL:
                include_all     = true;
                break;
            case INCLUDE_ENVIRONMENT:
                include_system  = true;
                break;
            case INCLUDE_PRIVATE:
                include_private = true;
                break;
            case INCLUDE_THIRD_PARTY:
                include_third   = true;
                break;
            case INCLUDE_ENVIRONMENT | INCLUDE_PRIVATE:
                include_system  = true;
                include_private = true;
                break;
            case INCLUDE_ENVIRONMENT | INCLUDE_THIRD_PARTY:
                include_system  = true;
                include_third   = true;
                break;
            case INCLUDE_PRIVATE | INCLUDE_THIRD_PARTY:
                include_private = true;
                include_third   = true;
                break;
            case INCLUDE_ENVIRONMENT | INCLUDE_THIRD_PARTY | INCLUDE_PRIVATE:
                include_private = true;
                include_system  = true;
                include_third   = true;
                break;
        }

        if (include_all) {
            return true;
        }

        if (path != null && path.length() > sdCardDir.getAbsolutePath().length()
                && path.startsWith(sdCardDir.getAbsolutePath())) { // 不在SD卡中的图片不考虑，直接屏蔽
            String relativePath = path.substring(
                    sdCardDir.getAbsolutePath().length() + 1); // 相对Sd卡的路径,ps:substring(index) 包括index及以后的
            Log.v("MediaStore", "relativePath=" + relativePath);
            return filterDir(path, relativePath, include_private, include_system, include_third);
        }

        return false;
    }

    /**
     * <过滤图片>
     *
     *
     * @param rawPath 绝对路径
     * @param relativePath 相对Sd卡的路径 例如：/Photo/ScreenShots
     * @param include_private
     * @param include_system
     * @param include_third
     * @return
     *
    boolean filterDir(String rawPath, String relativePath, boolean include_private, boolean include_system, boolean include_third) {
        if (include_system) {
            if (rawPath.startsWith(LocalFileStorageManager.SYS_DIR_DCIM)
                || rawPath.startsWith(LocalFileStorageManager.SYS_DIR_PIC)
                || rawPath.startsWith(LocalFileStorageManager.SYS_DIR_DOWNLOADS)
                || rawPath.startsWith(LocalFileStorageManager.SYS_DIR_DOCUMENTS)
                || new File(relativePath).getParent() == null) // 根目录下的图片
            {
                return true;
            }
        }

        String[] folders = relativePath.split(File.separator);
        if (include_private && folders[0].equalsIgnoreCase(Constants.FOCUS_TEACH)) {
            return true;
        }

        if (include_third) {
            for (int i = 1; i < folders.length - 1; i++) { // 例如/wandoujia/pic/xx.image, wangdoujia和xx.image即头尾不参与判断
                String subFolderName = folders[i];
                if (relateToImage(subFolderName)) {
                    return true;
                }
            }
        }
        return false;
    }*/

    private boolean relateToImage(String s) {
        return s.equalsIgnoreCase("img") || s.equalsIgnoreCase("pic") ||
                s.equalsIgnoreCase("image") || s.equalsIgnoreCase("picture") ||
                s.equalsIgnoreCase("images") || s.equalsIgnoreCase("pictures");
    }

    /**
     * <排序>
     *
     * @param directories
     */
    public void sort(List<MediaDirectory> directories) {
        Collections.sort(directories, new Comparator<MediaDirectory>() {
            Comparator pinyinComparator = Collator.getInstance(java.util.Locale.CHINA);
            Comparator englishComaraptor = Collator.getInstance(Locale.ENGLISH);

            @Override
            public int compare(MediaDirectory lhs, MediaDirectory rhs) {
                /** 最近照片在第一位 */
                if (lhs.getId().equals(KEY_DIRALL)) {
                    return -1;
                }

                if (rhs.getId().equals(KEY_DIRALL)) {
                    return 1;
                }

                /**
                 * 中英文先后顺序
                 */
                return compareChineseWithEnglish(lhs, rhs);
            }

            /** 根据首字母排序 */
            int compareChineseWithEnglish(MediaDirectory lhs, MediaDirectory rhs) {
                String lfirst, rfirst;
                lfirst = lhs.getName().substring(0, 1);
                rfirst = rhs.getName().substring(0, 1);
                boolean chineseFirst = orderMode == ORDER_CHN_LET;
                if (isCharacter(lfirst)) {
                    if (!isCharacter(rfirst)) {
                        return chineseFirst ? -1 : 1;
                    } else {
                        int i = pinyinComparator.compare(lhs.getName(), rhs.getName());
                        return desc() ? -i : i;
                    }
                } else {
                    if (isCharacter(rfirst)) {
                        return chineseFirst ? 1 : -1;
                    } else {
                        int i = englishComaraptor.compare(lhs.getName(), rhs.getName());
                        return desc() ? -i : i;
                    }
                }
            }
        });
    }


    /**
     * <判断是不是汉字>
     *
     * @param chr
     * @return
     */
    boolean isCharacter(String chr) {
        return chr.getBytes().length >= 2;
    }

    public static class Builder {

        private Bundle bundle;

        public Builder() {
            bundle = new Bundle();
        }

        public Builder(Bundle bundle) {
            this.bundle = bundle;
        }

        /**
         * <中文标识文件夹名称规则>
         *
         * @param useChinese true启用中文
         * @param nameMap    中英文对照
         *                   “所有照片”文件夹名称
         * @see MediaOptions#KEY_DIRALL
         * 其他最好是系统名称，例如
         * @see android.os.Environment#DIRECTORY_PICTURES
         * @see android.os.Environment#DIRECTORY_DCIM
         * @see android.os.Environment#DIRECTORY_DOWNLOADS
         */
        public Builder nameDisplay(boolean useChinese, NameMap... nameMap) {
            bundle.putBoolean(EXTRA_TRANSLATE, useChinese);
            ArrayList<NameMap> nameMaps = new ArrayList<>();
            nameMaps.addAll(Arrays.asList(nameMap));
            bundle.putParcelableArrayList(EXTRA_NAMEMAP, nameMaps);
            return this;
        }

        /**
         * <是否显示“全部照片”的文件夹,并取名>
         *
         * @param show       true表示显示
         * @param dirAllName 重命名名称
         * @return
         */
        public Builder showPhotoDirAll(boolean show, String dirAllName) {
            bundle.putBoolean(EXTRA_SHOW_DIRALL, show);
            bundle.putString(EXTRA_DIRALL_NAME, dirAllName);
            return this;
        }

        /**
         * <所有照片的过滤规则>
         *
         * @param include 模式
         * @see MediaOptions#INCLUDE_ALL  不做过滤
         * @see MediaOptions#INCLUDE_ENVIRONMENT 包括系统的
         * @see MediaOptions#INCLUDE_PRIVATE 包括我的应用的
         * @see MediaOptions#INCLUDE_THIRD_PARTY 包括第三方的
         * <p>
         * 填写参数时也可以使用组合模式例如MODE_PRIVATE | INCLUDE_ENVIRONMENT
         */
        public Builder filterPhotoDirAll(int include) {
            bundle.putInt(EXTRA_DIRALL_FILTER, include);
            return this;
        }

        /**
         * <一般文件夹过滤规则>
         *
         * @param include
         * @return
         * @see Builder#filterPhotoDirAll(int) 的参数填写规则
         */
        public Builder filterPhotoDirNormal(int include) {
            bundle.putInt(EXTRA_NORMAL_FILTER, include);
            return this;
        }

        /**
         * @param desc  true降序
         * @param order 顺序
         * @return
         * @see MediaOptions#ORDER_CHN_LET 先中文，后字母
         * @see MediaOptions#ORDER_LET_CHN 先字母，后中文
         */
        public Builder order(boolean desc, int order) {
            bundle.putBoolean(EXTRA_DESC, desc);
            bundle.putInt(EXTRA_ORDER_MODE, order);
            return this;
        }

        public Bundle build() {
            return bundle;
        }
    }
}
