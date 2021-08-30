package my.edu.tarc.apidemo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import my.edu.tarc.apidemo.api.RetrofitInstance
import my.edu.tarc.apidemo.model.ReusltRespond
import my.edu.tarc.apidemo.model.studentRespond
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var  btnGet: Button
    private lateinit var  btnAdd :Button
    private lateinit var  btnLoad :Button
    private lateinit var btnBrowse:Button

    private lateinit var img : ImageView
    private var imgUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGet = findViewById(R.id.btnGet)
        btnAdd = findViewById(R.id.btnAdd)
        btnLoad = findViewById(R.id.btnLoad)
        btnBrowse = findViewById(R.id.btnBrowse)
        img = findViewById(R.id.imgProfile)

        btnBrowse.setOnClickListener(){
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            launchSomeActivity.launch(intent)
        }


        btnGet.setOnClickListener(){
            val call = RetrofitInstance.api.getAll()

            call.enqueue(object: Callback<List<studentRespond>> {
                override fun onResponse(
                    call: Call<List<studentRespond>>,
                    response: Response<List<studentRespond>>
                ) {
                    val rs = response.body()
                    var strNameList = ""

                    for(student:studentRespond in rs!!){
                        strNameList += "${student.id} ${student.name}\n"
                    }

                    val tvResult :TextView = findViewById(R.id.tvResult)
                    tvResult.text = strNameList
                }

                override fun onFailure(call: Call<List<studentRespond>>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }


            })
        }
        btnLoad.setOnClickListener(){
            val call = RetrofitInstance.api.getById("W099")

            call.enqueue(object: Callback<studentRespond>{
                override fun onResponse(
                    call: Call<studentRespond>,
                    response: Response<studentRespond>
                ) {
                    val rs = response.body()

                    val tfId : TextView = findViewById(R.id.tfID)
                    val tfName : TextView = findViewById(R.id.tfName)
                    val tfProgramme : TextView = findViewById(R.id.tfProgramme)

                    tfId.text = rs?.id
                    tfName.text = rs?.name
                    tfProgramme.text = rs?.programme

                    Glide.with(img.context).load(rs?.imgURL).into(img)
                }

                override fun onFailure(call: Call<studentRespond>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

            })
        }
        btnAdd.setOnClickListener(){
            val bitmap = (img.getDrawable() as BitmapDrawable).bitmap
            val byteArray: ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
            val strImg: String = Base64.encodeToString(byteArray.toByteArray(), Base64.DEFAULT)

            val tfId : TextView = findViewById(R.id.tfID)
            val tfName : TextView = findViewById(R.id.tfName)
            val tfProgramme : TextView = findViewById(R.id.tfProgramme)

            val id = tfId.text.toString()
            val name = tfName.text.toString()
            val programme = tfProgramme.text.toString()

            val call = RetrofitInstance.api.add(id, name, programme, strImg)
            call.enqueue(object: Callback<ReusltRespond> {
                override fun onResponse(
                    call: Call<ReusltRespond>,
                    response: Response<ReusltRespond>
                ) {
                    val rs = response.body()

                    Toast.makeText(applicationContext, rs?.message, Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<ReusltRespond>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }
            })

        }
    }

    var launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            imgUri  = data?.data
            img.setImageURI(data?.data)
        }
    }

}