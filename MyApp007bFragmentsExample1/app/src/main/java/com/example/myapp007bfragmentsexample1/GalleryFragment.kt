package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    // Obrázky a názvy forem ve stejném pořadí
    private val images = listOf(
        R.drawable.img_tatsugiri_curly,
        R.drawable.img_tatsugiri_droopy,
        R.drawable.img_tatsugiri_stretchy
    )
    private val names = listOf(
        R.string.form_curly,
        R.string.form_droopy,
        R.string.form_stretchy
    )

    // Která forma je právě vidět
    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showForm()

        binding.btnPrevious.setOnClickListener {
            index = index - 1
            if (index < 0) {
                index = images.size - 1
            }
            showForm()
        }

        binding.btnNext.setOnClickListener {
            index = index + 1
            if (index >= images.size) {
                index = 0
            }
            showForm()
        }
    }

    // Zobrazí obrázek a název podle indexu
    private fun showForm() {
        binding.ivForm.setImageResource(images[index])
        binding.tvFormName.text = getString(names[index])
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
