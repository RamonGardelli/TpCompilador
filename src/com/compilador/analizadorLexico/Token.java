package com.compilador.analizadorLexico;

public class Token {

    private int tokenId;
    private String reading;
    private int numLine;

    public Token(int tokenId, String reading, int numLine) {
        this.tokenId = tokenId;
        this.reading = reading;
        this.numLine = numLine;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public int getNumLine() {
        return numLine;
    }

    public void setNumLine(int numLine) {
        this.numLine = numLine;
    }
}
