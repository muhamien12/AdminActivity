package com.kamisehat.adminactivity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.kamisehat.adminactivity.databinding.ItemListBinding

class       AdapterAdmin(
    private val context: Context,
    private val listData: List<ModelWisata>
) :
    RecyclerView.Adapter<AdapterAdmin.ViewHolder>() {

    private lateinit var binding: ItemListBinding

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterAdmin.ViewHolder {
        binding = ItemListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(
            binding.root
        )
    }

    override fun onBindViewHolder(holder: AdapterAdmin.ViewHolder, position: Int) {
        val wisata = listData[position]

        binding.apply {
            val name = "Nama   : ${wisata.NamaWisata}"
            tvName.text = name
            contentMain.setOnClickListener {
                showDialogMenu(wisata)
            }
        }
    }

    override fun getItemCount(): Int {
        return listData.size

    }

    private fun showDialogMenu(wisata: ModelWisata) {
        //dialog popup edit hapus
        val builder =
            AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        val option = arrayOf("Edit", "Hapus")
        builder.setItems(option) { dialog, which ->
            when (which) {
                //0 -> untuk berpindah ke activity AddEditActivity untuk edit dengan membawa data
                0 -> context.startActivity(Intent(context, AddActivity::class.java).apply {
                    putExtra(AddActivity.REQ_EDIT, true)
                    putExtra(AddActivity.EXTRA_DATA, wisata)
                })
                1 -> showDialogDel(wisata.strId?:"")
            }
        }
        builder.create().show()
    }


    private fun showDialogDel(strId: String) {
        //dialog pop delete
        val builder =
            AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Hapus Data")
                .setMessage("Yakin mau hapus?")
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    deleteById(strId)
                }
                .setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

    private fun deleteById(id: String) {
        Log.d("TAG", "deleteById: $id")
        //menghapus data berdasarkan id
        FirebaseFirestore.getInstance().collection("Wisata")
            .document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Succes Hapus data", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "deleteById: success")
            }
            .addOnFailureListener {
                Toast.makeText(context, "Gagal Hapus data", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "deleteById: failed")
            }
    }

}