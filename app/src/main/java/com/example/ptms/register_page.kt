package com.example.ptms

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class register_page : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var registerButton: Button
    private lateinit var loginText:TextView
    private lateinit var email:EditText
    private lateinit var username:EditText
    private lateinit var password:EditText
    private lateinit var contact:EditText
    private lateinit var name:EditText
    private lateinit var image:ImageView
    private lateinit var uri:Uri
    private lateinit var upload:Button
    private lateinit var db:FirebaseDatabase
    private lateinit var reference: DatabaseReference
    lateinit var databaseReference: DatabaseReference
    private lateinit var storage:FirebaseStorage
    private lateinit var storageReference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register_page)
        registerButton = findViewById(R.id.registerButton)
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()



        loginText = findViewById(R.id.loginText)

        email = findViewById(R.id.emailId)
        username = findViewById(R.id.registerusername)
        password= findViewById(R.id.registerpassword)
        name = findViewById(R.id.Name)
        contact = findViewById(R.id.contactNo)
        upload = findViewById(R.id.uploadimage)
        image= findViewById(R.id.image)
        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent() , {
            image.setImageURI(it)
            if (it != null) {
                uri = it
            }
        }

        )
        upload.setOnClickListener{
        galleryImage.launch("image/*")
        }

        registerButton?.setOnClickListener{
           val uname = username.text.toString()
            val em = email.text.toString()
            val pass = password.text.toString()
            var nam = name.text.toString()
            var cont =contact.text.toString()
            if(em.isNotEmpty() && pass.isNotEmpty() && uname.isNotEmpty()&& nam.isNotEmpty() && cont.isNotEmpty() ) {

                firebaseAuth.createUserWithEmailAndPassword(em, pass).addOnCompleteListener {



                    if (it.isSuccessful) {
                        Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show()

                        realtimeDatabase(nam ,em,uname,8.5f,"Btech CSE",cont)
                        setImage(uname)

                        var i: Intent = Intent(this, LoginPage::class.java)
                        startActivity(i)
                    } else {
                        try {
                            throw it.getException()!!
                        }
                        catch( e: FirebaseAuthInvalidCredentialsException) {
                           Toast.makeText(this,"Invalid Credentials Check your email",Toast.LENGTH_SHORT).show()
                        }catch (e: FirebaseAuthWeakPasswordException) {
                            Toast.makeText(this, "Weak Paasword", Toast.LENGTH_SHORT).show()
                        } catch (e: FirebaseAuthUserCollisionException) {
                            Toast.makeText(this, "Some error! Please try again"+e.toString(), Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {


                            Toast.makeText(
                                this,
                                it.exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }
            else {
                Toast.makeText(this,"Empty Fields are not allowed",Toast.LENGTH_SHORT).show()
            }


        }
        loginText.setOnClickListener{
            val intent :Intent = Intent(this,LoginPage::class.java)
            startActivity(intent)
        }




    }
    fun realtimeDatabase(nam:String,em:String,uname:String,cgpa:Float,program:String,cont:String) {
        val users = User(nam, em, uname, cgpa, program, cont)
        reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.child(uname).setValue(users).addOnCompleteListener{
            if(it.isSuccessful) {
                Toast.makeText(this, "Your data saved ", Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun setImage(uname: String){
        storageReference = FirebaseStorage.getInstance().getReference("Images")
        storageReference.child(System.currentTimeMillis().toString()).putFile(uri).addOnSuccessListener {
            task -> task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
            val uid = FirebaseAuth.getInstance().currentUser!!.uid


            val userImages = UserImage(uri.toString())
            if (uid != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Images")
                databaseReference.child(uid).setValue(userImages).addOnSuccessListener {
                    Toast.makeText(this,"Your images is also saved",Toast.LENGTH_SHORT).show()
                }
            }

        }
        }
    }



}