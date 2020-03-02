package com.wosmart.ukprotocollibary.applicationlayer;

import com.wosmart.ukprotocollibary.model.data.DeviceFunction;
import com.wosmart.ukprotocollibary.model.data.ReminderFunction;
import com.wosmart.ukprotocollibary.model.data.SportFunction;

public class ApplicationLayerFunctionPacket {

    private final int FUNCTION_LENGTH = 4;

    private SportFunction sportFunction;

    private ReminderFunction reminderFunction;

    private DeviceFunction deviceFunction;

    public ApplicationLayerFunctionPacket() {
        this.sportFunction = new SportFunction();
        this.reminderFunction = new ReminderFunction();
        this.deviceFunction = new DeviceFunction();
    }

    public SportFunction getSportFunction() {
        return sportFunction;
    }

    public void setSportFunction(SportFunction sportFunction) {
        this.sportFunction = sportFunction;
    }

    public ReminderFunction getReminderFunction() {
        return reminderFunction;
    }

    public void setReminderFunction(ReminderFunction reminderFunction) {
        this.reminderFunction = reminderFunction;
    }

    public DeviceFunction getDeviceFunction() {
        return deviceFunction;
    }

    public void setDeviceFunction(DeviceFunction deviceFunction) {
        this.deviceFunction = deviceFunction;
    }

    public boolean parseData(byte[] data) {
        if ((data.length < FUNCTION_LENGTH)) {
            return false;
        }
        this.sportFunction.parseData(data);
        this.reminderFunction.parseData(data);
        this.deviceFunction.parseData(data);
        return true;
    }

    @Override
    public String toString() {
        return "ApplicationLayerFunctionPacket{" +
                "sportFunction=" + sportFunction +
                ", reminderFunction=" + reminderFunction +
                ", deviceFunction=" + deviceFunction +
                '}';
    }
}
