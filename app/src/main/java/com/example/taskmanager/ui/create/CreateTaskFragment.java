package com.example.taskmanager.ui.create;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.taskmanager.R;
import com.example.taskmanager.database.TaskDatabase;
import com.example.taskmanager.databinding.FragmentCreateTaskBinding;
import com.example.taskmanager.ui.tasks.TaskItem;
import com.example.taskmanager.ui.tasks.TasksFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateTaskFragment extends Fragment {

    private FragmentCreateTaskBinding binding;
    private String selectedCategory = "Work"; // Default category

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        binding.createTaskRepeatable.setText("Repeatable?");

        // Get categories from resources
        String[] categories = getResources().getStringArray(R.array.task_categories);

        // Convert the array to a list
        List<String> categoryList = new ArrayList<>(Arrays.asList(categories));

        // Remove "All" category if it exists
        categoryList.remove("All");

        // Create an adapter with the modified category list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, categoryList);
        binding.spinnerCategory.setAdapter(adapter);

        // Handle category selection
        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button buttonConfirm = binding.createTaskConfirm;
        buttonConfirm.setOnClickListener(v -> saveTask());

        return root;
    }

    private void saveTask() {
        String title = binding.createTaskTitle.getText().toString();
        String description = binding.createTaskDesc.getText().toString();
        String priceStr = binding.createTaskPrice.getText().toString();
        boolean isRepeatable = binding.createTaskRepeatable.isChecked();
        String category = selectedCategory;

        // Validate input
        if (title.isEmpty()) {
            binding.createTaskTitle.setError("Title cannot be empty");
            return;
        }

        if (description.isEmpty()) {
            binding.createTaskDesc.setError("Description cannot be empty");
            return;
        }

        if (priceStr.isEmpty()) {
            binding.createTaskPrice.setError("Price cannot be empty");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                binding.createTaskPrice.setError("Price must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            binding.createTaskPrice.setError("Invalid price");
            return;
        }
        // Create the new task item
        TaskItem task = new TaskItem(title, description, Double.parseDouble(priceStr), isRepeatable, category);

        // Save to database in background
        new AsyncTask<TaskItem, Void, Void>() {
            @Override
            protected Void doInBackground(TaskItem... tasks) {
                TaskDatabase.getInstance(getContext()).taskDao().insertTask(tasks[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Navigate back to the previous screen
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.popBackStack();

                // Refresh tasks in TasksFragment
                TasksFragment tasksFragment = (TasksFragment) getActivity().getSupportFragmentManager()
                        .findFragmentByTag(TasksFragment.class.getSimpleName());

            }
        }.execute(task);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }
}
