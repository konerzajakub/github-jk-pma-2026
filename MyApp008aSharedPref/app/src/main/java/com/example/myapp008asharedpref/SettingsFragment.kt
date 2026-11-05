package com.example.myapp008asharedpref

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp008asharedpref.databinding.FragmentSettingsBinding
import kotlin.random.Random

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireActivity().getSharedPreferences("GameProfile", Context.MODE_PRIVATE)

        // Do formuláře se načte, co už je uložené
        binding.etNickname.setText(sharedPref.getString("NICKNAME", ""))
        binding.switchHardcoreMode.isChecked = sharedPref.getBoolean("HARDCORE", false)

        binding.btnSave.setOnClickListener {
            val editor = sharedPref.edit()
            editor.putString("NICKNAME", binding.etNickname.text.toString())
            editor.putBoolean("HARDCORE", binding.switchHardcoreMode.isChecked)

            // Simulace těžké práce, hlavní vlákno se na 3 s zastaví
            Thread.sleep(3000)

            // Synchronní zápis
            editor.commit()

            Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show()
        }

        // Kostka: nesouvisí s ukládáním, jen ukazuje, jestli aplikace reaguje
        var rolls = 0
        binding.tvRolls.text = getString(R.string.rolls, rolls)
        binding.btnRoll.setOnClickListener {
            val number = Random.nextInt(1, 7)
            rolls++
            binding.tvDice.text = number.toString()
            binding.tvRolls.text = getString(R.string.rolls, rolls)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
