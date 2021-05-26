package ru.example.netty.model;

/**
 * @author TaylakovSA
 */
public class ResponseData {

    private int intValue;


    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return "ru.example.netty.model.ResponseData{" +
                "intValue=" + intValue +
                '}';
    }
}
