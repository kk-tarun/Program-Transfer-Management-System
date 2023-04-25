package com.example.ptms

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class FrontPage : AppCompatActivity(),SelectListener{
    private lateinit var transfer_btn:Button
    private lateinit var recyclerView: RecyclerView
    private var arrProgram = ArrayList<Model>()
    private lateinit var searchView: SearchView
    private lateinit var adapter:ProgramAdapter
    private lateinit var tv:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front_page)

        searchView = findViewById(R.id.search_view)
        searchView.queryHint ="Search Program ..."

        recyclerView = findViewById<RecyclerView>(R.id.program_list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProgramAdapter(createArrayList(),  this )
        recyclerView.adapter = adapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        tv = findViewById(R.id.selected_program)
        transfer_btn = findViewById(R.id.transfer_button)

        transfer_btn.isEnabled = false
        transfer_btn.setBackgroundColor(Color.parseColor("grey"));

        transfer_btn.setOnClickListener{
            var i = Intent(this,Guidelines::class.java)
            i.putExtra("selected_program",tv.text.toString())
            startActivity(i)
        }
    }
    fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<Model>()
            for (i in arrProgram) {
                if (i.getTitle()?.lowercase(Locale.ROOT)!!.contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.setFilteredList(filteredList)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, title: String)
    }

    private fun createArrayList(): ArrayList<Model> {
//        val arrProgram = ArrayList<Model>()
        val obj = Model()

        obj.setTitle("Computer Science and Engineering")
        obj.setImage(R.drawable.baseline_computer)
        obj.setDesc(getString(R.string.cse))
        arrProgram.add(obj)

        val obj1 = Model()
        obj1.setTitle("Mechanical Engineering")
        obj1.setImage(R.drawable.mech)
        obj1.setDesc(getString(R.string.mech))
        arrProgram.add(obj1)

        val obj2 = Model()
        obj2.setTitle("Electronics and Communication Engineering")
        obj2.setImage(R.drawable.electronics)
        obj2.setDesc(getString(R.string.ece))
        arrProgram.add(obj2)

        val obj3 = Model()
        obj3.setTitle("Civil Engineering")
        obj3.setImage(R.drawable.civil)
        obj3.setDesc(getString(R.string.civil))
        arrProgram.add(obj3)

        val obj4 = Model()
        obj4.setTitle("Electrical Engineering")
        obj4.setImage(R.drawable.electrical)
        obj4.setDesc(getString(R.string.elec))
        arrProgram.add(obj4)

        val obj5 = Model()
        obj5.setTitle("Aeronautical Engineering")
        obj5.setImage(R.drawable.aero)
        obj5.setDesc(getString(R.string.Aero))
        arrProgram.add(obj5)

        val obj6 = Model()
        obj6.setTitle("Biotechnology Engineering")
        obj6.setImage(R.drawable.bio)
        obj6.setDesc(getString(R.string.bio))
        arrProgram.add(obj6)
        return arrProgram
    }

    override fun onItemClicked(model: Model?) {
        tv.text = model?.getTitle()
        transfer_btn.isEnabled = true
        transfer_btn.setBackgroundColor(Color.parseColor("#8692f7"))
//        Toast.makeText(this,model?.getTitle(),Toast.LENGTH_SHORT).show()
    }

}