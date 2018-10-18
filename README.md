# Remote EJB quickstart

Sample app showing (some) usages of JBoss EJB Client.

## Usage

1. Create Wildfly application user and start Wildfly node:

    ```
    cd $WILDFLY_HOME/bin
    ./add-user.sh -u user -p secret -a
    ./standalone.sh
    ```

    If clustering capabilities are required, you can start two nodes with '-ha' config instead:

    ```
    ./standalone.sh -c standalone-ha.xml
    ./standalone.sh -c standalone-ha.xml -Djboss.socket.binding.port-offset=100 -Djboss.node.name=node2
    ```

2. Compile this quickstart:

    ```
    cd $QS_HOME
    mvn clean package
    ```
3. Deploy server-side JAR to Wildfly node(s):
    ```
    cp remote-ejb-server-side/target/remote-ejb-server-side.jar $WILDFLY_HOME/standalone/deployments/
    ```
4. Run the client of your choice - see classes in _remote-ejb-standalone-client_ module.
