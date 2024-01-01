package com.arincdogansener.finalrecipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arincdogansener.finalrecipeapp.adapter.MainCategoryAdapter
import com.arincdogansener.finalrecipeapp.adapter.SubCategoryAdapter
import com.arincdogansener.finalrecipeapp.database.RecipeDatabase
import com.arincdogansener.finalrecipeapp.databinding.ActivityHomeBinding
import com.arincdogansener.finalrecipeapp.entities.Category
import com.arincdogansener.finalrecipeapp.entities.CategoryItems
import com.arincdogansener.finalrecipeapp.entities.MealsItems
import com.arincdogansener.finalrecipeapp.entities.Recipies
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {

    private lateinit var binding : ActivityHomeBinding
    var arrMainCategory = ArrayList<CategoryItems>()
    var arrSubCategory = ArrayList<MealsItems>()

    var mainCategoryAdapter = MainCategoryAdapter()
    var subCategoryAdapter = SubCategoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        getDataFromDb()

        mainCategoryAdapter.setClickListener(onCLicked)
        subCategoryAdapter.setClickListener(onCLickedSubItem)


    }

    private val onCLicked  = object : MainCategoryAdapter.OnItemClickListener{
        override fun onClicked(categoryName: String) {
            getMealDataFromDb(categoryName)
        }
    }

    private val onCLickedSubItem  = object : SubCategoryAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(this@HomeActivity,DetailActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }
    }

    private fun getDataFromDb(){
        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getAllCategory()
                arrMainCategory = cat as ArrayList<CategoryItems>
                arrMainCategory.reverse()
                getMealDataFromDb(arrMainCategory[0].strCategory)
                mainCategoryAdapter.setData(arrMainCategory)
                binding.rvMainCategory.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL, false)
                binding.rvMainCategory.adapter = mainCategoryAdapter
            }


        }
    }

    private fun getMealDataFromDb(categoryName:String){
        binding.tvCategory.text = "$categoryName Category"

        launch {
            this.let {
                var cat = RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getSpecificMealList(categoryName)
                arrSubCategory = cat as ArrayList<MealsItems>
                subCategoryAdapter.setData(arrSubCategory)
                binding.rvSubCategory.layoutManager = LinearLayoutManager(this@HomeActivity,LinearLayoutManager.HORIZONTAL,false)
                binding.rvSubCategory.adapter = subCategoryAdapter
            }
        }
    }
}

//class HomeActivity : BaseActivity(), FragmentActivity() {
//
//    private lateinit var binding: ActivityHomeBinding
//    private lateinit var gestureDetector: GestureDetector
//    private lateinit var workManager: WorkManager
//    private lateinit var workRequest: OneTimeWorkRequest
//    private lateinit var customWorker: CustomWorker
//    lateinit var bottomFragment :BottomFragment
//    private var arrMainCategory = ArrayList<CategoryItems>()
//    private var arrSubCategory = ArrayList<MealsItems>()
//    private var mainCategoryAdapter = MainCategoryAdapter()
//    private var subCategoryAdapter = SubCategoryAdapter()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityHomeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        gestureDetector = GestureDetector(this, MyGestureListener())
//        workManager = WorkManager.getInstance(this)
//
//        // Set up RecyclerView adapters and click listeners
//        mainCategoryAdapter.setClickListener(onClicked)
//        subCategoryAdapter.setClickListener(onClickedSubItem)
//
//        // Set up RecyclerViews
//        binding.rvMainCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.rvMainCategory.adapter = mainCategoryAdapter
//
//        binding.rvSubCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        binding.rvSubCategory.adapter = subCategoryAdapter
//
//        // Set up gesture detection on rvMainCategory
//        binding.rvMainCategory.setOnTouchListener { _, event ->
//            gestureDetector.onTouchEvent(event)
//        }
//
//        // Set up WorkManager to execute background task
//        binding.btnStartService.setOnClickListener {
//            enqueueWorkManager()
//        }
//
//        // Fetch data from Room Database
//        getDataFromDb()
//    }
//
//    private val onClicked = object : MainCategoryAdapter.OnItemClickListener {
//        override fun onClicked(categoryName: String) {
//            getMealDataFromDb(categoryName)
//        }
//    }
//
//    private val onClickedSubItem = object : SubCategoryAdapter.OnItemClickListener {
//        override fun onClicked(id: String) {
//            val intent = Intent(this@HomeActivity, DetailActivity::class.java)
//            intent.putExtra("id", id)
//            startActivity(intent)
//        }
//    }
//
//    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {
//        companion object {
//            private const val SWIPE_THRESHOLD = 100
//            private const val SWIPE_VELOCITY_THRESHOLD = 100
//        }
//
//        override fun onFling(
//            e1: MotionEvent?,
//            e2: MotionEvent?,
//            velocityX: Float,
//            velocityY: Float
//        ): Boolean {
//            if (e1 != null && e2 != null) {
//                val deltaX = e2.x - e1.x
//                val deltaY = e2.y - e1.y
//
//                if (Math.abs(deltaX) > Math.abs(deltaY)) {
//                    if (Math.abs(deltaX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                        // Swipe to the right
//                        if (deltaX > 0) {
//                            navigateToPreviousCategory()
//                        }
//                        // Swipe to the left
//                        else {
//                            navigateToNextCategory()
//                        }
//                    }
//                }
//            }
//
//            return true
//        }
//    }
//
//    private fun enqueueWorkManager() {
//        // Create work request and enqueue it with WorkManager
//        workRequest = OneTimeWorkRequest.Builder(CustomWorker::class.java)
//            .setInputData(Data.Builder().putInt("num", 10).putString("name", "nese").build())
//            .build()
//
//        workManager.enqueue(workRequest)
//
//        // Observe the result of the background task
//        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(this, { workInfo ->
//            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
//                val resultData: Data = workInfo.outputData
//                showToast("SUCCEEDED ${resultData.getInt("result", 0)}")
//            }
//        })
//    }
//
//    private fun navigateToPreviousCategory() {
//        if (arrMainCategory.isNotEmpty() && arrMainCategory.size > 1) {
//            val currentIndex =
//                arrMainCategory.indexOfFirst { it.strcategory == binding.tvCategory.text.toString() }
//            val previousIndex =
//                (currentIndex - 1 + arrMainCategory.size) % arrMainCategory.size
//            getMealDataFromDb(arrMainCategory[previousIndex].strcategory)
//        }
//    }
//
//    private fun navigateToNextCategory() {
//        if (arrMainCategory.isNotEmpty() && arrMainCategory.size > 1) {
//            val currentIndex =
//                arrMainCategory.indexOfFirst { it.strcategory == binding.tvCategory.text.toString() }
//            val nextIndex = (currentIndex + 1) % arrMainCategory.size
//            getMealDataFromDb(arrMainCategory[nextIndex].strcategory)
//        }
//    }
//
//    private fun getDataFromDb() {
//        launch {
//            val cat =
//                RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getAllCategory()
//            arrMainCategory = cat as ArrayList<CategoryItems>
//            arrMainCategory.reverse()
//            getMealDataFromDb(arrMainCategory[0].strcategory)
//            mainCategoryAdapter.setData(arrMainCategory)
//        }
//    }
//
//    private fun getMealDataFromDb(categoryName: String) {
//        binding.tvCategory.text = "$categoryName Category"
//        launch {
//            val cat =
//                RecipeDatabase.getDatabase(this@HomeActivity).recipeDao().getSpecificMealList(categoryName)
//            arrSubCategory = cat as ArrayList<MealsItems>
//            subCategoryAdapter.setData(arrSubCategory)
//        }
//    }
//}