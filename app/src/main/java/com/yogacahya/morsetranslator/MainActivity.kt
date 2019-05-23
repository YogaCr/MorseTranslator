package com.yogacahya.morsetranslator

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var alphabet = true
    lateinit var db: MorseDatabase

    companion object {
        var error = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkDb()
        btnCamera.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            startActivityForResult(intent, 1)
        }
        btnCopy.setOnClickListener {
            try {
                if (!error && !etOrigin.text.isNullOrBlank()) {
                    val clipboard = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                    val clip = ClipData.newPlainText("copiedtext", etTranslated.text.toString())
                    clipboard.primaryClip = clip
                    Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "You can't copy this text", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, e.message!!, Toast.LENGTH_SHORT).show()
            }
        }
        btnSwap.setOnClickListener {
            if (tvOrigin.text.toString() == "Alphabet") {
                tvOrigin.text = "Morse"
                tvTranslated.text = "Alphabet"
                alphabet = false
                tvDesc.visibility = View.VISIBLE
                swapTranslate()
            } else {
                tvOrigin.text = "Alphabet"
                tvTranslated.text = "Morse"
                alphabet = true
                tvDesc.visibility = View.GONE
                swapTranslate()
            }
        }
        etOrigin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrBlank()) {
                    etTranslated.setText(Translate(db, alphabet).execute(s.toString()).get())

                } else {
                    etTranslated.setText("")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            etOrigin.setText(data?.getStringExtra("text"))
            etTranslated.setText(Translate(db, alphabet).execute(data?.getStringExtra("text")).get())
        }
    }

    private fun swapTranslate() {
        if (error) {
            etTranslated.setText("")
            etOrigin.setText("")
            return
        }
        val s = etOrigin.text.toString()
        etOrigin.setText(etTranslated.text.toString())
        etTranslated.setText(s)
        if (!etOrigin.text.isNullOrBlank()) {
            etTranslated.setText(Translate(db, alphabet).execute(etOrigin.text.toString()).get())

        } else {
            etTranslated.setText("")
        }
    }

    private fun checkDb() {
        db = MorseDatabase.getInstance(this)!!
        Toast.makeText(this, db.morseDao().checkMorse().size.toString(), Toast.LENGTH_SHORT).show()
        if (db.morseDao().checkMorse().size < 41) {
            db.morseDao().deleteAll()
            db.morseDao().insertMorse(Morse("A", ".-"))
            db.morseDao().insertMorse(Morse("B", "-..."))
            db.morseDao().insertMorse(Morse("C", "-.-."))
            db.morseDao().insertMorse(Morse("D", "-.."))
            db.morseDao().insertMorse(Morse("E", "."))
            db.morseDao().insertMorse(Morse("F", "..-."))
            db.morseDao().insertMorse(Morse("G", "--."))
            db.morseDao().insertMorse(Morse("H", "...."))
            db.morseDao().insertMorse(Morse("I", ".."))
            db.morseDao().insertMorse(Morse("J", ".---"))
            db.morseDao().insertMorse(Morse("K", "-.-"))
            db.morseDao().insertMorse(Morse("L", ".-.."))
            db.morseDao().insertMorse(Morse("M", "--"))
            db.morseDao().insertMorse(Morse("N", "-."))
            db.morseDao().insertMorse(Morse("O", "---"))
            db.morseDao().insertMorse(Morse("P", ".--."))
            db.morseDao().insertMorse(Morse("Q", "--.-"))
            db.morseDao().insertMorse(Morse("R", ".-."))
            db.morseDao().insertMorse(Morse("S", "..."))
            db.morseDao().insertMorse(Morse("T", "-"))
            db.morseDao().insertMorse(Morse("U", "..-"))
            db.morseDao().insertMorse(Morse("V", "...-"))
            db.morseDao().insertMorse(Morse("W", ".--"))
            db.morseDao().insertMorse(Morse("X", "-..-"))
            db.morseDao().insertMorse(Morse("Y", "-.--"))
            db.morseDao().insertMorse(Morse("Z", "--.."))
            db.morseDao().insertMorse(Morse(".", ".-.-.-"))
            db.morseDao().insertMorse(Morse(",", "--..--"))
            db.morseDao().insertMorse(Morse(":", "---..."))
            db.morseDao().insertMorse(Morse("-", "-....-"))
            db.morseDao().insertMorse(Morse("/", "-..-."))
            db.morseDao().insertMorse(Morse("1", ".----"))
            db.morseDao().insertMorse(Morse("2", "..---"))
            db.morseDao().insertMorse(Morse("3", "...--"))
            db.morseDao().insertMorse(Morse("4", "....-"))
            db.morseDao().insertMorse(Morse("5", "....."))
            db.morseDao().insertMorse(Morse("6", "-...."))
            db.morseDao().insertMorse(Morse("7", "--..."))
            db.morseDao().insertMorse(Morse("8", "---.."))
            db.morseDao().insertMorse(Morse("9", "----."))
            db.morseDao().insertMorse(Morse("0", "-----"))
        }
    }

    class Translate(private val db: MorseDatabase, private val alphabet: Boolean) : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            var result = ""
            if (alphabet) {
                var x = 0
                for (c in params[0]!!.toUpperCase().toCharArray()) {
                    try {
                        if (c == ' ' && result == "" || c == '\n' && result == "") {
                            continue
                        }
                        result += if (c == ' ' || c == '\n') {
                            ""
                        } else {
                            db.morseDao().selectMorseByAlphabet(c.toString()).morse
                        }
                        if (x != params[0]!!.toCharArray().size - 1) {
                            result += "/"
                        }
                        x = x.inc()
                        MainActivity.error = false
                    } catch (e: Exception) {
                        result = if (e is NullPointerException) {
                            "Please insert it with right character."
                        } else {
                            e.message!!
                        }
                        MainActivity.error = true
                        break
                    }

                }
            } else {
                try {
                    val m = params[0]!!.replace("_", "-")
                    for (s in m.split("/")) {
                        if (s == " " && result == "") {
                            continue
                        }
                        result += if (s == "") {
                            " "
                        } else {
                            db.morseDao().selectMorseByMorse(s).alphabet
                        }
                        MainActivity.error = false
                    }
                } catch (e: Exception) {
                    result = if (e is NullPointerException) {
                        "Please insert it with right character."
                    } else {
                        e.message!!
                    }
                    MainActivity.error = true
                }

            }
            return result
        }
    }
}
