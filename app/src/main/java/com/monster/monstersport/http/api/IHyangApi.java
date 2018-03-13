package com.monster.monstersport.http.api;


import com.monster.monstersport.bean.BookBean;
import com.monster.monstersport.bean.ChapterBean;
import com.monster.monstersport.bean.ChapterListBean;
import com.monster.monstersport.http.BaseHttpResult;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author zhzy
 * @date 2017/11/1
 */
public interface IHyangApi {

    String BASE_URL = "https://app.xhhread.com/";

    /* 章节目录 */
    @GET("/chapter/searchChapterListVO.i?pageSize=10000")
    Observable<BaseHttpResult<ChapterListBean>> searchChapterListVO(@Query("storyid") String storyid);

    /* 获取章节内容*/
    @GET("/chapter/getChapterReadById.i")
    Observable<BaseHttpResult<ChapterBean>> getChapterReadByIdV2(@Query("chapterid") String chapterid);


    /* 书籍特定章的上一章 */
    @GET("/chapter/getPreChapterReadById.i")
    Observable<BaseHttpResult<ChapterBean>> getPreChapterReadByIdV2(@Query("chapterid") String chapterid);


    /* 书籍特定章的下一章 */
    @GET("/chapter/getNextChapterReadById.i")
    Observable<BaseHttpResult<ChapterBean>> getNextChapterReadByIdV2(@Query("chapterid") String chapterid);


    //长篇详情
    @GET("/longstory/getLongStoryInfoByIdNew.i")
    Observable<BaseHttpResult<BookBean>> getLongStoryInfoByIdNew(@Query("storyid") String storyid);

}
