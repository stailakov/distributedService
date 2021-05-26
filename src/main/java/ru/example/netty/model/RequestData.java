package ru.example.netty.model;

/**
 * @author TaylakovSA
 */
public class RequestData {

    private int intValue;
    private int term;
    private int peer;
    private String stringValue;

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public int getPeer() {
        return peer;
    }

    public void setPeer(int peer) {
        this.peer = peer;
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "intValue=" + intValue +
                ", term=" + term +
                ", peer=" + peer +
                ", stringValue='" + stringValue + '\'' +
                '}';
    }
}
