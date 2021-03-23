
package boundarywalk.concepts;

import java.util.concurrent.Future;

/**
 *
 * @author alessioferri
 */
public interface VirtualRobotController {
    
    public Future<RoomMap> walkBoundary();
    
}
