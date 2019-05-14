package com.vk.constants;

public class Constants {

    public static final int PUBLIC_ID_WITH_AUDIO_ON_THE_WALL = -170362981;
    public static final int NUMBER_OF_PHOTOS_IN_THE_MESSAGE = 9;
    public static final int ALEXANDER_BOLDYREV_VKID = 662638;
    public static final int VASILII_KALITEEVSKY_VKID = 1701808;
    public final static int MAX_AVAILABLE_PHOTOS_COUNT = 1000;

    public static final String KEYBOARD = "{ \"one_time\": true, \"buttons\": " +
            "[[{ \"action\": { \"type\": \"text\", \"payload\": \"{\\\"button\\\": \\\"3\\\"}\", " +
            "\"label\": \"Подробнее о музыканте\" }, \"color\": \"default\" }] ] } ";

    private String indexPath;
    private String reIndexPath;
    private String photoFolderPath;
    private String userPhotoFolderPath;

    private String indexPathAudio;
    private String reIndexPathAudio;
    private String photoFolderPathAudio;
    private String userPhotoFolderPathAudio;

    private String indexPathAudioCommerce;
    private String reIndexPathAudioCommerce;
    private String photoFolderPathAudioCommerce;
    private String userPhotoFolderPathAudioCommerce;
    private int groupIdWithMinus;
    private String albumMusicId;
    private String albumMusicCommerceId;

