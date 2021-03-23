/*
 * Copyright (c) 2021, alessioferri
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package boundarywalk;

import boundarywalk.concepts.RoomMap;
import boundarywalk.concepts.VirtualRobotController;
import boundarywalk.concepts.VirtualRobotObservable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import org.json.JSONObject;
import boundarywalk.concepts.VirtualRobotClient;

/**
 *
 * @author alessioferri
 */
public class BoundaryWalkController implements VirtualRobotController {

    private final VirtualRobotClient operations;
    private final ExecutorService executor;
    private final RoomMap room;
    private boolean boundaryCompleted;
    private boolean interrupted;
    private final VirtualRobotObservable observable;
    private int countTurns;

    public BoundaryWalkController(VirtualRobotClient operations, VirtualRobotObservable observable) {
        this.operations = operations;
        this.executor = Executors.newSingleThreadExecutor();
        this.room = new GeneratedRoomMap();
        this.boundaryCompleted = false;
        this.observable = observable;
        this.countTurns = 0;
        this.interrupted = false;
    }

    @Override
    public Future<RoomMap> walkBoundary() {
        System.out.println("Called");
        this.boundaryCompleted = false;
        this.interrupted = false;
        final var self = this;
        final var exec = this.executor.submit(() -> {
            self.operations.request(RobotMessages.W_MSG);
            
            System.out.println(Thread.currentThread().isInterrupted());
            
            final Consumer<JSONObject> handler = (JSONObject endmove) -> {
                String answer = endmove.getString("endmove");
                String move = endmove.getString("move");
                
                if ( self.interrupted ) {
                    System.out.println("Skipping, as interrupted");
                    return;
                }

                System.out.println("Received move answer to move " + move);

                switch (answer) {
                    case "true":
                        self.handleBoundaryForward();
                        break;
                    case "false":
                        self.handleBoundaryTurn();
                        break;
                    default:
                        System.out.println("Nothing");
                        break;
                }
            };

            self.observable.register(handler);

            while (!self.boundaryCompleted && !Thread.currentThread().isInterrupted()) {}
            
            if ( Thread.currentThread().isInterrupted()) {
                System.out.println("Interrupted");
                self.interrupted = true;
            }
            
            self.observable.unregister(handler);

            return self.room;
        });
        return exec;
    }

    public void handleBoundaryForward() {
        if ( this.boundaryCompleted ) {
            return;
        }
        this.operations.request(RobotMessages.W_MSG);
    }

    public void handleBoundaryTurn() {
        this.operations.request(RobotMessages.L_MSG);
        if (this.countTurns == 3) {
            this.boundaryCompleted = true;
            this.countTurns = 0;
            return;
        }
        this.countTurns++;
    }
}
