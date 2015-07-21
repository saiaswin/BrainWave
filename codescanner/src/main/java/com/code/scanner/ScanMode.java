package com.code.scanner;

public enum ScanMode {
    QRCODE("QR_CODE_MODE"),
    BARCODE("BAR_CODE_MODE");

    private String mode;

    ScanMode(String mode){
        this.mode=mode;
    }

    @Override
    public String toString() {
        return mode;
    }
}
