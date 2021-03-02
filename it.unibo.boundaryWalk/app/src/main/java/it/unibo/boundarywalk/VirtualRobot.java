package it.unibo.boundaryWalk;

import java.util.function.Consumer;

public interface VirtualRobot {
    
    public boolean moveForward(int duration);
    
    public boolean moveLeft(int duration);
    
    public default void boundaryWalk(Consumer<Integer> observer) {
        int d = 300;

        boolean collided;
        do {
            collided = this.moveForward(d);
            if ( ! collided ) {
                observer.accept(d);
            }
        } while(!collided);

        this.moveLeft( 300 );
        observer.accept(-1);

        do {
            collided = this.moveForward(d);
            if ( ! collided ) {
                observer.accept(d);
            }
        } while(!collided);

        this.moveLeft(300);
        observer.accept(-1);

        do {
            collided = this.moveForward(d);
            if ( ! collided ) {
                observer.accept(d);
            }
        } while(!collided);

        this.moveLeft(300);
        observer.accept(-1);

        do {
            collided = this.moveForward(d);
            if ( ! collided ) {
                observer.accept(d);
            }
        } while(!collided);

        this.moveLeft(300);
        observer.accept(-1);
    }
  
}
