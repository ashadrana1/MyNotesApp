package com.example.mynotes
//MY FINAL APP
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.Database.NoteDatabase
import com.example.mynotes.Models.Note
import com.example.mynotes.Models.NoteViewModel
import com.example.mynotes.adapter.NotesAdapter
import com.example.mynotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener, PopupMenu.OnMenuItemClickListener{

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote: Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if(result.resultCode == Activity.RESULT_OK){
            val note = result.data?.getSerializableExtra("note") as? Note

            if(note != null){
                viewModel.updateNote(note)
            }
            //The second Activity(i.e AddNote) can be called in two cases, 1st is whenever the user taps on the
            //floating action button mtlb user ko ek nya note bnana hai in that case,we want to call funcn from
            //line 76-84, and 2nd case is when user taps on an already existing note, which means he wants to
            //update the note, so in this case we will run code from line 27-40
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //for initializing the UI
        initUI()

        //viewModel always requires a viewmodelFactory(reference ke liye, RetrofitPost) wala project dekhliyo
        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        //yahan jo ye this hai, wo actually lifecycle owner ko refer krr rha hai
        //The lambda expression is executed whenever the observed LiveData (allnotes) is changed.
        viewModel.allnotes.observe(this){list->
            list?.let {
                adapter.updateList(list)
            }
        }

        //database ka instance bnaliya
        database = NoteDatabase.getDatabase(this)
    }


    private fun initUI() {

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        //for explanation of below, see 1:23:51
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as? Note
                if(note != null){
                    viewModel.insertNote(note)
                }
            }
        }

        binding.fbAddNote.setOnClickListener{
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //whenever user types anything inside the searchview, so this particular method will be called, and whenever
                //there is a change in the text, then we want to call the filterList method(in NotesAdapter)
                if(newText != null){
                    adapter.filterList(newText)
                }
                return true
            }

        })


    }

    override fun onItemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    override fun onDeleteMenuClicked(note: Note) {
        viewModel.deleteNote(note)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
        popup.menu.findItem(R.id.delete_note)?.setOnMenuItemClickListener {
            this.onDeleteMenuClicked(selectedNote)
            true
        }
    }

    //iss neeche wale funcn ko show krne ke liye ooper class mein comma lgakr ye likha"PopupMenu.OnMenuItemClickListener"
    //then class MainActivity ke neeche red line aane lgi, then hmne wahan option+return dabaya to ye neeche wale func
    //ka implementation aane lga
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.delete_note){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
        //ab hm apni AddNote wali activity mein jayenge
    }
}
