package com.cscodetech.urbantaxi.imagepicker;

public interface ImageCompressionListener {
    void onStart();

    void onCompressed(String filePath);
}
