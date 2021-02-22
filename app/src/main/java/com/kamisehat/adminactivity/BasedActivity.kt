package com.kamisehat.adminactivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.kamisehat.adminactivity.databinding.ActivityBasedBinding

class BasedActivity : AppCompatActivity() {
    private val mFirestore = FirebaseFirestore.getInstance()
    var db = FirebaseFirestore.getInstance()
    private val collectionWisata = mFirestore.collection("Wisata")

    private lateinit var binding: ActivityBasedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_based)

        binding = ActivityBasedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        showData()
    }

    private fun initView() {
        binding.rvFiredb.apply {
            setHasFixedSize(true)
        }
        binding.fabFiredb.setOnClickListener {
            //berpindah ke activity AddEditActivity untuk tambah data
            startActivity(Intent(this, AddActivity::class.java).apply {
                putExtra(AddActivity.REQ_EDIT, false)
            })
        }
    }

    private fun showData() {
        db.collection("Wisata").get()
            .addOnSuccessListener {
                populateData(it.toObjects(ModelWisata::class.java))
            }.addOnFailureListener {

            }
    }

    private fun populateData(list: MutableList<ModelWisata>) {
        binding.rvFiredb.layoutManager = LinearLayoutManager(this)
        val gridDataAdapter = AdapterAdmin(this,list)
        binding.rvFiredb.adapter = gridDataAdapter
    }

}