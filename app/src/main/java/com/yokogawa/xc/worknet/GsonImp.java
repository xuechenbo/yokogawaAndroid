package com.yokogawa.xc.worknet;

import com.yokogawa.xc.bean.LoginBean;
import com.yokogawa.xc.bean.TokenBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GsonImp {

    //登录
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("staffLogin/login")
    Call<HttpResult<LoginBean>> login(@Body RequestBody info);

    //员工修改密码
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("staffLogin/updatePassword")
    Call<HttpResult<String>> updatePassword(@Header("Authorization") String token, @Body RequestBody info);

    //根据token 获取员工信息
    @POST("staffLogin/getToken")
    Call<HttpResult<String>> getTokenMsg(@Header("Authorization") String token);

    //退出登录
    @POST("staffLogin/logout")
    Call<HttpResult<TokenBean>> logout(@Header("Authorization") String token);

    //更新token
    @POST("staffLogin/refreshToken")
    Call<HttpResult<LoginBean>> updataToken(@Header("Authorization") String token);

    //上传签名
    @Multipart
    @POST("staffLogin/update")
    Call<HttpResult<String>> updateSign(@Header("Authorization") String token, @Part MultipartBody.Part file);


    //获取当天生成的检查表
    @FormUrlEncoded
    @POST("staffSchedule/queryThisDayCheck")
    Call<HttpResult<String>> queryThisDayCheck(@Header("Authorization") String token,
                                               @Field("checkName") String checkName,
                                               @Field("groupId") String groupIds,
                                               @Field("stationId") String stationId,
                                               @Field("pageNum") String page,
                                               @Field("pageSize") String size);

    //员工提交检查表，保存进度
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("staffSchedule/add")
    Call<HttpResult<String>> staffSchedule(@Header("Authorization") String token, @Body RequestBody info);

    //更新员工提交记录
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("staffSchedule/update")
    Call<HttpResult<String>> insertRecord(@Header("Authorization") String token, @Body RequestBody info);

    //员工完成提交
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("staffSchedule/finishProgress")
    Call<HttpResult<String>> finishProgress(@Header("Authorization") String token, @Body RequestBody info);


    //员工新增提交记录
    @FormUrlEncoded
    @POST("staffSchedule/queryProgressByStaffId")
    Call<HttpResult<String>> queryProgressByStaffId(@Header("Authorization") String token,
                                                    @Field("id") String id,
                                                    @Field("pageSize") int pageSize,
                                                    @Field("pageNum") int pageNum);

    //模板履历-分页列表查询
    @FormUrlEncoded
    @POST("staffSchedule/queryResumeList")
    Call<HttpResult<String>> queryResumeList(@Header("Authorization") String token,
                                             @Field("checkId") String checkId,
                                             @Field("status") String status,
                                             @Field("pageSize") int pageSize,
                                             @Field("pageNum") int pageNum);

    //获取之前的作业记录
    @FormUrlEncoded
    @POST("staffSchedule/queryWorkRecord")
    Call<HttpResult<String>> queryWorkRecord(@Header("Authorization") String token,
                                             @Field("progressId") String progressId,
                                             @Field("groupId") String groupId,
                                             @Field("status") String status);

    //首工程员工扫码
    @FormUrlEncoded
    @POST("staffSchedule/insertStaffSchedule")
    Call<HttpResult<String>> insertStaffSchedule(@Header("Authorization") String token,
                                                 @Field("model") String name,
                                                 @Field("seriesNumber") String seriesNumber,
                                                 @Field("groupId") String groupId,
                                                 @Field("stationId") String stationId,
                                                 @Field("calculateNumber") String calculateNumber,
                                                 @Field("linkage") String linkage
    );


    @FormUrlEncoded
    @POST("staffSchedule/staffHomeworkExamination")
    Call<HttpResult<String>> staffHomeworkExamination(@Header("Authorization") String token,
                                                      @Field("checkId") String checkId,
                                                      @Field("groupId") String groupId,
                                                      @Field("stationId") String stationId);


    //查询正在作业中的员工
    @FormUrlEncoded
    @POST("staffSchedule/queryWorkerStaff")
    Call<HttpResult<String>> queryWorkerStaff(@Header("Authorization") String token,
                                              @Field("status") String status,
                                              @Field("checkId") String checkId);


    //APP检查更新-返回最新的一条数据
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("staffLogin/updateApp")
    Call<HttpResult<String>> updateApp(@Header("Authorization") String token);

    //pad管理信息
    @FormUrlEncoded
    @POST("pad/info")
    Call<HttpResult<String>> getPadMessage(@Field("padNo") String padNo);

    //添加PAD管理信息
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("pad/add")
    Call<HttpResult<String>> addPadDevice(@Body RequestBody info);


    //删除,编辑
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("pad/update")
    Call<HttpResult<String>> updatePadDevice(@Body RequestBody info);

    //组立线管理-列表查询
    @POST("staffLogin/queryGroupList")
    Call<HttpResult<String>> getGroupList(@Header("Authorization") String token);

    //工位列表-通过组立线进行查询
    @FormUrlEncoded
    @POST("staffLogin/queryStationList")
    Call<HttpResult<String>> getStationList(@Header("Authorization") String token,
                                            @Field("groupId") String groupId);


    //密钥确认  NG密钥 type==1   师带徒 type=2
    @FormUrlEncoded
    @POST("staffSchedule/secretKeyCheckV2")
    Call<HttpResult<String>> secretKeyCheck(@Header("Authorization") String token,
                                            @Field("type") String type,
                                            @Field("secretKey") String secretKey,
                                            @Field("groupId") String groupId
    );


    //扫码异常，通过型名以及模板编号获取检查表
    @FormUrlEncoded
    @POST("staffSchedule/queryCheckTemplateByModel")
    Call<HttpResult<String>> queryCheckTemplateByModel(@Header("Authorization") String token,
                                                       @Field("model") String model,
                                                       @Field("groupId") String groupId,
                                                       @Field("stationId") String stationId);

    //PAD-根据组立线获取模板编号
    @FormUrlEncoded
    @POST("staffSchedule/queryNumberByGroup")
    Call<HttpResult<String>> queryNumberByGroup(@Header("Authorization") String token,
                                                @Field("groupId") String groupId);


    //项目检查规则列表-正在作业也的员工获取作业规则
    @FormUrlEncoded
    @POST("staffSchedule/queryCheckRuleMap")
    Call<HttpResult<String>> queryCheckRuleMap(@Header("Authorization") String token,
                                               @Field("groupId") String groupId,
                                               @Field("stationId") String stationId);


    //异常日志
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("errorLog/add")
    Call<BaseResult> addErrorLog(@Body RequestBody info);


    //请求指图书
    @FormUrlEncoded
    @POST("transferWeb/queryWebService")
    Call<HttpResult<String>> queryWebService(@Field("param") String param);



    //指图书数据存储
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("pad/addRefersBook")
    Call<HttpResult<String>> addRefersBook(@Body RequestBody info);

}

