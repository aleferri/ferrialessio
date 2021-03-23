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

import boundarywalk.concepts.VirtualRobotClient;

/**
 *
 * @author alessioferri
 */
public class VirtualRobotClientARIL implements VirtualRobotClient {

    private final VirtualRobotClient embed;

    public VirtualRobotClientARIL(VirtualRobotClient embed) {
        this.embed = embed;
    }

    @Override
    public void request(String arilMove) {
        this.embed.request(this.translate(arilMove));
    }

    private String translate(String arilMove) {
        switch (arilMove.trim()) { //translate into cril move
            case "h":
                return "{\"robotmove\":\"alarm\", \"time\": " + RobotMessages.HTIME + "}";
            case "w":
                return "{\"robotmove\":\"moveForward\", \"time\": " + RobotMessages.WTIME + "}";
            case "s":
                return "{\"robotmove\":\"moveBackward\", \"time\": " + RobotMessages.STIME + "}";
            case "l":
                return "{\"robotmove\":\"turnLeft\", \"time\": " + RobotMessages.LTIME + "}";
            case "r":
                return "{\"robotmove\":\"turnRight\", \"time\":" + RobotMessages.RTIME + "}";
            default:
                return "{\"robotmove\":\"alarm\", \"time\": " + RobotMessages.HTIME + "}"; //to avoid exceptions
        }
    }

}
