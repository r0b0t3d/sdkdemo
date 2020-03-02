package com.wosmart.ukprotocollibary.applicationlayer;

import com.realsil.realteksdk.logger.ZLogger;

public class ApplicationLayerDisturbPacket {

    private boolean isOpen;

    private int startHour;

    private int startMinute;

    private int endHour;

    private int endMinute;

    // Packet Length
    public final static int DISTURB_HEADER_LENGTH = 3;

    public ApplicationLayerDisturbPacket() {
    }

    public ApplicationLayerDisturbPacket(boolean isOpen, int startHour, int startMinute, int endHour, int endMinute) {
        this.isOpen = isOpen;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(int endMinute) {
        this.endMinute = endMinute;
    }

    public byte[] getPacket() {
        byte[] data = new byte[DISTURB_HEADER_LENGTH];
        data[0] = (byte) (((isOpen ? 1 : 0) << 6) | (startHour << 1) | (startMinute >> 5));
        data[1] = (byte) ((startMinute << 3) | (endHour >> 2));
        data[2] = (byte) ((endHour << 6) | (endMinute) | (0x00));
        ZLogger.i("isOpen: " + isOpen +
                ", startHour: " + startHour +
                ", startMinute: " + startMinute +
                ", endHour: " + endHour +
                ", endMinute: " + endMinute);
        return data;
    }

    public boolean parseData(byte[] data) {
        isOpen = ((data[0] >> 6 & 0x01) == 0x01) ? true : false;
        startHour = data[0] >> 1 & 0x17;
        startMinute = (data[0] << 5 & 0x10) | (data[1] >> 3 & 0x0f);
        endHour = (data[1] << 2) | data[2] >> 6 & 0x0f;
        ZLogger.i("isOpen: " + isOpen +
                ", startHour: " + startHour +
                ", startMinute: " + startMinute +
                ", endHour: " + endHour +
                ", endMinute: " + endMinute);
        return true;
    }

    @Override
    public String toString() {
        return "ApplicationLayerDisturbPacket{" +
                "isOpen=" + isOpen +
                ", startHour=" + startHour +
                ", startMinute=" + startMinute +
                ", endHour=" + endHour +
                ", endMinute=" + endMinute +
                '}';
    }
}
