package eloise;

import eloise.exception.EloiseException;
import eloise.exception.InvalidIndexException;
import eloise.task.Task;
import eloise.task.ToDo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import eloise.task.TaskList;

public class TaskListTest {
    @Test
    public void shouldRemoveTask_whenDeleteTaskCalled() throws EloiseException {
        TaskList list = new TaskList();
        Task t1 = new ToDo("play games");
        Task t2 = new ToDo("clean room");

        list.addTask(t1);
        list.addTask(t2);

        list.delete(2);
        assertEquals(1, list.size());
        assertEquals("[T][ ] play games", list.get(0).toString());
    }

    @Test
    public void markTask_throwsException() {
        TaskList list = new TaskList();
        Task t1 = new ToDo("play games");

        assertThrows(InvalidIndexException.class, () -> list.mark(2));
    }
}
