package com.example.phonecall

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.phonecall.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Call button click listener
        binding.callButton.setOnClickListener {
            // Check if phone number is empty
            var phoneNumber = binding.number.text.toString()
            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Add country code if not already present
            val countryCode = "+91" // Replace with your desired country code
            if (!phoneNumber.startsWith(countryCode)) {
                phoneNumber = "$countryCode$phoneNumber"
            }

            val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))

            // Check for CALL_PHONE permission
            if (ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

                // Show rationale if needed
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(this, "Phone call permission is needed to make calls", Toast.LENGTH_SHORT).show()
                }

                // Request permission
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    1
                )
            } else {
                startActivity(callIntent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Call function goes here
                val phoneNumber = binding.number.text.toString()
                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(callIntent)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
