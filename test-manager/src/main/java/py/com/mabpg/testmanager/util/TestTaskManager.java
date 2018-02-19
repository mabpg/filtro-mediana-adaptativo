package py.com.mabpg.testmanager.util;



import java.util.concurrent.Callable;
import py.com.mabpg.tesisrgb.generics.BasicFilterAbstract;

/**
 *
 * @author Derlis Argüello
 */
public class TestTaskManager implements Callable<TaskResult>{
    
    private final TaskResult taskResult;

    public TestTaskManager(BasicFilterAbstract test, Double varianza, Double indice, int ventanas) {
        this.taskResult = new TaskResult(test, indice, varianza, ventanas);
    }

    public TaskResult getTaskResult() {
        return taskResult;
    }
    
    @Override
    public TaskResult call() throws Exception {
        taskResult.setColProcessor(taskResult.getBasicAbstract().run());
        return taskResult;
    }
    
}
