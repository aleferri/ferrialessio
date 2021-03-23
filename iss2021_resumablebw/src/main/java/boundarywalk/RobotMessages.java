package boundarywalk;

/**
 *
 * @author alessioferri
 */
public class RobotMessages {

    public static final String W_MSG = "w";
    public static final String L_MSG = "l";
    public static final String R_MSG = "r";
    public static final String S_MSG = "s";
    public static final String H_MSG = "h";
    
    public static final int WTIME    = 400;
    public static final int STIME    = WTIME;
    public static final int LTIME    = 300;
    public static final int RTIME    = LTIME;
    public static final int HTIME    = 100;

    public static final AppMessage AHEAD = AppMessage.create("msg(robotcmd,dispatch,appl,wenv," + W_MSG + " )");
    public static final AppMessage LEFT = AppMessage.create("msg(robotcmd,dispatch,appl,wenv," + L_MSG + ")");
    public static final AppMessage RIGHT = AppMessage.create("msg(robotcmd,dispatch,appl,wenv," + R_MSG + ")");
    public static final AppMessage BACK = AppMessage.create("msg(robotcmd,dispatch,appl,wenv," + S_MSG + ")");
    public static final AppMessage HALT = AppMessage.create("msg(robotcmd,dispatch,appl,wenv," + H_MSG + ")");

}
