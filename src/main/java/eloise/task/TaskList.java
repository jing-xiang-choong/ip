package eloise.task;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import eloise.parser.DateParser;
import eloise.exception.EloiseException;
import eloise.exception.InvalidIndexException;

/// Used ChatGPT to generate JavaDoc Comments TaskList methods

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

    /**
     * Adds a single task to the task list.
     *
     * @param t the {@link Task} to be added
     * @return the added task
     * @throws EloiseException if {@code t} is {@code null}
     */
    public Task addTask(Task t) throws EloiseException{
        if (t == null) throw new EloiseException("Cannot add a null task");
        tasks.add(t);
        assert tasks.contains(t) : "TaskList must contain newly added task";
        return t;
    }

    /**
     * Adds all tasks from the given list to this task list.
     *
     * @param taskList the list of tasks to add
     * @return the number of tasks successfully added
     * @throws EloiseException if {@code taskList} is {@code null}, or if it contains {@code null} tasks
     */
    public int addAll(List<Task> taskList) throws EloiseException{
        if (taskList == null) throw new EloiseException("Cannot add from a null list");
        for (Task t : taskList) {
            if (t == null) throw new EloiseException("Encountered null task in the list!");
        }
        int iniSize = tasks.size();
        tasks.addAll(taskList);
        assert tasks.size() == iniSize + taskList.size()
                : "TaskList size must increase by number of added tasks";
        return tasks.size() - iniSize;
    }

    /**
     * Retrieves a task at the given zero-based index.
     *
     * @param idxZeroBased the index of the task, starting from 0
     * @return the task at the specified index
     * @throws InvalidIndexException if the index is negative or out of bounds
     */
    public Task get(int idxZeroBased) throws InvalidIndexException {
        if ( idxZeroBased < 0 || idxZeroBased >= tasks.size()) {
            throw new InvalidIndexException("Task number out of range", tasks.size());
        }

        return tasks.get(idxZeroBased);
    }

    /**
     * Marks the task at the given one-based index as done.
     *
     * @param idxOneBased the index of the task, starting from 1
     * @return the marked task
     * @throws InvalidIndexException if the index is invalid
     */
    public Task mark(int idxOneBased) throws InvalidIndexException {
        Task t = getByOneBased(idxOneBased);
        t.mark();
        assert t.getIsDone() : "Task should be marked as done";
        return t;
    }

    /**
     * Marks the task at the given one-based index as not done.
     *
     * @param idxOneBased the index of the task, starting from 1
     * @return the unmarked task
     * @throws InvalidIndexException if the index is invalid
     */
    public Task unmark(int idxOneBased) throws InvalidIndexException {
        Task t = getByOneBased(idxOneBased);
        t.unmark();
        assert !t.getIsDone() : "Task should be marked as not done";
        return t;
    }

    /**
     * Deletes the task at the given one-based index from the list.
     *
     * @param idxOneBased the index of the task, starting from 1
     * @return the removed task
     * @throws InvalidIndexException if the index is invalid
     */
    public Task delete(int idxOneBased) throws InvalidIndexException{
        int idx = toZeroBased(idxOneBased);
        return tasks.remove(idx);
    }

    /**
     * Returns an unmodifiable view of all tasks in the list.
     *
     * @return an unmodifiable {@link List} of tasks
     */
    public List<Task> getAll() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Finds tasks whose description contains the given keyword.
     *
     * @param keyword the keyword to search for
     * @return a new {@link TaskList} containing matching tasks
     * @throws EloiseException if a matching task cannot be added
     */
    public TaskList findTasks(String keyword) throws EloiseException {
        TaskList matches = new TaskList();

        for (Task t : tasks) {
            if (t.getDescription().contains(keyword)) {
                matches.addTask(t);
            }
        }
        return matches;
    }

    /**
     * Sorts the task list alphabetically by description.
     */
    public void sortByDescription() {
        tasks.sort(Comparator.comparing(Task::getDescription));
    }

    /**
     * Sorts the task list chronologically by date.
     * <p>
     * Tasks without a date are placed after tasks with a date.
     */
    public void sortByDate() {
        tasks.sort(Comparator.comparing(Task::getDateTime,
                Comparator.nullsLast(Comparator.naturalOrder())));
    }

    /**
     * Returns a formatted string representation of the task list.
     * <p>
     * Each task is numbered starting from 1, followed by its details.
     *
     * @return a string representing the numbered list of tasks, or a message if empty
     */
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