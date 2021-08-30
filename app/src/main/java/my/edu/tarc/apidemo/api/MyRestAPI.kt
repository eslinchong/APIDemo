package my.edu.tarc.apidemo.api

import my.edu.tarc.apidemo.model.ReusltRespond
import my.edu.tarc.apidemo.model.studentRespond
import retrofit2.Call
import retrofit2.http.*

interface MyRestAPI {

    companion object{

        val BASE_URL:String = "http://demo.onmyfinger.com/"
    }
    @GET("home/getAll")
    fun getAll () : Call <List<studentRespond>>

    @GET("home/getbyid")
    fun getById (@Query("id")id:String) : Call <studentRespond>

    @POST("home/Add")
    @FormUrlEncoded
    fun add(
        @Field("id")id: String,
        @Field("name")name:String,
        @Field("programme") programme:String,
        @Field("img")img:String
    ):Call <ReusltRespond>
}