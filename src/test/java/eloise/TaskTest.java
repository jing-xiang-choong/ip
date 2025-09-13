package eloise;

import eloise.task.Task;
import eloise.task.ToDo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TaskTest {
    @Test
    public void shouldMarkTaskAsDone_whenMarkDoneIsCalled() {
        Task t = new ToDo("play games");
        t.mark();
        assertTrue(t.getIsDone()); //check state, whether it has been marked
        assertEquals("[T][X] play games", t.toString());
    }
}
