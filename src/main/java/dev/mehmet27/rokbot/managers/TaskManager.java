package dev.mehmet27.rokbot.managers;

import dev.mehmet27.rokbot.Main;
import dev.mehmet27.rokbot.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {

	private final Main main;

	private final List<Task> taskList = new ArrayList<>();

	private final Map<String, Task> taskMap = new HashMap<>();

	private String lastTask = null;

	private Task currentTask = null;

	public TaskManager(Main main) {
		this.main = main;
		/*taskMap.put("scoutFog", new ScoutFogTask());
		taskMap.put("collectVillages", new CollectVillagesTask());
		taskMap.put("allianceHelp", new AllianceHelpTask());*/
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public void putTask(Task task) {
		getTaskMap().get(lastTask).setNextTask(task);
		getTaskMap().put(task.getClass().getSimpleName(), task);
		lastTask = task.getClass().getSimpleName();
	}

	public Map<String, Task> getTaskMap() {
		return taskMap;
	}

	public void runTasks() {

	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}
}
