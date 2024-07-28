package com.example.taskmanager.ui.tasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.database.TaskDatabase;
import com.example.taskmanager.databinding.FragmentHomeBinding;
import com.example.taskmanager.databinding.FragmentTasksBinding;

import java.util.List;

public class TasksFragment extends Fragment {

    private FragmentTasksBinding binding;
    private TasksAdapter tasksAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TasksViewModel tasksViewModel =
                new ViewModelProvider(this).get(TasksViewModel.class);

        binding = FragmentTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerViewTasks;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTasks();






        return root;
    }

    private void loadTasks() {
        new AsyncTask<Void, Void, List<TaskItem>>() {
            @Override
            protected List<TaskItem> doInBackground(Void... voids) {
                TaskDatabase db = TaskDatabase.getInstance(getContext());
                return db.taskDao().getAllTasks();
            }
            @Override
            protected void onPostExecute(List<TaskItem> tasks) {
                tasksAdapter = new TasksAdapter(tasks);
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