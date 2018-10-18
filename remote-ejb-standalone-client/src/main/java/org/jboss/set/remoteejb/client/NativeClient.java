package org.jboss.set.remoteejb.client;

import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.ejb.client.EJBClientContext;
import org.jboss.ejb.protocol.remote.RemoteTransportProvider;
import org.jboss.logging.Logger;
import org.jboss.set.remoteejb.server.RemoteCalculator;
import org.wildfly.naming.client.WildFlyInitialContextFactory;

public class NativeClient {

    private static final String HOST = "localhost";
    private static final int PORT = 8080;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "secret";
    private static final String LOOKUP_NAME = "ejb:/remote-ejb-server-side/CalculatorBean!"
            + RemoteCalculator.class.getName();

    private static final Logger LOGGER = Logger.getLogger(NativeClient.class);

    public static void main(String[] args) throws NamingException, PrivilegedActionException {

        // 1. Default EJBClientContext

        RemoteCalculator remote = (RemoteCalculator) getInitialContext(HOST, PORT, USERNAME, PASSWORD)
                .lookup(LOOKUP_NAME);
        int result = remote.add(3, 5);
        System.out.println("Result is " + result);


        // 2. Custom EJBClientContext

        EJBClientContext context = new EJBClientContext.Builder()
                .setMaximumConnectedClusterNodes(5)
                .addTransportProvider(new RemoteTransportProvider())
                .build();

        context.runExceptionAction(new PrivilegedExceptionAction<Object>() {
            public Object run() throws Exception {
                RemoteCalculator remote = (RemoteCalculator) getInitialContext("localhost", 8080, "user", "secret")
                        .lookup("ejb:/remote-ejb-server-side/CalculatorBean!"
                                + RemoteCalculator.class.getName());
                int result = remote.add(3, 5);
                System.out.println("Result is " + result);
                return null;
            }
        });
    }

    private static Context getInitialContext(String host, Integer port, String username, String password) throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, WildFlyInitialContextFactory.class.getName());
        props.put(Context.PROVIDER_URL, String.format("%s://%s:%d", "remote+http", host, port));
        if (username != null && password != null) {
            props.put(Context.SECURITY_PRINCIPAL, username);
            props.put(Context.SECURITY_CREDENTIALS, password);
        }
        return new InitialContext(props);
    }

}
