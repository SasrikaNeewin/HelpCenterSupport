package com.example.hiremenow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    // Declare private properties for UI elements and a DatabaseReference
    private lateinit var namecus: EditText
    private lateinit var emailcus: EditText
    private lateinit var phonecus: EditText
    private lateinit var subjecus: EditText
    private lateinit var Messagecus: EditText
    private lateinit var subbmitbtn: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// Initialize the properties by finding their corresponding views from the layout XML file
        namecus = findViewById(R.id.namecus)
        emailcus = findViewById(R.id.emailcus)
        phonecus = findViewById(R.id.phonecus)
        subjecus = findViewById(R.id.subjecus)
        Messagecus = findViewById(R.id.Messagecus)
        subbmitbtn = findViewById(R.id.subbmitbtn)
        // Get a reference to the "Customers" node in the Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference("Customers")
        // Set a click listener on the "Submit" button
        subbmitbtn.setOnClickListener {
            // Call the saveCustomerData function when the button is clicked
            saveCustomerData()
        }
        // Set a click listener on the "Profile" button
        val profileButton: ImageButton = findViewById(R.id.profilebtn)
        profileButton.setOnClickListener {
            // Start the MainActivity12 activity when the button is clicked
            val intent = Intent(this, MainActivity12::class.java)
            startActivity(intent)
        }
    }
    // Define the saveCustomerData function
    private fun saveCustomerData() {
       // Get the values of the EditText fields and convert them to strings
        val customerName = namecus.text.toString()
        val customerEmail = emailcus.text.toString()
        val customerPhone = phonecus.text.toString()
        val customerSubject = subjecus.text.toString()
        val customerMessage = Messagecus.text.toString()

        // check if any of the fields are empty
        if (customerName.isEmpty() || customerEmail.isEmpty() || customerPhone.isEmpty() ||
            customerSubject.isEmpty() || customerMessage.isEmpty()) {
            // Show a toast message asking the user to fill in all fields
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            return
        }
        // validate email address
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        if (!emailPattern.matches(customerEmail)) {
            // Show a toast message asking the user to enter a valid email address
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_LONG).show()
            return
        }

        // validate phone number
        val phonePattern = "^[0-9]{10}$".toRegex()
        if (!phonePattern.matches(customerPhone)) {
            // Show a toast message asking the user to enter a valid 10-digit phone number
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_LONG).show()
            return
        }

        // create a new customer object
        val customerId = dbRef.push().key!!
        val customer = CustomerModel(customerId, customerName, customerEmail, customerPhone,
            customerSubject, customerMessage)

        // save the customer object to the database
        dbRef.child(customerId).setValue(customer)
            .addOnCompleteListener {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                // clear all the fields
                namecus.text.clear()
                emailcus.text.clear()
                phonecus.text.clear()
                subjecus.text.clear()
                Messagecus.text.clear()
            }
            .addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
    }
}