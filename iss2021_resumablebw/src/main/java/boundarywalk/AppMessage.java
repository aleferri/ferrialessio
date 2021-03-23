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

/**
 *
 * @author alessioferri
 */
public class AppMessage {

    private static int n = 0;
    private String MSGID;
    private String MSGTYPE;
    private String SENDER;
    private String RECEIVER;
    private String CONTENT;
    private String SEQNUM;

    private enum AppMsgType {
        event, dispatch, request, reply, invitation
    };

    public AppMessage(String MSGID, String MSGTYPE, String SENDER, String RECEIVER, String CONTENT, String SEQNUM) {
        this.MSGID = MSGID;
        this.MSGTYPE = MSGTYPE;
        this.SENDER = SENDER;
        this.RECEIVER = RECEIVER;
        this.CONTENT = CONTENT;
        this.SEQNUM = SEQNUM;
    }

    //Factory method
    public static AppMessage create(String MSGID, String MSGTYPE, String SENDER, String RECEIVER, String CONTENT) {
        //System.out.println("AppMsg create/5 " + CONTENT);
        return new AppMessage(MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, "" + n++);
    }

    public static AppMessage create(String m) {  //WARNING: CONTENT must be in Prolog syntax
        //System.out.println("AppMsg create m= " + m);
        //m= msg( MSGID,  MSGTYPE,  SENDER,  RECEIVER,  CONTENT, SEQNUM )
        String[] mSplit = m.split("msg");
        String mBody[] = mSplit[1].replace("(", "").split(",");
        String content = mBody[4].replace(")", "");
        return AppMessage.create(mBody[0], mBody[1], mBody[2], mBody[3], content);
    }

    public boolean isEvent() {
        return MSGTYPE.equals(AppMsgType.event.toString());
    }

    public boolean isDispatch() {
        return MSGTYPE.equals(AppMsgType.dispatch.toString());
    }

    public boolean isRequest() {
        return MSGTYPE.equals(AppMsgType.request.toString());
    }

    public boolean isReply() {
        return MSGTYPE.equals(AppMsgType.reply.toString());
    }

    public String getMsgType() {
        return MSGTYPE;
    }

    public String getMsgid() {
        return MSGID;
    }

    public String getSender() {
        return SENDER;
    }

    public String getReceiver() {
        return RECEIVER;
    }

    public String getContent() {
        return CONTENT;
    }

    public String toString() {
        return "msg(MSGID,MSGTYPE,SENDER,RECEIVER,CONTENT,SEQNUM)"
                .replace("MSGID", MSGID)
                .replace("MSGTYPE", MSGTYPE)
                .replace("SENDER", SENDER)
                .replace("RECEIVER", RECEIVER)
                .replace("CONTENT", CONTENT)
                .replace("SEQNUM", SEQNUM);

    }
}//AppMsg
