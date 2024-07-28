package com.example.taskmanager.ui.create;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.taskmanager.R;
import com.example.taskmanager.database.TaskDatabase;
import com.example.taskmanager.databinding.FragmentCreateTaskBinding;
import com.example.taskmanager.databinding.FragmentCreateTaskBinding;
import com.example.taskmanager.ui.tasks.TaskItem;

public class CreateTaskFragment extends Fragment {

    private FragmentCreateTaskBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CreateTaskViewModel createTaskViewModel =
                new ViewModelProvider(this).get(CreateTaskViewModel.class);

        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.createTaskTitle.setText("Task title");
        binding.createTaskDesc.setText("Description");
        binding.createTaskPrice.setText("Task price");
        binding.createTaskRepeatable.setText("Repeatable?");

        Button buttonConfirm = binding.createTaskConfirm;

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();

            }
        });

        return root;
    }

    private void saveTask() {
        String title = binding.createTaskTitle.getText().toString();
        String description = binding.createTaskDesc.getText().toString();
        String price = binding.createTaskPrice.getText().toString();
        boolean isRepeatable = binding.createTaskRepeatable.isChecked();

        TaskItem task = new TaskItem(title, description, Double.parseDouble(price), isRepeatable);

        // Save to database in background
        new AsyncTask<TaskItem, Void, Void>() {
            @Override
            protected Void doInBackground(TaskItem... tasks) {
                TaskDatabase.getInstance(getContext()).taskDao().insertTask(tasks[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Go back to the previous screen
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.popBackStack();
            }
        }.execute(task);
    }

    private void navigateBack(View view) {
        NavController navController = Navigation.findNavController(view);
        navController.navigateUp();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clear any additional resources or data
    }
}