package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.database.Cursor;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel.COLUMN_NAME_ARTWORK;
import static com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel.COLUMN_NAME_COMPOSER;
import static com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel.COLUMN_NAME_DURATION;
import static com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel.COLUMN_NAME_LYRICSURL;
import static com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel.COLUMN_NAME_POEM;
import static com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel.COLUMN_NAME_POET;
import static com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel.COLUMN_NAME_SOUNDURL;
import static com.timeofpoetry.timeofpoetry.timeofpoetry.model.MyPlayListModel.COLUMN_NAME_VOICE;


public class PoetryClass {
    public static final String E_MAIL_KEY = "r_user_email";
    public static final String PASSWORD_KEY = "r_user_password";
    public static final String REQUEST_MEMBER = "member";
    public static final String REQUEST_CONTENT = "content";
    public static final String KEY_TYPE = "r_type";
    public static final String AUTHOR = "r_author";
    public static final String POETRY = "r_poetry";
    public static final String VOICE = "r_voice";

    public interface ServerService{
        @Headers({"Accept: application/json"})
        @POST("/tfp_getmypoetry")
        Call<ArrayList<ArrayList<Poem>>> getMyPoetry(@Body GetMyPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_getmonthly")
        Call<ArrayList<ArrayList<Poem>>> getMonthlyPoetry(@Body GetMonthlyPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_getcontent")
        Call<ArrayList<ArrayList<Poem>>> getPlayByList(@Body GetPlayByList body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_set_mypoetry")
        Call<ArrayList<Response>> setMyPoetry(@Body SetMyPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_like_only")
        Call<ArrayList<Response>> like(@Body Like body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_ismypoetry")
        Call<ArrayList<Response>> isMyPoetry(@Body IsMyPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_getweatherpoetry")
        Call<ArrayList<ArrayList<Poem>>> nowPoetry(@Body NowPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/setaddedinfo")
        Call<ArrayList<Response>> addInfo(@Body AddInfo body);

        @Headers({"content-type: application/json"})
        @POST("/getnotice_list")
        Call<ArrayList<ArrayList<BoardIdItem>>> getNoticeId(@Body GetNoticeId body);

        @Headers({"content-type: application/json"})
        @POST("/getnotice_body")
        Call<ArrayList<ArrayList<BoardIdItem>>> getNoticeBody(@Body GetNoticeBody body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_like_rank")
        Call<ArrayList<ArrayList<Poem>>> getRank();

        @Headers({"content-type: application/json"})
        @POST("/get_banner")
        Call<ArrayList<Banner>> getBanner();
    }

    //나의 시집 조회
    public static class GetMyPoetry{
        final String request = PoetryClass.REQUEST_MEMBER;
        ArrayList<KeyValue> get_mypoetry = new ArrayList<>();

        public GetMyPoetry(String email, String pwd){
            this.get_mypoetry.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            this.get_mypoetry.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
        }
    }

    //나의 시집 추가 혹은 중복 체크
    public static class NewMyPoetry{
        final String request = PoetryClass.REQUEST_CONTENT;
        ArrayList<KeyValue> set_mypoetry_and_like = new ArrayList<>();
        ArrayList<KeyValue> is_my_poetry = new ArrayList<>();

        public NewMyPoetry(String option, String email, String poet, String title, String voice){
            ArrayList<KeyValue> handleArray = option.equals("set")? this.set_mypoetry_and_like : this.is_my_poetry;

            handleArray.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            handleArray.add(new KeyValue("r_authorName", poet));
            handleArray.add(new KeyValue("r_poetryTitle", title));
            handleArray.add(new KeyValue("r_voiceName", voice));
        }
    }

    //지금몇시 조회
    public static class GetWeatherPoetry{
        final String request = PoetryClass.REQUEST_MEMBER;
        ArrayList<KeyValue> get_todaylist = new ArrayList<>();

        public GetWeatherPoetry(){
            this.get_todaylist.add(new KeyValue("r_weather", "SKY_ALL"));
            this.get_todaylist.add(new KeyValue("r_temperature", "cold"));
            this.get_todaylist.add(new KeyValue("r_orderby", "id"));
            this.get_todaylist.add(new KeyValue("r_sort", "ASC"));
        }
    }

    public static class SetMyPoetry{
        final String request = PoetryClass.REQUEST_MEMBER;
        ArrayList<KeyValue> set_mypoetry = new ArrayList<>();

        public SetMyPoetry(String work, String email, String pwd, String poet, String poem, String voice){
            this.set_mypoetry.add(new KeyValue("r_work", work));
            this.set_mypoetry.add(new KeyValue(PoetryClass.KEY_TYPE, "single"));
            this.set_mypoetry.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            this.set_mypoetry.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
            this.set_mypoetry.add(new KeyValue(PoetryClass.AUTHOR, poet));
            this.set_mypoetry.add(new KeyValue(PoetryClass.POETRY, poem));
            this.set_mypoetry.add(new KeyValue(PoetryClass.VOICE, voice));

        }
    }

    //월간 몇시 조회
    public static class GetMonthlyPoetry{
        final String request = PoetryClass.REQUEST_MEMBER;
        ArrayList<KeyValue> get_monthlylist = new ArrayList<>();

        public GetMonthlyPoetry(String yearMonth[]){
            this.get_monthlylist.add(new KeyValue(PoetryClass.KEY_TYPE, "month"));
            this.get_monthlylist.add(new KeyValue("r_year", yearMonth[0]));
            this.get_monthlylist.add(new KeyValue("r_month", yearMonth[1]));
            this.get_monthlylist.add(new KeyValue("r_orderby", "x_contentTitle"));
            this.get_monthlylist.add(new KeyValue("r_sort", "ASC"));
        }
    }

    public static class GetPlayByList{
        final String request = PoetryClass.REQUEST_CONTENT;
        ArrayList<KeyValue> get_playbylist = new ArrayList<>();

        public GetPlayByList(String poet, String poem, String voice){
            this.get_playbylist.add(new KeyValue(PoetryClass.KEY_TYPE, "union"));
            this.get_playbylist.add(new KeyValue(PoetryClass.AUTHOR, poet));
            this.get_playbylist.add(new KeyValue(PoetryClass.POETRY, poem));
            this.get_playbylist.add(new KeyValue("r_voice", voice));
        }
    }

    public static class Like{
        final String request = PoetryClass.REQUEST_CONTENT;
        ArrayList<KeyValue> set_like_only = new ArrayList<>();

        public Like(String poet, String poem, String voice){
            this.set_like_only.add(new KeyValue("r_authorName", poet));
            this.set_like_only.add(new KeyValue("r_poetryTitle", poem));
            this.set_like_only.add(new KeyValue("r_voiceName", voice));
        }
    }

    //key value pair
    public static class KeyValue{
        final String key;
        final String value;

        public KeyValue(String key, String value){
            this.key = key;
            this.value = value;
        }
    }

    public static class IsMyPoetry{
        final String request = PoetryClass.REQUEST_CONTENT;
        ArrayList<KeyValue> is_my_poetry = new ArrayList<>();

        public IsMyPoetry(String email, String poet, String poem, String voice){
            this.is_my_poetry.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            this.is_my_poetry.add(new KeyValue("r_authorName", poet));
            this.is_my_poetry.add(new KeyValue("r_poetryTitle", poem));
            this.is_my_poetry.add(new KeyValue("r_voiceName", voice));
        }
    }

    public static class NowPoetry{
        final String request = PoetryClass.REQUEST_MEMBER;
        ArrayList<KeyValue> get_todaylist = new ArrayList<>();

        public NowPoetry(){
            this.get_todaylist.add(new KeyValue("r_weather", "SKY_A11"));
            this.get_todaylist.add(new KeyValue("r_temperatur", "cold"));
            this.get_todaylist.add(new KeyValue("r_orderby", "id"));
            this.get_todaylist.add(new KeyValue("r_sort", "ASC"));
        }
    }

    public static class AddInfo{
        final String request = "addedinfo";
        ArrayList<KeyValue> addedinfo = new ArrayList<>();

        public AddInfo(String email, String pwd, String poet, String poem, String season){
            this.addedinfo.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            this.addedinfo.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
            this.addedinfo.add(new KeyValue("r_nickname", ""));
            this.addedinfo.add(new KeyValue("r_like_poet", poet));
            this.addedinfo.add(new KeyValue("r_like_poetry", poem));
            this.addedinfo.add(new KeyValue("r_like_season", season));
        }
    }

    public static class GetNoticeId {
        final String request = "notice";
        ArrayList<KeyValue> notice_list_info = new ArrayList<>();

        public GetNoticeId(){
            this.notice_list_info.add(new KeyValue("r_notice_date", "all"));
        }
    }

    public static class GetNoticeBody{
        final String request = "notice";
        ArrayList<KeyValue> notice_body_info = new ArrayList<>();

        public GetNoticeBody(int id){
            this.notice_body_info.add(new KeyValue("r_notice_id", Integer.toString(id)));
        }
    }

    //요청 응답용 클래스
    //일반 http response
    public static class Response{
        @SerializedName("return") String status;
        @SerializedName("notification") boolean notification;

        public String getStatus(){
            return status;
        }
    }

    public static class GetSocial{
        @SerializedName("sns_name") String kind;
        @SerializedName("return") String re;

        public String getKind(){return kind;}

        public String getRe(){return re;}
    }

    public static class SetSocial{
        @SerializedName("user_social_id") String re;

        public String getRe(){return re;}
    }


    public static class BoardIdItem{
        @SerializedName("id") int id;
        @SerializedName("x_name") String title;
        @SerializedName("x_notice_date") String date;
        @SerializedName("x_notice_body") String content;
        public ObservableField<String> displayContent = new ObservableField<>("");

        public String getTitle(){
            return this.title;
        }

        public String getDate(){
            return this.date;
        }

        public int getId(){
            return this.id;
        }

        public String getContent(){return this.content; }
    }

    //시 클래스
    public static class Poem{
        @SerializedName("x_artworkURL") String artworkUrl;
        @SerializedName("x_authorID") String authorID;
        @SerializedName("x_authorName") String poet;
        public @SerializedName("x_likecount") int likeCount = 0;
        @SerializedName("x_poetryID") String poetryId;
        @SerializedName("x_poetryTitle") String poetryTitle;
        public @SerializedName("x_soundURL") String soundUrl;
        @SerializedName("x_textURL") String textUrl;
        @SerializedName("x_voiceID") String voiceId;
        @SerializedName("x_voiceName") String voiceName;
        @SerializedName("x_voiceSex") String voiceSex;
        @SerializedName("x_play_time") int playTime;
        @SerializedName("x_BGM_Name") String composer;
        int databaseId = -1;
        private ObservableBoolean isSelected = new ObservableBoolean(false);
        private Bitmap artwork;

        public Poem(){

        }

        public Poem(Cursor c, int id){
            this.artworkUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_ARTWORK));
            this.poet = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_POET));
            this.poetryTitle = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_POEM));
            this.soundUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_SOUNDURL));
            this.textUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_LYRICSURL));
            this.voiceName = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_VOICE));
            this.playTime = c.getInt(c.getColumnIndexOrThrow(COLUMN_NAME_DURATION));
            this.databaseId = id;
            this.composer = c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_COMPOSER));
        }

        public void setPlayTime(int time){
            this.playTime = time;
        }

        public void setArtworkUrl(String url){
            this.artworkUrl = url;
        }

        public void setPoet(String poet){
            this.poet = poet;
        }

        public void setVoiceName(String voice){
            this.voiceName = voice;
        }

        public void setPoetryTitle(String poem){
            this.poetryTitle = poem;
        }

        public void setDatabaseId(int id){
            this.databaseId = id;
        }

        public void setComposer(String name){
            this.composer = name;
        }

        public void setLikeCount(int count){
            this.likeCount = count;
        }
        public String getSoundUrl(){
            return this.soundUrl;
        }

        public String getArtworkUrl(){
            return this.artworkUrl;
        }

        public int getPlayTime(){return this.playTime;}

        public int getDatabaseId(){return this.databaseId;}

        public String getPoet(){
            return this.poet;
        }

        public String getPoem(){
            return this.poetryTitle;
        }

        public String getVoice(){
            return this.voiceName;
        }

        public String getTextUrl(){
            return this.textUrl;
        }

        public ObservableBoolean getIsSelected(){ return this.isSelected;}

        public void setIsSelect(boolean bol){ this.isSelected.set(bol);}

        public int getLike(){return this.likeCount;}

        public String getComposer(){return this.composer;}

        public void setArtwork(Bitmap resource){
            this.artwork = resource;
        }

        public Bitmap getArtwork(){
            return this.artwork;
        }

        public void setSoundUrl(String url){
            this.soundUrl = url;
        }

        public void setTextUrl(String url){
            this.textUrl = url;
        }

        @Override
        public Poem clone(){
            Poem poem = new Poem();
            poem.setIsSelect(false);
            poem.setArtworkUrl(this.getArtworkUrl());
            poem.setPoet(this.getPoet());
            poem.setPoetryTitle(this.getPoem());
            poem.setSoundUrl(this.getSoundUrl());
            poem.setTextUrl(this.getTextUrl());
            poem.setVoiceName(this.getVoice());
            poem.setPlayTime(this.getPlayTime());
            poem.setDatabaseId(this.getDatabaseId());
            poem.setComposer(this.getComposer());

            return poem;
        }
    }

    public static class Banner{
        @SerializedName("x_kind") String kind;
        @SerializedName("x_name") String name;
        @SerializedName("x_uri") String uri = "";
        @SerializedName("x_type") String type;

        public String getUri(){
            return uri;
        }
    }

    public static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://inf.timeforpoetry.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    public static boolean checkStatus(ArrayList<Response> responses){
        String status = responses.get(0).status.split("-")[0];
        if (status.equals("200")) return true;
        else return false;
    }

    public static int checkRegister(ArrayList<Response> responses){
        String status = responses.get(0).status.split("-")[0];
        if(status.equals("200")){
            return 1;
        }
        else if(status.equals("404")){
            return 2;
        }
        else{
            return 0;
        }
    }

    public static Poem getNullPoem(){
        Poem newPoem = new Poem();
        newPoem.setArtworkUrl("");
        newPoem.setPoet("");
        newPoem.setVoiceName("");
        newPoem.setComposer("");
        newPoem.setLikeCount(0);
        newPoem.setPoetryTitle("시를 추가해 주세요");
        newPoem.setPlayTime(0);

        return newPoem;
    }
}