package com.example.hiremenow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase

class MainActivity2 : AppCompatActivity() {
    // declaring private variables for TextViews, Buttons, and ImageView
    private lateinit var tvcusId:TextView
    private lateinit var tvNames:TextView
    private lateinit var tvEmail:TextView
    private lateinit var tvPhone:TextView
    private lateinit var tvSubject: TextView
    private lateinit var tvMessage:TextView
    private lateinit var tvEdit:Button
    private lateinit var tvDelete:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
// initialize ImageView and set a click listener to go to the MainActivity
        val help2btn = findViewById<ImageView>(R.id.help2btn)
        help2btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // initialize Delete button and set a click listener to go to the MainActivity6
        val myButton2 = findViewById<Button>(R.id.tvDelete)
        myButton2.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            startActivity(intent)
        }
        // initialize views
        initView()
        // set values to the views
        setValuesToViews()
        // set click listeners for Edit and Delete buttons
        tvEdit.setOnClickListener{
            openUpdateDialog(
                intent.getStringExtra("customerId").toString(),
                intent.getStringExtra("customerName").toString()
            )
        }
        tvDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("customerId").toString()
            )
        }
    }
//delete Records
    private fun deleteRecord(
        id: String
    ){
    // get reference to the database
        val dbRef = FirebaseDatabase.getInstance().getReference("Customers").child(id)
    // remove the value from the database
        val mTask = dbRef.removeValue()
    // add success and failure listeners
        mTask.addOnSuccessListener {
            Toast.makeText(this ,"Your data deleted",Toast.LENGTH_LONG).show()
            val intent = Intent(this,MainActivity13::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this ,"Deleting Err ${error.message}",Toast.LENGTH_LONG).show()
        }
    }

    // function to initialize views
    private fun initView() {
        tvcusId=findViewById(R.id.tv_cusId)
        tvNames = findViewById(R.id.tv_Names)
        tvEmail = findViewById(R.id.tv_Email)
        tvPhone = findViewById(R.id.tv_Phone)
        tvSubject = findViewById(R.id.tv_Subject)
        tvMessage = findViewById(R.id.tv_Message)
        tvEdit = findViewById(R.id.tvEdit)
        tvDelete = findViewById(R.id.tvDelete)
    }
    // function to set values to views
    private fun setValuesToViews(){
        tvcusId.text= intent.getStringExtra("customerId")
        tvNames.text= intent.getStringExtra("customerName")
        tvEmail.text= intent.getStringExtra("customerEmail")
        tvPhone.text= intent.getStringExtra("customerPhone")
        tvSubject.text= intent.getStringExtra("customerSubject")
        tvMessage.text= intent.getStringExtra("customerMessage")
    }
    // function to open update dialog
    private fun openUpdateDialog(
        customerId: String,
        customerName:String
    ){
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.activity_main5,null)

        mDialog.setView(mDialogView)
// initialize EditText views for customer data
        val etName = mDialogView.findViewById<EditText>(R.id.etName)
        val etEmail = mDialogView.findViewById<EditText>(R.id.etEmail)
        val etPhone = mDialogView.findViewById<EditText>(R.id.etPhone)
        val etSubject = mDialogView.findViewById<EditText>(R.id.etSubject)
        val etMessage = mDialogView.findViewById<EditText>(R.id.etMessage)
// initialize update button
        val updateconfirm = mDialogView.findViewById<Button>(R.id.updateconfirm)
// set current customer data to EditText views
        etName.setText( intent.getStringExtra("customerName").toString())
        etEmail.setText( intent.getStringExtra("customerEmail").toString())
        etPhone.setText( intent.getStringExtra("customerPhone").toString())
        etSubject.setText( intent.getStringExtra("customerSubject").toString())
        etMessage.setText( intent.getStringExtra("customerMessage").toString())
// set dialog title with customer name
        mDialog.setTitle("Updating $customerName Record")
// set update button click listener
        val alertDialog = mDialog.create()
        alertDialog.show()
// update the customer data in the database
        updateconfirm.setOnClickListener {
            updateCusData(
                customerId,
                etName.text.toString(),
                etEmail.text.toString(),
                etPhone.text.toString(),
                etSubject.text.toString(),
                etMessage.text.toString()
            )
// show a toast message to indicate data has been updated
            Toast.makeText(applicationContext,"Your Data Updated",Toast.LENGTH_LONG).show()

            //we wre setting the updated data to our textviews
            // update the text views with the new data
            tvNames.text= etName.text.toString()
            tvEmail.text= etEmail.text.toString()
            tvPhone.text=  etPhone.text.toString()
            tvSubject.text= etSubject.text.toString()
            tvMessage.text=  etMessage.text.toString()

            alertDialog.dismiss()
        }


    }

    private fun updateCusData(
        id:String,
        name:String,
        email:String,
        phone:String,
        subject:String,
        message:String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Customers").child(id)
        val cusInfo = CustomerModel(id,name,email,phone,subject,message)
        dbRef.setValue(cusInfo)
    }

}