package dev.kdrag0n.confirmationuitest

import android.os.Bundle
import android.os.Looper
import android.security.ConfirmationCallback
import android.security.ConfirmationPrompt
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import dev.kdrag0n.confirmationuitest.databinding.ActivityMainBinding
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener {
            showPrompt()
        }
    }

    private fun showPrompt() {
        if (!ConfirmationPrompt.isSupported(this)) {
            showToast("Not supported")
            return
        }

        val dialog = ConfirmationPrompt.Builder(this).run {
            setPromptText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.")
            setExtraData(ByteArray(16))
            build()
        }

        dialog.presentPrompt(Executors.newSingleThreadExecutor(), object : ConfirmationCallback() {
            override fun onConfirmed(dataThatWasConfirmed: ByteArray) {
                showToast("Confirmed")
            }

            override fun onDismissed() {
                showToast("Dismissed")
            }

            override fun onCanceled() {
                showToast("Canceled")
            }

            override fun onError(e: Throwable?) {
                showToast("Error")
            }
        })
    }

    private fun showToast(text: String) {
        findViewById<View>(android.R.id.content).post {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
