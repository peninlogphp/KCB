package lipuka.android.model.database;



import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class AsyncDatabaseRead {
   
    private ThreadPoolExecutor threadPool;

    public AsyncDatabaseRead() {
        threadPool = (ThreadPoolExecutor)Executors.newCachedThreadPool();
    }

   
    public void submitTask(DatabaseReadTask task) {
   threadPool.submit(task);
    }
}