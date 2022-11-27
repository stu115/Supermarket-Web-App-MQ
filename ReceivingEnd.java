package ds.jms.examples.client;

import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 *  *Example of queues of messages to work with wildfly-10

 * 
 */
public class ReceivingEnd {
    
    private static final Logger log = Logger.getLogger(ReceivingEnd.class.getName());

    // Set up all the default values
  private static final String DEFAULT_CONNECTION_FACTORY = "jms/OuterConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/stockLine";
    private static final String DEFAULT_MESSAGE_COUNT = "5";  
    private static final String DEFAULT_USERNAME = "appuser1";
    private static final String DEFAULT_PASSWORD = "qwert321#";      


    public static void main(String[] args) {

        Context contextOfName = null;

        try {
            String userName = System.getProperty("username", DEFAULT_USERNAME);
            String password = System.getProperty("password", DEFAULT_PASSWORD);

            // Set up the namingContext for the JNDI lookup
            contextOfName = CenterToBuild.makingOfContext(userName, password);
            
            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("center where connection is made", DEFAULT_CONNECTION_FACTORY);
            log.info("Trying to find the center where connection is made \"" + connectionFactoryString + "\"");
            
            ConnectionFactory connectionFactory = (ConnectionFactory) contextOfName.lookup(connectionFactoryString);
            log.info("Got to the center where connection is made \"" + connectionFactoryString + "\" in JNDI");

            String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
            log.info("Trying to find place to send \"" + destinationString + "\"");
            
            Destination destination = (Destination) contextOfName.lookup(destinationString);
            log.info("Got to the place to send  \"" + destinationString + "\" in JNDI");
            
            int count = Integer.parseInt(System.getProperty("number of messages", DEFAULT_MESSAGE_COUNT));

            try ( JMSContext context = connectionFactory.createContext(userName, password) ) {
                // Create the JMS consumer
                JMSConsumer consumer = context.createConsumer(destination);
                // Then receive the same number of messages that were sent
                for (int i = 0; i < count; i++) {
                    String rmdr = consumer.receiveBody(String.class, 5000);
                    System.out.println("Got the reminder which is " + rmdr);
                }
            }
        } catch (Exception e) {
            log.severe(e.getMessage());
            e.printStackTrace();
        } finally {
            if (contextOfName != null) {
                try {
                    contextOfName.close();
                } catch (NamingException e) {
                    log.severe(e.getMessage());
                }
            }
        }
    }
}



