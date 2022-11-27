package ds.jms.examples.client;

import java.util.Properties;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CenterToBuild {

    private static final Logger log = Logger.getLogger(CenterToBuild.class.getName());

    // Set up all the default values 
  private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8085";

    public static Context makingOfContext(String userName, String password) {

      Context contextOfName = null;
        try {
            // Set up the namingContext for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
            env.put(Context.SECURITY_PRINCIPAL, userName);
            env.put(Context.SECURITY_CREDENTIALS, password);
            contextOfName = new InitialContext(env);
            return contextOfName;
        } catch (NamingException e) {
            log.severe(e.getMessage());
            e.printStackTrace();
        }
        return contextOfName;
    }

}
