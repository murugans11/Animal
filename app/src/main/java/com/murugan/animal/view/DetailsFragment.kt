package com.murugan.animal.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.murugan.animal.R
import com.murugan.animal.databinding.FragmentDetailsBinding
import com.murugan.animal.model.Animal
import com.murugan.animal.model.AnimalPalette
import com.murugan.animal.utils.getProgressDrawable
import com.murugan.animal.utils.loadImage


class DetailsFragment : Fragment() {

    private var animal: Animal? = null
    private lateinit var dataBinding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            animal = DetailsFragmentArgs.fromBundle(it).animal
        }

        animal?.imageUrl?.let {
            setUpBackgroundColor(view, it)
        }

        dataBinding.animal = animal

    }

    private fun setUpBackgroundColor(view: View, imageUrl: String) {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource).generate() { paletter ->
                        val intColor = paletter?.lightVibrantSwatch?.rgb ?: 0
                        dataBinding.palette = AnimalPalette(intColor)
                    }
                }


            })

    }


}