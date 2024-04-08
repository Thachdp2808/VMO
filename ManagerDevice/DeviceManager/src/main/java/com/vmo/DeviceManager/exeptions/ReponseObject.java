package com.vmo.DeviceManager.exeptions;

public class ReponseObject {
    private String status;
    private String messages;
    private Object object;

    public ReponseObject(String status, String messages, Object object) {
        this.status = status;
        this.messages = messages;
        this.object = object;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "ReponseObject{" +
                "status='" + status + '\'' +
                ", messages='" + messages + '\'' +
                ", object=" + object +
                '}';
    }
}
