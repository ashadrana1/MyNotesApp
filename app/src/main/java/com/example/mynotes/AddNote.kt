package com.example.mynotes

import android.app.Activity
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mynotes.Models.Note
import com.example.mynotes.databinding.ActivityAddNoteBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date

class AddNote : AppCompatActivity() {

    lateinit var binding: ActivityAddNoteBinding
    private lateinit var note: Note
    private lateinit var old_note: Note
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(old_note.title)
            binding.etNote.setText(old_note.note)
            isUpdate = true
            //ab agr ye try block theek se execute hua mtlb ki fir changes krne ke baad user will click on "right icon"
            //which means we have to update the note
        }catch (e:Exception){
            e.printStackTrace()
        }

        binding.imgCheck.setOnClickListener{
            //if he clicks here so either he have updated an old note or created a new Note
            val title = binding.etTitle.text.toString()
            val note_desc = binding.etNote.text.toString()

            if(title.isNotEmpty() || note_desc.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")

                if(isUpdate){
                    //means here we have updated the note as isUpdate to bydefault false hi tha, but if it is true means
                    //try block run hua hai
                    note = Note(old_note.id, title, note_desc, formatter.format(Date()))
                }
                else{
                     note = Note(null,title,note_desc,formatter.format(Date()))
                }

                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            else{
                Toast.makeText(this@AddNote, "Please Enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        binding.imgBackArrow.setOnClickListener{
            onBackPressed()
        }
    }
}
