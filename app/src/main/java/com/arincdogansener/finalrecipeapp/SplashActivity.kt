package com.arincdogansener.finalrecipeapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arincdogansener.finalrecipeapp.database.RecipeDatabase
import com.arincdogansener.finalrecipeapp.databinding.ActivitySplashBinding
import com.arincdogansener.finalrecipeapp.entities.Category
import com.arincdogansener.finalrecipeapp.entities.Meal
import com.arincdogansener.finalrecipeapp.entities.MealsItems
import com.arincdogansener.finalrecipeapp.interfaces.GetDataService
import com.arincdogansener.finalrecipeapp.retrofitclient.RetrofitClientInstance
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity(), EasyPermissions.RationaleCallbacks, EasyPermissions.PermissionCallbacks{
    private lateinit var binding : ActivitySplashBinding
    private var READ_STORAGE_PERM = 123
    private val STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        readStorageTask()
        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
            println("pressed")
            finish()
        }
    }

    fun getCategories(){
        val service = RetrofitClientInstance.retrofitInstance.create(GetDataService::class.java)
        val call = service.getCategoryList()
        call.enqueue(object : Callback<Category>{
            override fun onResponse(
                call: Call<Category>,
                response: Response<Category>
            ) {
                for (arr in response.body()!!.categoryitems!!) {
                    getMeal(arr.strCategory)
                }
                insertDataIntoRoomDb(response.body())
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                binding.loader.visibility = View.INVISIBLE
                Toast.makeText(this@SplashActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getMeal(categoryName: String) {
        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
        val call = service.getMealList(categoryName)
        call.enqueue(object : Callback<Meal> {
            override fun onFailure(call: Call<Meal>, t: Throwable) {

                binding.loader.visibility = View.INVISIBLE
                Toast.makeText(this@SplashActivity, "Something went wrong", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onResponse(
                call: Call<Meal>,
                response: Response<Meal>
            ) {

                insertMealDataIntoRoomDb(categoryName, response.body())
            }

        })
    }

    fun insertDataIntoRoomDb(category: Category?){
        launch {
            this.let {

                for (arr in category!!.categoryitems!!){
                    RecipeDatabase.getDatabase(this@SplashActivity)
                        .recipeDao().insertCategory(arr)
                }
                //binding.btnGetStarted.visibility = View.VISIBLE
            }
        }
    }

    fun insertMealDataIntoRoomDb(categoryName: String, meal: Meal?) {

        launch {
            this.let {


                for (arr in meal!!.mealsItem!!) {
                    var mealItemModel = MealsItems(
                        arr.id,
                        arr.idMeal,
                        categoryName,
                        arr.strMeal,
                        arr.strMealThumb
                    )
                    RecipeDatabase.getDatabase(this@SplashActivity)
                        .recipeDao().insertMeal(mealItemModel)
                    Log.d("mealData", arr.toString())
                }

                binding.btnGetStarted.visibility = View.VISIBLE
            }
        }


    }

    fun clearDataBase() {
        launch {
            this.let {
                RecipeDatabase.getDatabase(this@SplashActivity).recipeDao().clearDb()
            }
        }
    }

    private fun hasReadStoragePermission():Boolean{
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun readStorageTask() {
        if (hasReadStoragePermission()) {
            clearDataBase()
            getCategories()
        }
        else{
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your storage,",
                READ_STORAGE_PERM,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }

//    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            AppSettingsDialog.Builder(this).build().show()
//        }
//    }
override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    if (requestCode == READ_STORAGE_PERM) {
        // İzin verildiyse, işlemleri gerçekleştir
        clearDataBase()
        getCategories()
    }
}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == READ_STORAGE_PERM) {
            // İzin verilmediyse, kullanıcıya uygun bir mesaj gösterilebilir
            Toast.makeText(this, "Storage permission denied durumu söz konusu", Toast.LENGTH_SHORT).show()
        }

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // İzin kalıcı olarak reddedildiyse, kullanıcıyı ayarlara yönlendirebilirsiniz
            AppSettingsDialog.Builder(this).build().show()
        }
    }
}
//@SuppressLint("CustomSplashScreen")
//class SplashActivity : BaseActivity(), EasyPermissions.RationaleCallbacks,
//    EasyPermissions.PermissionCallbacks {
//    private lateinit var binding : ActivitySplashBinding
//    private var READ_STORAGE_PERM = 123
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
//
//        readStorageTask()
//
//        binding.btnGetStarted.setOnClickListener {
//            var intent = Intent(this@SplashActivity, HomeActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
//
//
//    fun getCategories() {
//        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
//        val call = service.getCategoryList()
//        call.enqueue(object : Callback<Category> {
//            override fun onFailure(call: Call<Category>, t: Throwable) {
//
//                Toast.makeText(this@SplashActivity, "Something went wrong", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onResponse(
//                call: Call<Category>,
//                response: Response<Category>
//            ) {
//
//                for (arr in response.body()!!.categoryitems!!) {
//                    getMeal(arr.strCategory)
//                }
//                insertDataIntoRoomDb(response.body())
//            }
//
//        })
//    }
//
//
//    fun getMeal(categoryName: String) {
//        val service = RetrofitClientInstance.retrofitInstance!!.create(GetDataService::class.java)
//        val call = service.getMealList(categoryName)
//        call.enqueue(object : Callback<Meal> {
//            override fun onFailure(call: Call<Meal>, t: Throwable) {
//
//                binding.loader.visibility = View.INVISIBLE
//                Toast.makeText(this@SplashActivity, "Something went wrong", Toast.LENGTH_SHORT)
//                    .show()
//            }
//
//            override fun onResponse(
//                call: Call<Meal>,
//                response: Response<Meal>
//            ) {
//
//                insertMealDataIntoRoomDb(categoryName, response.body())
//            }
//
//        })
//    }
//
//    fun insertDataIntoRoomDb(category: Category?) {
//
//        launch {
//            this.let {
//
//                for (arr in category!!.categoryitems!!) {
//                    RecipeDatabase.getDatabase(this@SplashActivity)
//                        .recipeDao().insertCategory(arr)
//                }
//            }
//        }
//
//
//    }
//
//    fun insertMealDataIntoRoomDb(categoryName: String, meal: Meal?) {
//
//        launch {
//            this.let {
//
//
//                for (arr in meal!!.mealsItem!!) {
//                    var mealItemModel = MealsItems(
//                        arr.id,
//                        arr.idMeal,
//                        categoryName,
//                        arr.strMeal,
//                        arr.strMealThumb
//                    )
//                    RecipeDatabase.getDatabase(this@SplashActivity)
//                        .recipeDao().insertMeal(mealItemModel)
//                    Log.d("mealData", arr.toString())
//                }
//
//                binding.btnGetStarted.visibility = View.VISIBLE
//            }
//        }
//
//
//    }
//
//    fun clearDataBase() {
//        launch {
//            this.let {
//                RecipeDatabase.getDatabase(this@SplashActivity).recipeDao().clearDb()
//            }
//        }
//    }
//
//    private fun hasReadStoragePermission(): Boolean {
//        return EasyPermissions.hasPermissions(
//            this,
//            android.Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//    }
//
//    private fun readStorageTask() {
//        if (hasReadStoragePermission()) {
//            clearDataBase()
//            getCategories()
//        } else {
//            EasyPermissions.requestPermissions(
//                this,
//                "This app needs access to your storage,",
//                READ_STORAGE_PERM,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
//    }
//
//    override fun onRationaleDenied(requestCode: Int) {
//
//    }
//
//    override fun onRationaleAccepted(requestCode: Int) {
//
//    }
//
//    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
//            AppSettingsDialog.Builder(this).build().show()
//        }
//    }
//
//    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//
//    }
//}