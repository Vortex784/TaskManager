package com.maxte.taskmanager.ui.edit;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.maxte.taskmanager.R;
import com.maxte.taskmanager.database.TaskDatabase;
import com.maxte.taskmanager.databinding.FragmentEditTaskBinding;
import com.maxte.taskmanager.ui.tasks.TaskItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditTaskFragment extends Fragment {

    private FragmentEditTaskBinding binding;
    private String selectedCategory = "Work"; // Default category
    private TaskItem taskToEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Hide the FAB
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        // Get the Task ID passed from the TasksFragment
        if (getArguments() != null) {
            int taskId = getArguments().getInt("task_id"); // Retrieve task_id from Bundle

            // Fetch the task to edit from the database
            new AsyncTask<Integer, Void, TaskItem>() {
                @Override
                protected TaskItem doInBackground(Integer... params) {
                    TaskDatabase db = TaskDatabase.getInstance(requireContext());
                    return db.taskDao().getTaskById(params[0]); // Get task by task ID
                }

                @Override
                protected void onPostExecute(TaskItem taskItem) {
                    taskToEdit = taskItem;
                    if (taskToEdit != null) {
                        // Set existing task values to the UI elements
                        binding.editTaskTitle.setText(taskToEdit.getTitle());
                        binding.editTaskDescription.setText(taskToEdit.getDescription());
                        binding.editTaskPrice.setText(String.valueOf(taskToEdit.getPrice()));
                        binding.editTaskRepeatable.setChecked(taskToEdit.isRepeatable());
                    } else {
                        // Handle error if task is not found (e.g., show a Toast or Snackbar)
                    }
                }
            }.execute(taskId);
        }

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

        // Set up the Confirm button to save the edited task
        Button buttonConfirm = binding.btnSaveTask;
        buttonConfirm.setOnClickListener(v -> updateTask());

        return root;
    }

    private void updateTask() {
        String title = binding.editTaskTitle.getText().toString();
        String description = binding.editTaskDescription.getText().toString();
        String priceStr = binding.editTaskPrice.getText().toString();
        boolean isRepeatable = binding.editTaskRepeatable.isChecked();
        String category = selectedCategory;

        // Validate input
        if (title.isEmpty()) {
            binding.editTaskTitle.setError("Title cannot be empty");
            return;
        }

        if (description.isEmpty()) {
            binding.editTaskDescription.setError("Description cannot be empty");
            return;
        }

        if (priceStr.isEmpty()) {
            binding.editTaskPrice.setError("Price cannot be empty");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                binding.editTaskPrice.setError("Price must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            binding.editTaskPrice.setError("Invalid price");
            return;
        }

        // Update the task data
        taskToEdit.setTitle(title);
        taskToEdit.setDescription(description);
        taskToEdit.setPrice(price);
        taskToEdit.setRepeatable(isRepeatable);
        taskToEdit.setCategory(category);

        // Save the updated task to the database in the background
        new AsyncTask<TaskItem, Void, Void>() {
            @Override
            protected Void doInBackground(TaskItem... tasks) {
                TaskDatabase.getInstance(requireContext()).taskDao().updateTask(tasks[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                // Navigate back to the previous screen
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.popBackStack();
            }
        }.execute(taskToEdit);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        // Show the FAB again
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
    }
}
