package ch.epfl.sdp.user

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import ch.epfl.sdp.authentication.IUserConnector
import ch.epfl.sdp.authentication.User
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityProfileBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.io.File
import javax.inject.Inject


class ProfileActivity: AppCompatActivity(), Callback {
    private lateinit var binding: ActivityProfileBinding
    private var newProfilePic: Bitmap? = null
    @Inject lateinit var connector: IUserConnector
    @Inject lateinit var cache: IUserCache

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.profilePictureView.setOnClickListener() {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 1)
        }
        binding.okButton.setOnClickListener {
            validateInformations()
        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 2)
        } else {
            setInformations()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            2 -> if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setInformations()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data!!
            val filePathColumn = Array(1) { MediaStore.Images.Media.DATA }

            val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)
            cursor?.moveToFirst()

            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])!!
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            Picasso.with(this)
                    .load(File(picturePath))
                    .fit().centerCrop()
                    .into(binding.profilePictureView, this)
        }
    }

    private fun setInformations() {
        binding.pseudoText.text = Editable.Factory().newEditable(User.pseudo)
        if(User.profilePic == null) {
            Picasso.with(this)
                    .load("https://cdn0.iconfinder.com/data/icons/seo-marketing-glyphs-vol-7/52/user__avatar__man__profile__Person__target__focus-512.png")
                    .into(binding.profilePictureView)
        }
        else
            binding.profilePictureView.setImageBitmap(User.profilePic)
    }

    private fun validateInformations() {
        val bmp = binding.profilePictureView.drawable.toBitmap()
        val ps = binding.pseudoText.text.toString()
        var pseudoModify: String? = null
        var picModify: Bitmap? = null
        if(User.profilePic == null || !User.profilePic!!.sameAs(newProfilePic)) {
            User.profilePic = bmp
            picModify = bmp
        }
        if(User.pseudo != ps) {
            User.pseudo = ps
            pseudoModify = ps
        }
        cache.put(this)
        connector.modify(pseudoModify, picModify, {
            if(picModify != null) showSuccessAndKill()
            else finish()
        }, {onError()})
    }

    private fun showSuccessAndKill() {
        Looper.prepare()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Successfully uploaded profile pic")
                .setCancelable(false)
                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ -> finish()})
                .setOnDismissListener {finish()}
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onSuccess() {
        newProfilePic = binding.profilePictureView.drawable.toBitmap()
    }

    override fun onError() {
        Looper.prepare()
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Error")
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .setOnDismissListener {finish()}
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}