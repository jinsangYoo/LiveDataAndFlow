package com.example.livedataandflow.permission

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.livedataandflow.R
import com.example.livedataandflow.databinding.ActivityPermissionBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermissionUtil
import com.gun0912.tedpermission.normal.TedPermission as norTedPermission
import com.gun0912.tedpermission.coroutine.TedPermission as corTedPermission
import kotlinx.coroutines.launch
import timber.log.Timber

class PermissionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater).apply {
            isGrantedPostNotification.setOnClickListener {
                val result = isGranted("android.permission.POST_NOTIFICATIONS")
                Timber.d("isGranted post notification: $result")
                Toast.makeText(this@PermissionActivity, "result: $result", Toast.LENGTH_SHORT).show()
            }
            normalRequestPostNotification.setOnClickListener {
                normalRequestPermission {
                    todo()
                }
            }
            coroutineRequestPostNotification.setOnClickListener {
                lifecycleScope.launch {
                    coroutineRequestPermission {
                        todo()
                    }
                }
            }
            canRequestPostNotification.setOnClickListener {
                val result = canRequest("android.permission.POST_NOTIFICATIONS")
                Timber.d("request post notification: $result")
                Toast.makeText(this@PermissionActivity, "result: $result", Toast.LENGTH_SHORT).show()
            }
        }
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun isGranted(permission: String): Boolean {
        return TedPermissionUtil.isGranted(permission)
    }

    private fun canRequest(permission: String): Boolean {
        return TedPermissionUtil.canRequestPermission(this, permission)
    }

    private fun normalRequestPermission(logic: () -> Unit) {
        norTedPermission.create()
            .setPermissionListener(makePermissionListener(logic = logic))
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(
                Manifest.permission.POST_NOTIFICATIONS
            )
            .check()

//            .setPermissions(
//                Manifest.permission.POST_NOTIFICATIONS,
//                Manifest.permission.SYSTEM_ALERT_WINDOW
//            )
    }

    private suspend fun coroutineRequestPermission(logic: () -> Unit) {
        val permissionResult = corTedPermission.create()
            .setRationaleTitle(R.string.rationale_title)
            .setRationaleMessage(R.string.rationale_message)
            .setDeniedTitle("Permission denied")
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setGotoSettingButtonText("bla bla setting")
            .setPermissions(
                Manifest.permission.POST_NOTIFICATIONS
            )
            .check()
        Timber.d("permissionResult: $permissionResult")
    }

    private fun makePermissionListener(logic: (() -> Unit)? = null): PermissionListener {
        return object : PermissionListener {
            override fun onPermissionGranted() {
                logic?.let {
                    it()
                }
            }
            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(this@PermissionActivity,
                    "권한을 허가해주세요.",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun todo(){
        // TODO : 기능 구현
        Toast.makeText(this, "완료", Toast.LENGTH_SHORT).show()
    }
}