package com.example.knowhub.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.knowhub.R;
import com.example.knowhub.databinding.FragmentHomeBinding;
import com.example.knowhub.quiz.QuizActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Quiz Karten clickListener einrichten
        CardView cardAndroidQuiz = root.findViewById(R.id.card_android_quiz);
        cardAndroidQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("android");
            }
        });

        CardView cardProgrammingQuiz = root.findViewById(R.id.card_programming_quiz);
        cardProgrammingQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz("programming");
            }
        });

        return root;
    }

    private void startQuiz(String topic) {
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}