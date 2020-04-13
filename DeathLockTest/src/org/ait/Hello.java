package org.ait;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.AttributeChangeNotification;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class Hello
	extends NotificationBroadcasterSupport implements HelloMBean {

    private Logger helloLogger;
    public Hello() {
        this.helloLogger = Logger.getLogger("com.sun.example.hello");

        // Set the ConsoleHandler level to FINEST so that log messages 
        // will be printed to System.err for this demo when the log level 
        // gets changed to lower level.  
        // The default level for ConsoleHandler is INFO.
        if (helloLogger.getUseParentHandlers()) {
            Logger parent = helloLogger.getParent();
            if (parent != null) {
                Handler[] ha = parent.getHandlers();
                for (Handler h : ha) {
                    if (h instanceof ConsoleHandler) {
                        h.setLevel(Level.FINEST);
                    }
                }
            }
        }
    }

    public void sayHello() {
	System.out.println("hello, world");
    }

    public int add(int x, int y) {
	return x + y;
    }

    /* Getter for the Name attribute.  The pattern shown here is
       frequent: the getter returns a private field representing the
       attribute value.  In our case, the attribute value never
       changes, but for other attributes it might change as the
       application runs.  Consider an attribute representing
       statistics such as uptime or memory usage, for example.  Being
       read-only just means that it can't be changed through the
       management interface.  */
    public String getName() {
	return this.name;
    }

    /* Getter for the CacheSize attribute.  The pattern shown here is
       frequent: the getter returns a private field representing the
       attribute value, and the setter changes that field.  */
    public int getCacheSize() {
	return this.cacheSize;
    }

    /* Setter for the CacheSize attribute.  To avoid problems with
       stale values in multithreaded situations, it is a good idea
       for setters to be synchronized.  */
    public synchronized void setCacheSize(int size) {
        helloLogger.entering("Hello", "setCacheSize", new Integer(size));
	int oldSize = this.cacheSize;
	this.cacheSize = size;

	/* In a real application, changing the attribute would
	   typically have effects beyond just modifying the cacheSize
	   field.  For example, resizing the cache might mean
	   discarding entries or allocating new ones.  The logic for
	   these effects would be here.  */
	// System.out.println("Cache size now " + this.cacheSize);

	/* Construct a notification that describes the change.  The
	   "source" of a notification is the ObjectName of the MBean
	   that emitted it.  But an MBean can put a reference to
	   itself ("this") in the source, and the MBean server will
	   replace this with the ObjectName before sending the
	   notification on to its clients.

	   For good measure, we maintain a sequence number for each
	   notification emitted by this MBean.
	
	   The oldValue and newValue parameters to the constructor are
	   of type Object, so we are relying on Tiger's autoboxing
	   here.  */
	Notification n =
	    new AttributeChangeNotification(this,
					    sequenceNumber++,
					    System.currentTimeMillis(),
					    "CacheSize changed",
					    "CacheSize",
					    "int",
					    oldSize,
					    this.cacheSize);

	/* Now send the notification using the sendNotification method
	   inherited from the parent class
	   NotificationBroadcasterSupport.  */
        helloLogger.info("AttributeChangeNotification sent");
	sendNotification(n);

        helloLogger.exiting("Hello", "setCacheSize");
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
	String[] types = new String[] {
	    AttributeChangeNotification.ATTRIBUTE_CHANGE
	};
	String name = AttributeChangeNotification.class.getName();
	String description = "An attribute of this MBean has changed";
	MBeanNotificationInfo info =
	    new MBeanNotificationInfo(types, name, description);
	return new MBeanNotificationInfo[] {info};
    }

    private final String name = "Reginald";
    private int cacheSize = DEFAULT_CACHE_SIZE;
    private static final int DEFAULT_CACHE_SIZE = 200;

    private long sequenceNumber = 1;
}
