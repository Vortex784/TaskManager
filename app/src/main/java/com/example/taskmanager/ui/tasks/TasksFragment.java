package com.example.taskmanager.ui.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.database.TaskDatabase;
import com.example.taskmanager.databinding.FragmentTasksBinding;

import java.util.List;

public class TasksFragment extends Fragment {

    private FragmentTasksBinding binding;
    private TasksAdapter tasksAdapter;
    private String selectedCategory = "All"; // Default category (All tasks initially)

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Set up RecyclerView
        RecyclerView recyclerView = binding.recyclerViewTasks;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up Spinner to choose category
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.task_categories, android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFilterCategory.setAdapter(adapter);

        // Handle category selection
        binding.spinnerFilterCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
                loadTasks(); // Reload tasks when category changes
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Initial load of tasks
        loadTasks();

        return root;
    }

    private void loadTasks() {
        new AsyncTask<Void, Void, List<TaskItem>>() {
            @Override
            protected List<TaskItem> doInBackground(Void... voids) {
                TaskDatabase db = TaskDatabase.getInstance(getContext());

                // Load tasks based on the selected category
                if ("All".equals(selectedCategory)) {
                    return db.taskDao().getAllTasks();  // Load all tasks
                } else {
                    return db.taskDao().getTasksByCategory(selectedCategory);  // Load tasks for the selected category
                }
            }

            @Override
            protected void onPostExecute(List<TaskItem> tasks) {
                // Update the RecyclerView with the filtered tasks
                tasksAdapter = new TasksAdapter(getContext(), tasks);
                binding.recyclerViewTasks.setAdapter(tasksAdapter);
            }
        }.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