    public Constants(String indexPath, String reIndexPath, String photoFolderPath,
                     String userPhotoFolderPath, String indexPathAudio, String reIndexPathAudio,
                     String photoFolderPathAudio, String userPhotoFolderPathAudio, String indexPathAudioCommerce,
                     String reIndexPathAudioCommerce, String photoFolderPathAudioCommerce,
                     String userPhotoFolderPathAudioCommerce, int groupIdWithMinus, String albumMusicId, String albumMusicCommerceId) {
        this.indexPath = indexPath;
        this.reIndexPath = reIndexPath;
        this.photoFolderPath = photoFolderPath;
        this.userPhotoFolderPath = userPhotoFolderPath;
        this.indexPathAudio = indexPathAudio;
        this.reIndexPathAudio = reIndexPathAudio;
        this.photoFolderPathAudio = photoFolderPathAudio;
        this.userPhotoFolderPathAudio = userPhotoFolderPathAudio;
        this.indexPathAudioCommerce = indexPathAudioCommerce;
        this.reIndexPathAudioCommerce = reIndexPathAudioCommerce;
        this.photoFolderPathAudioCommerce = photoFolderPathAudioCommerce;
        this.userPhotoFolderPathAudioCommerce = userPhotoFolderPathAudioCommerce;
        this.groupIdWithMinus = groupIdWithMinus;
        this.albumMusicId = albumMusicId;
        this.albumMusicCommerceId = albumMusicCommerceId;
    }

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath;
    }

    public String getReIndexPath() {
        return reIndexPath;
    }

    public void setReIndexPath(String reIndexPath) {
        this.reIndexPath = reIndexPath;
    }

    public String getPhotoFolderPath() {
        return photoFolderPath;
    }

    public void setPhotoFolderPath(String photoFolderPath) {
        this.photoFolderPath = photoFolderPath;
    }

    public String getUserPhotoFolderPath() {
        return userPhotoFolderPath;
    }

    public void setUserPhotoFolderPath(String userPhotoFolderPath) {
        this.userPhotoFolderPath = userPhotoFolderPath;
    }

    public String getIndexPathAudio() {
        return indexPathAudio;
    }

    public void setIndexPathAudio(String indexPathAudio) {
        this.indexPathAudio = indexPathAudio;
    }

    public String getReIndexPathAudio() {
        return reIndexPathAudio;
    }

    public void setReIndexPathAudio(String reIndexPathAudio) {
        this.reIndexPathAudio = reIndexPathAudio;
    }

    public String getPhotoFolderPathAudio() {
        return photoFolderPathAudio;
    }

    public void setPhotoFolderPathAudio(String photoFolderPathAudio) {
        this.photoFolderPathAudio = photoFolderPathAudio;
    }

    public String getUserPhotoFolderPathAudio() {
        return userPhotoFolderPathAudio;
    }

    public void setUserPhotoFolderPathAudio(String userPhotoFolderPathAudio) {
        this.userPhotoFolderPathAudio = userPhotoFolderPathAudio;
    }

    public String getIndexPathAudioCommerce() {
        return indexPathAudioCommerce;
    }

    public void setIndexPathAudioCommerce(String indexPathAudioCommerce) {
        this.indexPathAudioCommerce = indexPathAudioCommerce;
    }

    public String getReIndexPathAudioCommerce() {
        return reIndexPathAudioCommerce;
    }

    public void setReIndexPathAudioCommerce(String reIndexPathAudioCommerce) {
        this.reIndexPathAudioCommerce = reIndexPathAudioCommerce;
    }

    public String getPhotoFolderPathAudioCommerce() {
        return photoFolderPathAudioCommerce;
    }

    public void setPhotoFolderPathAudioCommerce(String photoFolderPathAudioCommerce) {
        this.photoFolderPathAudioCommerce = photoFolderPathAudioCommerce;
    }

    public String getUserPhotoFolderPathAudioCommerce() {
        return userPhotoFolderPathAudioCommerce;
    }

    public void setUserPhotoFolderPathAudioCommerce(String userPhotoFolderPathAudioCommerce) {
        this.userPhotoFolderPathAudioCommerce = userPhotoFolderPathAudioCommerce;
    }

    public int getGroupIdWithMinus() {
        return groupIdWithMinus;
    }

    public void setGroupIdWithMinus(int groupIdWithMinus) {
        this.groupIdWithMinus = groupIdWithMinus;
    }

    public String getAlbumMusicId() {
        return albumMusicId;
    }

    public void setAlbumMusicId(String albumMusicId) {
        this.albumMusicId = albumMusicId;
    }

    public String getAlbumMusicCommerceId() {
        return albumMusicCommerceId;
    }

    public void setAlbumMusicCommerceId(String albumMusicCommerceId) {
        this.albumMusicCommerceId = albumMusicCommerceId;
    }

    /*public static final String indexPath = "C:\\Users\\Администратор\\ProfoundBotStaff\\reIndex";
    public static final String reIndexPath = "C:\\Users\\Администратор\\ProfoundBotStaff\\reIndex";
    public static final String photoFolderPath = "C:\\Users\\Администратор\\ProfoundBotStaff\\PhotoForBot";
    public static final String userPhotoFolderPath = "C:\\Users\\Администратор\\ProfoundBotStaff\\PhotoFromUser\\";

    public static final String indexPathAudio = "C:\\Users\\Администратор\\ProfoundBotStaff\\reIndexAudio";
    public static final String reIndexPathAudio = "C:\\Users\\Администратор\\ProfoundBotStaff\\reIndexAudio";
    public static final String photoFolderPathAudio = "C:\\Users\\Администратор\\ProfoundBotStaff\\PhotoForBotAudio";
    public static final String userPhotoFolderPathAudio = "C:\\Users\\Администратор\\ProfoundBotStaff\\PhotoFromUserAudio\\";

    public static final String indexPathAudioCommerce = "C:\\Users\\Администратор\\ProfoundBotStaff\\reIndexAudioCommerce";
    public static final String reIndexPathAudioCommerce = "C:\\Users\\Администратор\\ProfoundBotStaff\\reIndexAudioCommerce";
    public static final String photoFolderPathAudioCommerce = "C:\\Users\\Администратор\\ProfoundBotStaff\\PhotoForBotAudioCommerce";
    public static final String userPhotoFolderPathAudioCommerce = "C:\\Users\\Администратор\\ProfoundBotStaff\\PhotoFromUserAudioCommerce\\";*/





}
