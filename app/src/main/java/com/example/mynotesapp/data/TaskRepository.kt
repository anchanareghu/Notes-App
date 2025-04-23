package com.example.mynotesapp.data

import javax.inject.Inject

class TaskRepository @Inject constructor(
   private val taskDao: TaskDao
) {
   suspend fun insertTask(task: Task) = taskDao.insert(task)
   suspend fun getTasksById(id: String) = taskDao.getTasksById(id)
}