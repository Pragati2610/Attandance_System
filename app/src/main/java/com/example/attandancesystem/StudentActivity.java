package com.example.attandancesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class StudentActivity extends AppCompatActivity {

    Toolbar toolbar;
    private String className;
    private String section;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private  DbHelper dbHelper;
    private long cid;
    private MyCalendar myCalendar;
    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        myCalendar = new MyCalendar();
        toolbar = findViewById(R.id.toolbar);
        dbHelper = new DbHelper(this);

        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        section = intent.getStringExtra("section");
        position = intent.getIntExtra("position",-1);
        cid = intent.getLongExtra("cid", -1);

        setToolbar();
        loadData();

        recyclerView = findViewById(R.id.student_rcv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        studentAdapter = new StudentAdapter(this,studentItems);
        recyclerView.setAdapter(studentAdapter);
        studentAdapter.setOnItemClickListener(position->changeStatus(position));
        loadStatusData();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()){
            @SuppressLint("Range") long sid = cursor.getLong(cursor.getColumnIndex(DbHelper.S_ID));
            @SuppressLint("Range") int roll = cursor.getInt(cursor.getColumnIndex(DbHelper.STUDENT_ROLL_KEY));
            @SuppressLint("Range") String studentName = cursor.getString(cursor.getColumnIndex(DbHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid,roll,studentName));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();

        if(status.equals("P")) status = "A";
        else status = "P";

        studentItems.get(position).setStatus(status);
        studentAdapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        TextView title =findViewById(R.id.title_toolbar);
        subtitle = findViewById(R.id.subtitle_toolbar);
        ImageButton back = findViewById(R.id.back);
        ImageButton save =findViewById(R.id.save);
        save.setOnClickListener(V->saveStatus());

        title.setText(className);
        subtitle.setText(section+" | "+myCalendar.getDate());

        back.setOnClickListener(v->onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(MenuItem->onMenuItemClick(MenuItem));
    }

    private void saveStatus() {
        for(StudentItem studentItem : studentItems){
            String status = studentItem.getStatus();
            if(status!="P") status = "A";
            long value = dbHelper.addStatus(studentItem.getSid(),myCalendar.getDate(),status);

            if(value==-1) dbHelper.updateStatus(studentItem.getSid(),myCalendar.getDate(),status);
        }
    }
    private void loadStatusData(){
        for(StudentItem studentItem : studentItems) {
            String status = dbHelper.getStatus(studentItem.getSid(),myCalendar.getDate());
            if(status!=null) studentItem.setStatus(status);
            else studentItem.setStatus("");
        }
        studentAdapter.notifyDataSetChanged();
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.add_student){
            showAddStudentDialog();
        }else if(menuItem.getItemId()==R.id.show_calendar){
            showCalendar();
        }
        return true;
    }

    private void showCalendar() {
        MyCalendar myCalendar = new MyCalendar();
        myCalendar.show(getSupportFragmentManager(),"");
        myCalendar.setOnCalendarClickListener(this::onCalendarClicked);
    }

    private void onCalendarClicked(int year, int month, int day) {
        myCalendar.setDate(year, month, day);
        subtitle.setText(section+" | "+myCalendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(roll,name));
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long sid = dbHelper.addStudent(cid,roll,name);
        StudentItem studentItem = new StudentItem(sid,roll,name);
        studentItems.add(studentItem);
        studentAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog myDialog = new MyDialog(studentItems.get(position).getRoll(),studentItems.get(position).getName());
        myDialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        myDialog.setListener((roll_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position, String name) {
        dbHelper.updateStudent(studentItems.get(position).getSid(),name);
        studentItems.get(position).setName(name);
        studentAdapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        studentAdapter.notifyItemRemoved(position);
    }
}