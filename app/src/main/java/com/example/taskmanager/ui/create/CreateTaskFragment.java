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

public class CreateTaskFragment extends Fragment {

    private FragmentCreateTaskBinding binding;
    private String selectedCategory = "Work"; // Default category

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.createTaskTitle.setText("Task title");
        binding.createTaskDesc.setText("Description");
        binding.createTaskPrice.setText("Task price");
        binding.createTaskRepeatable.setText("Repeatable?");

        // Initialize category Spinner using binding
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.task_categories, android.R.layout.simple_spinner_dropdown_item);
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
        String price = binding.createTaskPrice.getText().toString();
        boolean isRepeatable = binding.createTaskRepeatable.isChecked();
        String category = selectedCategory;

        // Create the new task item
        TaskItem task = new TaskItem(title, description, Double.parseDouble(price), isRepeatable, category);

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
    }
}
