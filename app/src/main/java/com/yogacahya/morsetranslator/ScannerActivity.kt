package com.yogacahya.morsetranslator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.lucem.anb.characterscanner.ScannerListener
import kotlinx.android.synthetic.main.activity_scanner.*

class ScannerActivity : AppCompatActivity() {
    private var scanned = false
    private var result = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        btnTranslate.setOnClickListener {
            if (scanned) {
                val data = Intent()
                data.putExtra("text", result)
                setResult(Activity.RESULT_OK, data)
                finish()
            }
        }
        scanner.isFocusable = true
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        scanner.setOnDetectedListener(this, object : ScannerListener {
            override fun onStateChanged(p0: String?, p1: Int) {

            }

            override fun onDetected(p0: String?) {
                tvScanning.visibility = View.GONE
                lyScan.visibility = View.VISIBLE
                tvScanResult.text = p0!!
                result = p0
                scanned = true
            }
        })
    }
}
