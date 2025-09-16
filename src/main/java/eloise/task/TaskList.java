package eloise.task;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import eloise.exception.EloiseException;
import eloise.exception.InvalidIndexException;
import eloise.parser.Parser;

public class TaskList{
    private final List<Task> tasks;

    /**
     * Creates an empty list if no existing data
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>();
        if (initial != null) {
            this.tasks.addAll(initial);
        }
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task addTask(Task t) throws EloiseException{
        if (t == null) throw new EloiseException("Cannot add a null task");
        tasks.add(t);
        return t;
    }

    public int addAll(List<Task> taskList) throws EloiseException{
        if (taskList == null) throw new EloiseException("Cannot add from a null list");
        for (Task t : taskList) {
            if (t == null) throw new EloiseException("Encountered null task in the list!");
        }
        int iniSize = tasks.size();
        tasks.addAll(taskList);
        return tasks.size() - iniSize;
    }

    public Task get(int idxZeroBased) throws InvalidIndexException {
        if ( idxZeroBased < 0 || idxZeroBased >= tasks.size()) {
            throw new InvalidIndexException("Task number out of range", tasks.size());
        }

        return tasks.get(idxZeroBased);
    }

    public Task mark(int idxOneBased) throws InvalidIndexException {
        Task t = getByOneBased(idxOneBased);
        t.mark();
        return t;
    }

    public Task unmark(int idxOneBased) throws InvalidIndexException {
        Task t = getByOneBased(idxOneBased);
        t.unmark();
        return t;
    }

    public Task delete(int idxOneBased) throws InvalidIndexException{
        int idx = toZeroBased(idxOneBased);
        return tasks.remove(idx);
    }

    public List<Task> getAll() {
        return Collections.unmodifiableList(tasks);
    }

    public TaskList findTasks(String keyword) throws EloiseException {
        TaskList matches = new TaskList();

        for (Task t : tasks) {
            if (t.getDescription().contains(keyword)) {
                matches.addTask(t);
            }
        }
        return matches;
    }

    // replaces list function
    @Override
    public String toString() {
        if (tasks.isEmpty()) {
            return "No items added yet.";
        }
        StringBuilder list = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            list.append(i + 1).append(". ")
                    .append(tasks.get(i))
                    .append(System.lineSeparator());
        }

        return list.toString().stripTrailing();
    }
// helper methods
    private Task getByOneBased(int idxOneBased) throws InvalidIndexException{
        int idx = toZeroBased(idxOneBased);
        return tasks.get(idx);
    }

    private int toZeroBased(int idx) throws InvalidIndexException{
        if (idx < 1 || idx > tasks.size()) {
            throw new InvalidIndexException("Task number out of range", tasks.size());
        }
        return idx - 1;
    }
}