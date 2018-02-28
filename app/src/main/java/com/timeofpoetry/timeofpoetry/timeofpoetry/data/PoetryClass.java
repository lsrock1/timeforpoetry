package com.timeofpoetry.timeofpoetry.timeofpoetry.data;

import android.database.Cursor;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.graphics.Bitmap;
import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import com.timeofpoetry.timeofpoetry.timeofpoetry.data.PlayListDB;


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
        Call<List<List<Poem>>> getMyPoetry(@Body GetMyPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_getmonthly")
        Call<List<List<Poem>>> getMonthlyPoetry(@Body GetMonthlyPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_getcontent")
        Call<List<List<Poem>>> getPlayByList(@Body GetPlayByList body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_set_mypoetry")
        Call<List<Response>> setMyPoetry(@Body SetMyPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_like_only")
        Call<List<Response>> like(@Body Like body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_ismypoetry")
        Call<List<Response>> isMyPoetry(@Body IsMyPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_getweatherpoetry")
        Call<List<List<Poem>>> nowPoetry(@Body NowPoetry body);

        @Headers({"content-type: application/json"})
        @POST("/setaddedinfo")
        Call<List<Response>> addInfo(@Body AddInfo body);

        @Headers({"content-type: application/json"})
        @POST("/getnotice_list")
        Call<List<List<BoardIdItem>>> getNoticeId(@Body GetNoticeId body);

        @Headers({"content-type: application/json"})
        @POST("/getnotice_body")
        Call<List<List<BoardIdItem>>> getNoticeBody(@Body GetNoticeBody body);

        @Headers({"content-type: application/json"})
        @POST("/tfp_like_rank")
        Call<List<List<Poem>>> getRank();

        @Headers({"content-type: application/json"})
        @POST("/get_banner")
        Call<List<Banner>> getBanner();

        @Headers({"content-type: application/json"})
        @POST("/getaddedinfo")
        Call<List<List<GetAddInfo>>> getAddInfo(@Body RequestAddInfo body);

        @Headers({"Accept: application/json"})
        @POST("/tfp_regist")
        Call<List<Response>> signUp(@Body SignUp body);

        @Headers({"Accept: application/json"})
        @POST("/tfp_login")
        Call<List<Response>> signIn(@Body SignIn body);

        @Headers({"Accept: application/json"})
        @POST("/get_sns")
        Call<List<GetSocial>> getSns(@Body GetSns body);

        @Headers({"Accept: application/json"})
        @POST("/set_sns")
        Call<List<SetSocial>> setSns(@Body SetSns body);
    }

    public static class SignUp{
        private final String request = PoetryClass.REQUEST_MEMBER;
        private List<KeyValue> regist = new LinkedList<>();

        public SignUp(String email, String pwd){
            regist.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            regist.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
        }
    }

    public static class SignIn{
        private final String request = PoetryClass.REQUEST_MEMBER;
        private List<KeyValue> login = new LinkedList<>();

        public SignIn(String email, String pwd){
            login.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            login.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
        }
    }

    public static class SetSns{
        private final String request = "SNS";
        private List<KeyValue> create = new LinkedList<>();

        public SetSns(String email, String pwd, int kind){
            create.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            create.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
            create.add(new KeyValue("r_social_kind", kind == 0 ? "KAKAO/TALK" : "facebook"));
            create.add(new KeyValue("r_auth_token", ""));
            create.add(new KeyValue("r_access_token", ""));
            create.add(new KeyValue("r_refresh_token", ""));
        }
    }

    public static class GetSns{
        private final String request = "SNS";
        private List<KeyValue> search = new LinkedList<>();

        public GetSns(String email, String pwd, int kind){
            search.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            search.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
            search.add(new KeyValue("r_social_kind", kind == 0 ? "KAKAO/TALK" : "FACEBOOK"));
        }
    }

    //나의 시집 조회
    public static class GetMyPoetry{
        private final String request = PoetryClass.REQUEST_MEMBER;
        private List<KeyValue> get_mypoetry = new LinkedList<>();

        public GetMyPoetry(String email, String pwd){
            get_mypoetry.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            get_mypoetry.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
        }
    }

    //나의 시집 추가 혹은 중복 체크
    public static class NewMyPoetry{
        private final String request = PoetryClass.REQUEST_CONTENT;
        private List<KeyValue> set_mypoetry_and_like = new LinkedList<>();
        private List<KeyValue> is_my_poetry = new LinkedList<>();

        public NewMyPoetry(String option, String email, String poet, String title, String voice){
            List<KeyValue> handleArray = option.equals("set")? set_mypoetry_and_like : is_my_poetry;

            handleArray.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            handleArray.add(new KeyValue("r_authorName", poet));
            handleArray.add(new KeyValue("r_poetryTitle", title));
            handleArray.add(new KeyValue("r_voiceName", voice));
        }
    }

    //지금몇시 조회
    public static class GetWeatherPoetry{
        private final String request = PoetryClass.REQUEST_MEMBER;
        private List<KeyValue> get_todaylist = new LinkedList<>();

        public GetWeatherPoetry(){
            get_todaylist.add(new KeyValue("r_weather", "SKY_ALL"));
            get_todaylist.add(new KeyValue("r_temperature", "cold"));
            get_todaylist.add(new KeyValue("r_orderby", "id"));
            get_todaylist.add(new KeyValue("r_sort", "ASC"));
        }
    }

    public static class SetMyPoetry{
        private final String request = PoetryClass.REQUEST_MEMBER;
        private List<KeyValue> set_mypoetry = new LinkedList<>();

        public SetMyPoetry(String work, String email, String pwd, String poet, String poem, String voice){
            set_mypoetry.add(new KeyValue("r_work", work));
            set_mypoetry.add(new KeyValue(PoetryClass.KEY_TYPE, "single"));
            set_mypoetry.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            set_mypoetry.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
            set_mypoetry.add(new KeyValue(PoetryClass.AUTHOR, poet));
            set_mypoetry.add(new KeyValue(PoetryClass.POETRY, poem));
            set_mypoetry.add(new KeyValue(PoetryClass.VOICE, voice));

        }
    }

    //월간 몇시 조회
    public static class GetMonthlyPoetry{
        private final String request = PoetryClass.REQUEST_MEMBER;
        private List<KeyValue> get_monthlylist = new LinkedList<>();

        public GetMonthlyPoetry(String yearMonth[]){
            get_monthlylist.add(new KeyValue(PoetryClass.KEY_TYPE, "month"));
            get_monthlylist.add(new KeyValue("r_year", yearMonth[0]));
            get_monthlylist.add(new KeyValue("r_month", yearMonth[1]));
            get_monthlylist.add(new KeyValue("r_orderby", "x_contentTitle"));
            get_monthlylist.add(new KeyValue("r_sort", "ASC"));
        }
    }

    public static class GetPlayByList{
        private final String request = PoetryClass.REQUEST_CONTENT;
        private List<KeyValue> get_playbylist = new LinkedList<>();

        public GetPlayByList(String poet, String poem, String voice){
            get_playbylist.add(new KeyValue(PoetryClass.KEY_TYPE, "union"));
            get_playbylist.add(new KeyValue(PoetryClass.AUTHOR, poet));
            get_playbylist.add(new KeyValue(PoetryClass.POETRY, poem));
            get_playbylist.add(new KeyValue("r_voice", voice));
        }
    }

    public static class Like{
        private final String request = PoetryClass.REQUEST_CONTENT;
        private List<KeyValue> set_like_only = new LinkedList<>();

        public Like(String poet, String poem, String voice){
            set_like_only.add(new KeyValue("r_authorName", poet));
            set_like_only.add(new KeyValue("r_poetryTitle", poem));
            set_like_only.add(new KeyValue("r_voiceName", voice));
        }
    }

    //key value pair
    static class KeyValue{
        private final String key;
        private final String value;

        KeyValue(String key, String value){
            this.key = key;
            this.value = value;
        }
    }

    public static class IsMyPoetry{
        private final String request = PoetryClass.REQUEST_CONTENT;
        private List<KeyValue> is_my_poetry = new LinkedList<>();

        public IsMyPoetry(String email, String poet, String poem, String voice){
            is_my_poetry.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            is_my_poetry.add(new KeyValue("r_authorName", poet));
            is_my_poetry.add(new KeyValue("r_poetryTitle", poem));
            is_my_poetry.add(new KeyValue("r_voiceName", voice));
        }
    }

    public static class NowPoetry{
        private final String request = PoetryClass.REQUEST_MEMBER;
        private List<KeyValue> get_todaylist = new LinkedList<>();

        public NowPoetry(){
            get_todaylist.add(new KeyValue("r_weather", "SKY_A11"));
            get_todaylist.add(new KeyValue("r_temperatur", "cold"));
            get_todaylist.add(new KeyValue("r_orderby", "id"));
            get_todaylist.add(new KeyValue("r_sort", "ASC"));
        }
    }

    public static class AddInfo{
        private final String request = "addedinfo";
        private List<KeyValue> addedinfo = new LinkedList<>();

        public AddInfo(String email, String pwd, String poet, String poem, String season){
            addedinfo.add(new KeyValue(PoetryClass.E_MAIL_KEY, email));
            addedinfo.add(new KeyValue(PoetryClass.PASSWORD_KEY, pwd));
            addedinfo.add(new KeyValue("r_nickname", email));
            addedinfo.add(new KeyValue("r_like_poet", poet));
            addedinfo.add(new KeyValue("r_like_poetry", poem));
            addedinfo.add(new KeyValue("r_like_season", season));
        }
    }

    public static class GetNoticeId {
        private final String request = "notice";
        private List<KeyValue> notice_list_info = new LinkedList<>();

        public GetNoticeId(){
            notice_list_info.add(new KeyValue("r_notice_date", "all"));
        }
    }

    public static class GetNoticeBody{
        private final String request = "notice";
        private List<KeyValue> notice_body_info = new LinkedList<>();

        public GetNoticeBody(int id){
            notice_body_info.add(new KeyValue("r_notice_id", Integer.toString(id)));
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
    public static class Poem implements Parcelable{
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
        private boolean ward;

        public Poem(){

        }

        public Poem(Cursor c, int id){
            artworkUrl = c.getString(c.getColumnIndexOrThrow(PlayListDB.COLUMN_NAME_ARTWORK));
            poet = c.getString(c.getColumnIndexOrThrow(PlayListDB.COLUMN_NAME_POET));
            poetryTitle = c.getString(c.getColumnIndexOrThrow(PlayListDB.COLUMN_NAME_POEM));
            soundUrl = c.getString(c.getColumnIndexOrThrow(PlayListDB.COLUMN_NAME_SOUNDURL));
            textUrl = c.getString(c.getColumnIndexOrThrow(PlayListDB.COLUMN_NAME_LYRICSURL));
            voiceName = c.getString(c.getColumnIndexOrThrow(PlayListDB.COLUMN_NAME_VOICE));
            playTime = c.getInt(c.getColumnIndexOrThrow(PlayListDB.COLUMN_NAME_DURATION));
            databaseId = id;
            composer = c.getString(c.getColumnIndexOrThrow(PlayListDB.COLUMN_NAME_COMPOSER));
        }

        public Poem(Parcel src) {
//            dest.writeString(artworkUrl);
//            dest.writeString(authorID);
//            dest.writeString(poet);
//            dest.writeInt(likeCount);
//            dest.writeString(poetryId);
//            dest.writeString(poetryTitle);
//            dest.writeString(soundUrl);
//            dest.writeString(textUrl);
//            dest.writeString(voiceId);
//            dest.writeString(voiceName);
//            dest.writeString(voiceSex);
//            dest.writeInt(playTime);
//            dest.writeString(composer);
//            dest.writeInt(databaseId);
//            if(artwork != null) dest.writeValue(artwork)
            this.artworkUrl = src.readString();
            this.authorID = src.readString();
            this.poet = src.readString();
            this.likeCount = src.readInt();
            this.poetryId = src.readString();
            this.poetryTitle = src.readString();
            this.soundUrl = src.readString();
            this.textUrl = src.readString();
            this.voiceId = src.readString();
            this.voiceName = src.readString();
            this.voiceSex = src.readString();
            this.playTime = src.readInt();
            this.composer = src.readString();
            this.databaseId = src.readInt();
        }

        public boolean isWard() {
            return ward;
        }

        public void setWard(boolean ward) {
            this.ward = ward;
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

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(artworkUrl);
            dest.writeString(authorID);
            dest.writeString(poet);
            dest.writeInt(likeCount);
            dest.writeString(poetryId);
            dest.writeString(poetryTitle);
            dest.writeString(soundUrl);
            dest.writeString(textUrl);
            dest.writeString(voiceId);
            dest.writeString(voiceName);
            dest.writeString(voiceSex);
            dest.writeInt(playTime);
            dest.writeString(composer);
            dest.writeInt(databaseId);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator CREATOR = new Creator<Poem>(){
            public Poem createFromParcel(Parcel src){
                return new Poem(src);
            }

            @Override
            public Poem[] newArray(int size) {
                return new Poem[0];
            }
        };
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

    public static class GetAddInfo{
        @SerializedName("x_like_poet") String poet;
        @SerializedName("x_like_poetry") String poetry;
        @SerializedName("x_like_season") String season;

        public String getPoet() {
            return poet;
        }

        public String getPoetry() {
            return poetry;
        }

        public String getSeason() {
            return season;
        }
    }

    public static class RequestAddInfo{
        final String request = REQUEST_MEMBER;
        ArrayList<KeyValue> addedinfo = new ArrayList<>();

        public RequestAddInfo(String email, String pwd){
            addedinfo.add(new KeyValue(E_MAIL_KEY, email));
            addedinfo.add(new KeyValue(PASSWORD_KEY, pwd));
        }
    }

    public static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://inf.timeforpoetry.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    public static boolean checkStatus(List<Response> responses){
        String status = responses.get(0).status.split("-")[0];
        Log.d("test response code", responses.get(0).status);
        return status.equals("200");
    }

    public static int checkRegister(List<Response> responses){
        String status = responses.get(0).status.split("-")[0];
        Log.d("test register code", responses.get(0).status);
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
        newPoem.setArtwork(null);

        return newPoem;
    }
}
