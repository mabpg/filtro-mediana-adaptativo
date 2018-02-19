package py.com.mabpg.testmanager;

import py.com.mabpg.testmanager.util.TestImageManager;


public class TestMain {

    /**
     * @param args the command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        TestImageManager test = new TestImageManager();
        test.run();
        
    }
    
}