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

import boundarywalk.concepts.VirtualRobotObservable;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.json.JSONObject;
import boundarywalk.concepts.VirtualRobotClient;

/**
 *
 * @author alessioferri
 */
@ClientEndpoint
public class VirtualRobotWS implements VirtualRobotObservable, VirtualRobotClient {

    private final String URL;
    private Session userSession = null;
    private final List<Consumer<JSONObject>> observers;

    public VirtualRobotWS(String url) {
        this.URL = url;
        this.observers = new ArrayList<>();
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://" + url));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("VirtualRobotWS | ERROR: " + e.getMessage());
        }
    }

    //Callback hook for Connection open events.
    @OnOpen
    public void onOpen(Session userSession) { //, @PathParam("username") String username, EndpointConfig epConfig
        //ClientEndpointConfig clientConfig = (ClientEndpointConfig) epConfig;
        Principal userPrincipal = userSession.getUserPrincipal();
        if (userPrincipal != null) { //there is an authenticated user
            System.out.println("        VirtualRobotWS | onOpen user=" + userPrincipal.getName());
        }
        this.userSession = userSession;
    }

    //Callback hook for Connection close events.
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("VirtualRobotWS | closing websocket because " + reason.getReasonPhrase());
        this.userSession = null;
    }

    //Callback hook for Message Events.
    //THe WENv system sends always an answer for any command sent to it
    @OnMessage
    public void onMessage(String message) {
        try {
            //{"collision":"true ","move":"..."} or {"sonarName":"sonar2","distance":19,"axis":"x"}
            //System.out.println("        IssWsSupport | onMessage:" + message);
            JSONObject jsonObj = new JSONObject(message);
            for (var observer : this.observers) {
                observer.accept(jsonObj);
            }

        } catch (Exception e) {
            System.out.println("        VirtualRobotWS | onMessage ERROR " + e.getMessage());

        }
    }

    @OnError
    public void disconnected(Session session, Throwable error) {
        System.out.println("VirtualRobotWS | disconnected  " + error.getMessage());
    }

    public void forward(String msg) {
        try {
            //System.out.println("        IssWsSupport | forward:" + msg);
            //this.userSession.getAsyncRemote().sendText(message);
            userSession.getBasicRemote().sendText(msg); //synch: blocks until the message has been transmitted
            //System.out.println("        IssWsSupport | DONE forward " + msg);
        } catch (Exception e) {
            System.out.println("        VirtualRobotWS | ERROR forward  " + e.getMessage());
        }
    }

    @Override
    public void request(String msg) {
        try {
            //System.out.println("        IssWsSupport | request " + msg);
            //this.userSession.getAsyncRemote().sendText(message);
            this.userSession.getBasicRemote().sendText(msg);    //synch: blocks until the message has been transmitted
        } catch (Exception e) {
            System.out.println("        VirtualRobotWS | request ERROR " + e.getMessage());
        }
    }

    public void close() {
        try {
            userSession.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(Consumer<JSONObject> consumer) {
        this.observers.add(consumer);
    }

    @Override
    public void unregister(Consumer<JSONObject> consumer) {
        this.observers.remove(consumer);
    }

}
