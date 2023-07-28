package com.example.mynotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.Models.Note
import com.example.mynotes.R
import kotlin.random.Random

class NotesAdapter(private val context: Context, val listener:NotesClickListener) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    //this is the arrayList which we are going to show in a given point of time in recycler view
    private val NotesList = ArrayList<Note>()

    //this list contain all the elements which we are going to fetch from our database
    private val fullList = ArrayList<Note>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = NotesList[position]

        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.Note_tv.text = currentNote.note

        holder.date.text = currentNote.date
        holder.date.isSelected = true
//to change the background color of every every diffeerently
        holder.notes_layout.setCardBackgroundColor(holder.itemView.resources.getColor(randomColor(), null))

        //ab yahan mai apna NotesClickListener ko likhne ke baad aaya huu
        holder.notes_layout.setOnClickListener{
            listener.onItemClicked(NotesList[holder.adapterPosition])
        }
        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClicked(NotesList[holder.adapterPosition], holder.notes_layout)
            true // Return true to indicate that the long click is consumed and no further actions should happen.
        }


    }
    //ab mai ooper ka onclickListener likhne ke baad yahan se hi continue krr rha huu
    fun updateList(newList: List<Note>){
        //so whenever user adds a note/calls this method so we want create the fullList(line 21) and set this fullList
        //as our new List

        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)

        notifyDataSetChanged()
    }

    //jb userSearchbar mein kuch type krke dhoondne ki koshish krega
    fun filterList(search: String){

        NotesList.clear()

        for(item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true){
                NotesList.add(item)
            }
        }

        // ab firse recyclerview mein change huawa data show krna hai, to isiliye notify krwadiya usse
        notifyDataSetChanged()

        //now we go to our mainActivity
    }


    fun randomColor(): Int{
        val list = ArrayList<Int>()

        list.add(R.color.colorAccent)
        list.add(R.color.blue)
        list.add(R.color.gray)
        list.add(R.color.greenDark)
        list.add(R.color.orange)
        list.add(R.color.pink)
        list.add(R.color.purple)
        list.add(R.color.red)

        //ab jb bhi randomColor call hoga, to hmko ek random no. btana hai isme se
        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        //as time is continuously changing, isiliye hmne apna seed hi time ko bnaliya means everytime randomColor func
        //call hoga to fir time ki value i.e seed change hogya hoga and hmko ek nya randomIndex milega

        return list[randomIndex]
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val Note_tv = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }

    interface NotesClickListener{
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)

        fun onDeleteMenuClicked(note: Note)
    }
}
