package org.atctech.schoolmanagementsystem.ApiRequest;

import org.atctech.schoolmanagementsystem.model.AllNotice;
import org.atctech.schoolmanagementsystem.model.ClassInformation;
import org.atctech.schoolmanagementsystem.model.CoursesTeacher;
import org.atctech.schoolmanagementsystem.model.LiveResults;
import org.atctech.schoolmanagementsystem.model.MessagesAll;
import org.atctech.schoolmanagementsystem.model.StudentDetails;
import org.atctech.schoolmanagementsystem.model.TeacherDetails;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Jacky on 2/28/2018.
 */

public interface ApiRequest {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> login(@Field("admin") String admin,@Field("password") String password);

    @FormUrlEncoded
    @POST("change_password.php")
    Call<ResponseBody> updatePassword(@Field("u_id") String id,@Field("password") String password);

    @FormUrlEncoded
    @POST("pro_details.php")
    Call<StudentDetails> getStudentDetails(@Field("id") String id);

    @GET("all_teacher.php")
    Call<List<TeacherDetails>> getAllTeacher();

    @FormUrlEncoded
    @POST("book.php")
    Call<List<LiveResults>> getResult(@Field("id") String id);

    @FormUrlEncoded
    @POST("all_teachers.php")
    Call<List<CoursesTeacher>> getAllCourseTeacher(@Field("class") String classID);

    @FormUrlEncoded
    @POST("teaching_evo.php")
    Call<ResponseBody> submitTeachingEvo(@Field("u_id") String id,@Field("t_name") String t_name,@Field("teaching") String teaching,@Field("listening") String listening,
                                         @Field("t_behave") String t_behave,@Field("writing") String writing,@Field("cl_attend") String cl_attend,
                                         @Field("comment") String comment);


    @FormUrlEncoded
    @POST("classes_subject.php")
    Call<ClassInformation> allClassesInformation(@Field("id") String id);

    @FormUrlEncoded
    @POST("messege.php")
    Call<ResponseBody> sendMessage(@Field("student_id") String id,
                                   @Field("subject") String subject,
                                   @Field("message") String message);
    @FormUrlEncoded
    @POST("chatScript.php")
    Call<List<MessagesAll>> getAllMessages(@Field("student_id") String studentId);


    @FormUrlEncoded
    @POST("receievelocation.php")
    Call<ResponseBody> sendLocation(@Field("u_id") String id,@Field("latitude") Double latitude,@Field("longitude") Double longitude);

    @GET("notice.php")
    Call<List<AllNotice>> getAllStudentNotice();
}
