package ch.epfl.sdp.user

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.epfl.sdp.authentication.User
import ch.epfl.sdp.databinding.ActivityProfileBinding
import com.squareup.picasso.Picasso
import java.io.File


class ProfileActivity: AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.profilePictureView.setOnClickListener() {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 1)
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
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        when (requestCode) {
            2 -> if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setInformations()
            }
        }
    }

    private fun setInformations() {
        //val resId = this.resources.getIdentifier("defaultprofilepic.png", "drawable", this.packageName)
        //binding.profilePictureView.setImageResource(resId)
        binding.pseudoText.text = Editable.Factory().newEditable(User.pseudo)
        Picasso.with(this)
                .load("https://cdn0.iconfinder.com/data/icons/seo-marketing-glyphs-vol-7/52/user__avatar__man__profile__Person__target__focus-512.png")
                .into(binding.profilePictureView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val selectedImage = data.data!!
            val filePathColumn = Array(1) { MediaStore.Images.Media.DATA }

            val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null);
            cursor?.moveToFirst()

            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])!!
            val picturePath = cursor.getString(columnIndex);
            cursor.close();
            Picasso.with(this)
                    .load(File(picturePath))
                    .fit().centerCrop()
                    .into(binding.profilePictureView)
        }
    }
}