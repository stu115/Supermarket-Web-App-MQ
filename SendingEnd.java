package ds.jms.examples.client;

import java.util.logging.Logger;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.naming.Context;
import javax.naming.NamingException;

/**
 *Example of queues of messages to work with wildfly-10
 * 
 */
public class SendingEnd {
    private static final Logger log = Logger.getLogger(SendingEnd.class.getName());

    // Set the first values such as the Center Of Connection which is the factory
    //Connection Center is where the connections are created
   private static final String DEFAULT_MESSAGE = "Add 10 packs of Casio watches to stock";
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

            // Set the context of the name, to do the looking for which is JNDI looking for
            contextOfName = CenterToBuild.makingOfContext(userName, password);
            
            // Carry out the looking for which is JNDI looking for
            String connectionFactoryString = System.getProperty("center where connection is made", DEFAULT_CONNECTION_FACTORY);
            log.info("Trying to find the center where connection is made \"" + connectionFactoryString + "\"");
            
            ConnectionFactory connectionFactory = (ConnectionFactory) contextOfName.lookup(connectionFactoryString);
            log.info("Got to the center where connection is made \"" + connectionFactoryString + "\" thanks to the JNDI");

            String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
            log.info("Trying to find place to send \"" + destinationString + "\"");
            
            Destination destination = (Destination) contextOfName.lookup(destinationString);
            log.info("Got to the place to send \"" + destinationString + "\" thanks to the JNDI");

            int count = Integer.parseInt(System.getProperty("number of messages", DEFAULT_MESSAGE_COUNT));
            String content = System.getProperty("message is ", DEFAULT_MESSAGE);

            try ( JMSContext context = connectionFactory.createContext(userName, password) ) {
                //Pass the messages with their count
                for (int i = 0; i < count; i++) {
                    context.createProducer().send(destination, content);
                    System.out.println("Passing " + i + " reminders which have: " + content);
                }
            }
        } catch (NamingException e) {
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
