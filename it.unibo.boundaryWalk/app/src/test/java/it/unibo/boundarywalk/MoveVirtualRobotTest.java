package it.unibo.boundarywalk;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class MoveVirtualRobotTest {
    private MoveVirtualRobot appl;

    private int index;
    private int[] dList;

    private void checkUpdate(int i) {
        if ( i < 0 ) {
            index++;
            if ( index == 4 ) {
                index = 0;
            }
        } else {
            dList[index] += i;
            System.out.println("Total " + dList[index]);
        }
    }

    @Before
    public void systemSetUp() {
        System.out.println("TestMoveVirtualRobot | setUp: robot should be at HOME-DOWN ");
        appl = new MoveVirtualRobot();
        index = 0;
        dList = new int[]{0,0,0,0};
    }

    @After
    public void  terminate() {
        System.out.println("%%%  TestMoveVirtualRobot |  terminates ");
    }

    //@Test
    public void testMovesNoFailing() {
        System.out.println("TestMoveVirtualRobot | testWork ");
        boolean moveFailed = appl.moveLeft(300);
        assertTrue( ! moveFailed  );
        moveFailed = appl.moveRight(1000);    //back to DOWN
        assertTrue( ! moveFailed  );
        moveFailed = appl.moveStop(100);
        assertTrue( ! moveFailed  );
    }

    //@Test
    public void testMoveForwardNoHit() {
        System.out.println("TestMoveVirtualRobot | testMoveForward ");
        boolean moveFailed = appl.moveForward(600);
        assertTrue( ! moveFailed  );
        moveFailed = appl.moveBackward(600);  //back to home
        assertTrue( ! moveFailed  );
    }

    //@Test
    public void testMoveForwardHit() {
        System.out.println("TestMoveVirtualRobot | testMoveForward ");
        boolean moveFailed = appl.moveForward(1600);
        assertTrue( moveFailed  );
        moveFailed = appl.moveBackward(1600);       //back to home
        assertTrue( moveFailed  );
    }

    @Test
    public void testWalkBoundary() {
        System.out.println("TestMoveVirtualRobot | testBoundaryWalk ");
        appl.moveBackward(1000);
        appl.boundaryWalk(this::checkUpdate);
        assertTrue(index == 0);
        System.out.println(Arrays.toString(dList) );
        assertTrue(dList[0] == dList[2] && dList[1] == dList[3]);
    }

}