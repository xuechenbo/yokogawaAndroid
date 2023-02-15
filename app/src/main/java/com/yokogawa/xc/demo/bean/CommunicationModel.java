package com.yokogawa.xc.demo.bean;

public class CommunicationModel {


    public interface CommunicationModelDelegate {

        void onReceiveReadInformationResponse(int result, byte readResult, byte[] receiveData);

        void onReceiveWriteInformationStartResponse(byte receiveData);
    }

    public void setModelCallback(CommunicationModel.CommunicationModelDelegate delegate) {

    }
}
