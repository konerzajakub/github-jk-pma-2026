package com.example.myapp007bfragmentsexample1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp007bfragmentsexample1.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val images = listOf(
        R.drawable.img_tatsugiri_curly,
        R.drawable.img_tatsugiri_droopy,
        R.drawable.img_tatsugiri_stretchy
    )

    // Index správné odpovědi (0 = Curly, 1 = Droopy, 2 = Stretchy)
    private var answer = 0
    private var correct = 0
    private var total = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newQuestion()

        binding.btnCurly.setOnClickListener { checkAnswer(0) }
        binding.btnDroopy.setOnClickListener { checkAnswer(1) }
        binding.btnStretchy.setOnClickListener { checkAnswer(2) }
    }

    // Vybere náhodnou formu a ukáže její obrázek
    private fun newQuestion() {
        answer = images.indices.random()
        binding.ivQuiz.setImageResource(images[answer])
        binding.tvScore.text = getString(R.string.quiz_score, correct, total)
    }

    private fun checkAnswer(choice: Int) {
        total = total + 1
        if (choice == answer) {
            correct = correct + 1
            Toast.makeText(requireContext(), R.string.quiz_correct, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), R.string.quiz_wrong, Toast.LENGTH_SHORT).show()
        }
        newQuestion()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
