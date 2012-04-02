import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Benchmark {
    private static final long WARMUP_NANO_TIME = 10L * 1000 * 1000 * 1000;
    
    static long execute(String file) throws Exception {
        FileInputStream fis = null;
        BufferedReader reader;

        long start = System.nanoTime();
        RBTreeMap map = RBTreeMap.newInstance();
        try {
            fis = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] records = line.split(",");
                map.put(records[0], records[1]);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }

        try {
            fis = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] records = line.split(",");
                if (!records[1].equals(map.get(records[0]))) {
                    throw new RuntimeException("value does not match: " + map.get(records[0]) + " != " + records[1] + " (" + map.height() + ")");
                }
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return System.nanoTime() - start;
    }

    public static void main(String[] args) throws Exception {
        String file = args[0];
        long start = System.nanoTime();
        while ((System.nanoTime() - start) < WARMUP_NANO_TIME) {
            execute(file);
        }
        System.out.println(execute(file));
    }
}
