package com.example.myapp008bdatastore

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapp008bdatastore.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.random.Random

// DataStore pro celou aplikaci a klíče, používá je i DashboardFragment
val Context.dataStore by preferencesDataStore(name = "gamer_profile")
val NICKNAME_KEY = stringPreferencesKey("NICKNAME")
val HARDCORE_KEY = booleanPreferencesKey("HARDCORE")

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

        // Do formuláře se načte, co už je uložené
        viewLifecycleOwner.lifecycleScope.launch {
            val preferences = requireContext().dataStore.data.first()
            binding.etNickname.setText(preferences[NICKNAME_KEY] ?: "")
            binding.switchHardcoreMode.isChecked = preferences[HARDCORE_KEY] ?: false
        }

        binding.btnSave.setOnClickListener {
            val newName = binding.etNickname.text.toString()
            val newHardcore = binding.switchHardcoreMode.isChecked
            val appContext = requireContext().applicationContext

            viewLifecycleOwner.lifecycleScope.launch {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnSave.isEnabled = false

                // Staré hodnoty si zapamatujeme kvůli tlačítku Zpět
                val oldPreferences = appContext.dataStore.data.first()
                val oldName = oldPreferences[NICKNAME_KEY] ?: ""
                val oldHardcore = oldPreferences[HARDCORE_KEY] ?: false

                // Čekání 3 s, hlavní vlákno ale zůstává volné
                delay(3000)

                appContext.dataStore.edit { prefs ->
                    prefs[NICKNAME_KEY] = newName
                    prefs[HARDCORE_KEY] = newHardcore
                }

                binding.progressBar.visibility = View.INVISIBLE
                binding.btnSave.isEnabled = true

                // Snackbar s tlačítkem Zpět vrátí původní hodnoty
                Snackbar.make(binding.root, getString(R.string.saved), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        lifecycleScope.launch {
                            appContext.dataStore.edit { prefs ->
                                prefs[NICKNAME_KEY] = oldName
                                prefs[HARDCORE_KEY] = oldHardcore
                            }
                        }
                        if (_binding != null) {
                            binding.etNickname.setText(oldName)
                            binding.switchHardcoreMode.isChecked = oldHardcore
                        }
                        Toast.makeText(appContext, appContext.getString(R.string.undone), Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
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
