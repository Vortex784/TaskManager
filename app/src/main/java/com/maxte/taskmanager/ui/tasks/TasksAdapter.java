package com.maxte.taskmanager.ui.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.maxte.taskmanager.R;
import com.maxte.taskmanager.database.Points;
import com.maxte.taskmanager.database.PointsDao;
import com.maxte.taskmanager.database.TaskDatabase;
import com.maxte.taskmanager.databinding.ItemTaskBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    private List<TaskItem> tasksList;
    private Context context;

    public TasksAdapter(Context context, List<TaskItem> tasksList) {
        this.tasksList = tasksList;
        this.context = context;

    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemTaskBinding itemBinding = ItemTaskBinding.inflate(layoutInflater, parent, false);
        return new TaskViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskItem task = tasksList.get(position);
        holder.bind(task);
    }



    @Override
    public int getItemCount() {
        return tasksList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private final ItemTaskBinding binding;

        public TaskViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    TaskItem task = tasksList.get(position);
                    markAsComplete(task);
                }

            });



            binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    TaskItem task = tasksList.get(position);
                    deleteTask(task);
                }
            });

            binding.btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    TaskItem task = tasksList.get(position);
                    editTask(task);
                }
            });
        }

        public void bind(TaskItem task) {
            binding.taskTitle.setText(task.getTitle());
            binding.taskDescription.setText(task.getDescription());
            binding.taskPrice.setText(String.valueOf(task.getPrice()));
        }




        private void markAsComplete(TaskItem task) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    TaskDatabase db = TaskDatabase.getInstance(context.getApplicationContext());
                    PointsDao pointsDao = db.pointsDao();
                    Points points = pointsDao.getPoints();
                    if (points == null) {
                        points = new Points(0);
                       pointsDao.insert(points);
                  }
                    points.setPoints(points.getPoints() + task.getPrice());
                    pointsDao.update(points);

                    if (!task.isRepeatable()) {
                        deleteTask(task);
                        Snackbar.make(binding.getRoot().getRootView(), "Task marked as complete and removed", Snackbar.LENGTH_LONG).show();
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    notifyDataSetChanged();
                    Snackbar.make(binding.getRoot().getRootView(), "Task marked as complete", Snackbar.LENGTH_LONG).show();
                }
            }.execute();
        }

        private void editTask(TaskItem task) {
            Bundle bundle = new Bundle();
            bundle.putInt("task_id", task.getId());  // Pass task ID as argument

            // Get the NavController from the current context (activity/fragment)
            NavController navController = Navigation.findNavController((View) binding.getRoot().getParent());
            navController.navigate(R.id.nav_edit_task, bundle);  // Navigate to TaskEditFragment with task_id as argument
        }


        private void deleteTask(TaskItem task) {
            new AsyncTask<TaskItem, Void, Void>() {
                @Override
                protected Void doInBackground(TaskItem... tasks) {
                    TaskDatabase.getInstance(itemView.getContext()).taskDao().deleteTask(tasks[0]);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    tasksList.remove(task);
                    Snackbar.make(binding.getRoot().getRootView(), "Task Deleted", Snackbar.LENGTH_LONG).show();
                    notifyDataSetChanged();

                }
            }.execute(task);
        }
    }
}
