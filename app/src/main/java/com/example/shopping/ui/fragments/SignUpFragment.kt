package com.example.shopping.ui.fragments

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImage
import com.example.shopping.R
import com.example.shopping.databinding.FragmentSignUpBinding
import com.example.shopping.ui.viewmodels.RegisterViewModel
import com.example.shopping.utilies.BitmapResolver
import com.example.shopping.utilies.Extension.convertToRequestBody
import com.example.shopping.utilies.Extension.replaceFragment
import com.example.shopping.utilies.Extension.showSnake
import com.example.shopping.utilies.baseClasses.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.math.pow


@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>(FragmentSignUpBinding::inflate),
    View.OnClickListener {
    private var viewModel: RegisterViewModel? = null
    private var imageUri: File? = null
    override fun FragmentSignUpBinding.initialize() {
        initViews()
        viewModel = ViewModelProvider(requireActivity())[RegisterViewModel::class.java]
        viewModel!!.getDatum().observe(requireActivity(), {
            registerSuccessful()
        })

    }

    private fun registerSuccessful() {
        val loginFragment = AccountFragment()
        replaceFragment(
            loginFragment,
            R.id.FragmentLoad,
            requireActivity().supportFragmentManager.beginTransaction()
        )
    }

    private fun initViews() {
        binding.btnBrowser.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_Browser -> {
                galleryIntent()
            }
            R.id.btn_SignUp -> {
                userRegister()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1994 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission from popup was granted
                    galleryIntent()

                } else // permission from popup was denied
                    showSnake(requireView(), getString(R.string.permission_denied))
            }
            else -> return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                1994 -> {
                    if (data.clipData != null) {
                        val mClipData: ClipData = data.clipData!!
                        for (i in 0 until mClipData.itemCount) {
                            val item: ClipData.Item = mClipData.getItemAt(i)
                            val uri: Uri = item.uri
                            setImageCallback(uri)

                        }
                    } else
                        setImageCallback(data.data!!)
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> CropImage.getActivityResult(data)
                    ?.let {
                        it.uriContent?.let { result ->
                            setImageCallback(result)

                        }
                    }

            }
        } else
            println("onActivityResult : null")

    }

    private fun setImageCallback(uri: Uri) {
        try {
            val bitmap = BitmapResolver.getBitmap(requireActivity().contentResolver, uri)!!
            val file: File = saveToFile(
                generateRandomDigits(10).toString(),
                getResizedBitmap(bitmap, 1024)

            )

            imageUri = file
            println("FilePath:" + file.absolutePath)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun generateRandomDigits(n: Int): Int {
        val m = 10.0.pow((n - 1).toDouble()).toInt()
        return m + Random().nextInt(9 * m)
    }

    private fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width: Int = image.width
        var height: Int = image.height

        val bitmapRatio: Float = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        binding.ivSignUp.setImageBitmap(image)
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    private fun saveToFile(filename: String, bitmap: Bitmap): File {
        val sd: File = requireActivity().cacheDir
        val folder = File(sd, "/uploadedImages/")

        if (!folder.exists()) {
            folder.mkdirs()
        }

        val savedFile = File(folder, filename)
        if (savedFile.exists()) {
            savedFile.delete()
        }

        try {
            savedFile.createNewFile()
            val outputStream = FileOutputStream(savedFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

        } catch (ignored: IOException) {
        }


        return savedFile
    }

    private fun galleryIntent() {
        CropImage.activity().start(requireContext(), this)
    }


    private fun userRegister() {

        val requestBody = RequestBody.create(MediaType.parse("image/*"),imageUri)
        val fileToUpload: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", imageUri!!.name, requestBody)

        val name: RequestBody = convertToRequestBody(
            Objects.requireNonNull(binding.etName.text).toString().trim()
        )
        val phone: RequestBody = convertToRequestBody(
            Objects.requireNonNull(binding.etPhone.text).toString().trim()
        )
        val email: RequestBody = convertToRequestBody(
            Objects.requireNonNull(binding.etSignUpEmail.text).toString().trim()
        )
        val password: RequestBody = convertToRequestBody(
            Objects.requireNonNull(binding.etPassword.text).toString().trim()
        )

        viewModel!!.getRegisterDetails(name, phone, email, password, fileToUpload)

    }
}


