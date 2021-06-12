package com.myproject.sqlitedatabaseapp

import android.content.ContentValues
import android.content.DialogInterface
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.SimpleCursorAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var db:SQLiteDatabase
    lateinit var rs:Cursor
    lateinit var adapter: SimpleCursorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = DataBase(applicationContext)
        db = database.readableDatabase
        rs = db.rawQuery("SELECT * FROM ACTABLE ORDER BY NAME",null)


        registerForContextMenu(list_view)

        //Insert Button
        btn_Insert.setOnClickListener {
             var cv = ContentValues()
            cv.put("NAME",et_Name.text.toString())
            cv.put("MEANING",et_Meaning.text.toString())
            db.insert("ACTABLE",null,cv)
            rs.requery()

            var ad = AlertDialog.Builder(this)
            ad.setTitle("Add Record")
            ad.setMessage("Record Inserted Successfully....!")
            ad.setPositiveButton("OK",DialogInterface.OnClickListener { dialogInterface, i ->
                et_Name.setText("")
                et_Meaning.setText("")
                et_Name.requestFocus()
            })
            ad.show()
        }


          val adapter = SimpleCursorAdapter(applicationContext,
              android.R.layout.simple_expandable_list_item_2,rs,
            arrayOf("NAME","MEANING"),
            intArrayOf(android.R.id.text1,android.R.id.text2),0)
        list_view.adapter = adapter


        //View All Button
        btn_View_All.setOnClickListener {
            adapter.notifyDataSetChanged()
            //View All Data
            searchView.isIconified = false
            searchView.queryHint = "Search among ${rs.count} records"

            searchView.visibility = View.VISIBLE
            list_view.visibility = View.VISIBLE
        }


        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                rs = db.rawQuery(
                    "SELECT * FROM ACTABLE WHERE NAME LIKE '%${newText}' OR MEANING LIKE '${newText}'",
                    null)
                adapter.changeCursor(rs)
                return false
            }
        })


        //Clear Button
        btn_Clear.setOnClickListener {
            et_Name.setText("")
            et_Meaning.setText("")
            et_Name.requestFocus()
        }


        //Update Button
        btn_Update.setOnClickListener {
            var cv = ContentValues()
            cv.put("NAME",et_Name.text.toString())
            cv.put("MEANING",et_Meaning.text.toString())
            db.update("ACTABLE",cv,"_id = ?", arrayOf(rs.getString(0)))
            rs.requery()

            var ad = AlertDialog.Builder(this)
            ad.setTitle("Update Record")
            ad.setMessage("Record Updated Successfully....!")
            ad.setPositiveButton("OK",DialogInterface.OnClickListener { dialogInterface, i ->
                if(rs.moveToFirst()){
                    et_Name.setText(rs.getString(1))
                    et_Meaning.setText(rs.getString(2))
                }
            })
            ad.show()
        }


        //Delete Button
        btn_Delete.setOnClickListener {
            db.delete("ACTABLE","_id = ?", arrayOf(rs.getString(0)))
            rs.requery()

            var ad = AlertDialog.Builder(this)
            ad.setTitle("Delete Record")
            ad.setMessage("Record Deleted Successfully....!")
            ad.setPositiveButton("OK",DialogInterface.OnClickListener { dialogInterface, i ->
                if(rs.moveToFirst()){
                    et_Name.setText(rs.getString(1))
                    et_Meaning.setText(rs.getString(2))
                }
                else{
                    et_Name.setText("No Data Found")
                    et_Meaning.setText("No Data Found")
                }
            })
            ad.show()
        }

        //First Button
        btn_First.setOnClickListener{
            if(rs.moveToFirst()){
                et_Name.setText(rs.getString(1))
                et_Meaning.setText(rs.getString(2))
            }
            else
                Toast.makeText(applicationContext,"No Data Found",Toast.LENGTH_LONG).show()
        }


        //Next Button
        btn_Next.setOnClickListener{
            if(rs.moveToNext()){
                et_Name.setText(rs.getString(1))
                et_Meaning.setText(rs.getString(2))
            }
            else if (rs.moveToFirst()){
                et_Name.setText(rs.getString(1))
                et_Meaning.setText(rs.getString(2))
            }
        }


        //Previous Button
        btn_Previous.setOnClickListener {
            if(rs.moveToPrevious()){
                et_Name.setText(rs.getString(1))
                et_Meaning.setText(rs.getString(2))
            }
            else if (rs.moveToLast()){
                et_Name.setText(rs.getString(1))
                et_Meaning.setText(rs.getString(2))
            }
            else
                Toast.makeText(applicationContext,"No Data Found",Toast.LENGTH_LONG).show()
        }


        //Last Button
        btn_Last.setOnClickListener {
            if(rs.moveToLast()){
                et_Name.setText(rs.getString(1))
                et_Meaning.setText(rs.getString(2))
            }
            else
                Toast.makeText(applicationContext,"No Data Found",Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu?.add(1,1,1,"DELETE")
        menu?.setHeaderTitle("Removing Data")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if(item.itemId == 11){
            db.delete("ACTABLE","_id = ?", arrayOf(rs.getString(0)))
            rs.requery()
            adapter.notifyDataSetChanged()
            searchView.queryHint = "Search among ${rs.count} records"
        }
        return super.onContextItemSelected(item)
    }
}