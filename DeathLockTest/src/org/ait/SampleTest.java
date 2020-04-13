package org.ait;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class SampleTest {
    public static void main(String[] argv) throws Exception {
        /*
         * Create deadlocked threads
         */
        Deadlock dl = new Deadlock();

        // Get the Platform MBean Server
	MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

    	// Create the Hello World MBean
    	Hello mbean = new Hello();
        mbean.setCacheSize(150);

        try {
	    // Construct the ObjectName for the MBean we will register
    	    ObjectName name = new ObjectName("com.sun.example:type=Hello");
    
            // Register the Hello World MBean
    	    mbs.registerMBean(mbean, name);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("\nPress <Enter> to exit this SampleTest program.\n");
        waitForEnterPressed();
    }

    private static void waitForEnterPressed() {
        try {
            boolean done = false;
            while (!done) {
                char ch = (char) System.in.read();
                if (ch < 0 || ch == '\n') {
                    done = true;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
