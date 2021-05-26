package ru.example.netty.model;

/**
 * @author TaylakovSA
 */
public class ResponseData {

    private int intValue;
    private int term;
    private int peer;


    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
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
        return "ResponseData{" +
                "intValue=" + intValue +
                ", term=" + term +
                ", peer=" + peer +
                '}';
    }
}
