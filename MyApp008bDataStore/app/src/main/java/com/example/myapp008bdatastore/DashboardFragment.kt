package com.example.myapp008bdatastore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp008bdatastore.databinding.FragmentDashboardBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Načtení uložených dat z DataStore (asynchronně)
        viewLifecycleOwner.lifecycleScope.launch {
            val preferences = requireContext().dataStore.data.first()
            val name = preferences[NICKNAME_KEY] ?: ""
            val hardcore = preferences[HARDCORE_KEY] ?: false
            showProfile(name, hardcore)
        }
    }

    // Vypíše přezdívku a režim do přehledu
    private fun showProfile(name: String, hardcore: Boolean) {
        if (name == "") {
            binding.tvNickname.text = getString(R.string.nickname, getString(R.string.no_nickname))
        } else {
            binding.tvNickname.text = getString(R.string.nickname, name)
        }
        if (hardcore) {
            binding.tvHardcore.text = getString(R.string.hardcore_on)
        } else {
            binding.tvHardcore.text = getString(R.string.hardcore_off)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
