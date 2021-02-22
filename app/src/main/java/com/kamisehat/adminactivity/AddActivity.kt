package com.kamisehat.adminactivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kamisehat.adminactivity.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    private var isEdit = false
    private var wisata: ModelWisata? = null
    private val mFirestore = FirebaseFirestore.getInstance()
    //private val mUsersCollection = mFirestore.collection(PATH_COLLECTION)


    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isEdit = intent.getBooleanExtra(REQ_EDIT, false)
        wisata = intent.getParcelableExtra(EXTRA_DATA) ?: ModelWisata()

        if (wisata != null && wisata?.strId != null) populateEditData(wisata!!)

        binding.btnSave.setOnClickListener {
            if (isEdit) updateData() else createWisata()
        }
        initView()
    }

    private fun updateData() {
        val DocumentReference: DocumentReference = mFirestore.collection("Wisata").document()
        with(binding) {
            val data = ModelWisata(
                tvNama.text.toString(),
                tvDeskripsi.text.toString(),
                tvGambar.text.toString(),
                tvAddress.text.toString(),
                tilLat.text.toString(),
                tilLong.text.toString(),
                wisata?.strId
            )
            Log.d("TAG", "updateData: $data ")
            FirebaseFirestore.getInstance().collection("Wisata")
                .document(wisata?.strId ?: "")
                .update(data.mapped())
                .addOnSuccessListener {
                    startActivity(
                        Intent(
                            this@AddActivity,
                            BasedActivity::class.java
                        )
                    )
                }
                .addOnFailureListener { Log.d("TAG", "updateData: failed = ${it.message}") }
        }


        // TODO: 2/21/2021 get data
        // TODO: 2/21/2021 firestore update pake data di mapped
    }

    private fun populateEditData(wisata: ModelWisata) {
        with(binding) {
            tvNama.setText(wisata.NamaWisata)
            tvDeskripsi.setText(wisata.Deskripsi)
            tvAddress.setText(wisata.Alamat)
            tvGambar.setText(wisata.Gambar)
            tilLat.setText(wisata.lat.toString())
            tilLong.setText(wisata.long.toString())

        }
    }

    private fun initView() {
        //set view jika data di edit maka akan tampil pada form input
        if (isEdit) {
            binding.btnSave.text = getString(R.string.update)
            binding.tvNama.text = Editable.Factory.getInstance().newEditable(wisata?.NamaWisata)
            binding.tvDeskripsi.text = Editable.Factory.getInstance().newEditable(wisata?.Deskripsi)
            binding.tvAddress.text = Editable.Factory.getInstance().newEditable(wisata?.Alamat)
            binding.tvGambar.text = Editable.Factory.getInstance().newEditable(wisata?.Gambar)
            binding.tilLat.text = Editable.Factory.getInstance().newEditable(wisata?.lat)
            binding.tilLong.text = Editable.Factory.getInstance().newEditable(wisata?.long)
        }
    }
/*
    private fun saveData() {
        setData()
    }

    private fun setData() {
        createWisata(strId).addOnCompleteListener {
            if (it.isSuccessful) {
                if (isEdit) Toast.makeText(this, "Sukses tambah data", Toast.LENGTH_SHORT).show()
                else Toast.makeText(this, "Sukses perbarui data", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this@AddActivity, BasedActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Gagal tambah data", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error Added data ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
*/

    private fun createWisata() {
        with(binding) {
            val data = ModelWisata(
                tvNama.text.toString(),
                tvDeskripsi.text.toString(),
                tvGambar.text.toString(),
                tvAddress.text.toString(),
                tilLat.text.toString(),
                tilLong.text.toString(),
                wisata?.strId
            )
            Log.d("TAG", "updateData: $data ")
            FirebaseFirestore.getInstance().collection("Wisata")
                .document()
                .set(data.mapped())
                .addOnSuccessListener {
                    startActivity(
                        Intent(
                            this@AddActivity,
                            BasedActivity::class.java
                        )
                    )
                }
                .addOnFailureListener { Log.d("TAG", "SaveData: failed = ${it.message}") }
        }
    }

    companion object {
        //key untuk intent data"
        const val EXTRA_DATA = "extra_data"
        const val REQ_EDIT = "req_edit"
    }
}

private fun ModelWisata.mapped(): Map<String, Any?> = mapOf(
    "NamaWisata" to this.NamaWisata,
    "Deskripsi" to this.Deskripsi,
    "Gambar" to this.Gambar,
    "Alamat" to this.Alamat,
    "lat" to this.lat,
    "long" to this.long

)
