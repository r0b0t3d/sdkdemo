package com.wosmart.ukprotocollibary.model.data;

import com.wosmart.ukprotocollibary.model.enums.DeviceFunctionStatus;

/**
 * device support function
 */
public class DeviceFunction {
    private DeviceFunctionStatus BloodPressure;
    private DeviceFunctionStatus Rate;
    private DeviceFunctionStatus ScreenOrientationSetting;
    private DeviceFunctionStatus Step;
    private DeviceFunctionStatus Sleep;
    private DeviceFunctionStatus WeRun;
    private DeviceFunctionStatus ScreenLightDurationSetting;
    private DeviceFunctionStatus HourSystemSetting;
    private DeviceFunctionStatus UnitSetting;
    private DeviceFunctionStatus Disturb;

    public DeviceFunction() {
    }

    public DeviceFunction(DeviceFunctionStatus bloodPressure, DeviceFunctionStatus rate, DeviceFunctionStatus screenOrientationSetting, DeviceFunctionStatus step, DeviceFunctionStatus sleep, DeviceFunctionStatus weRun, DeviceFunctionStatus screenLightDurationSetting, DeviceFunctionStatus hourSystemSetting, DeviceFunctionStatus unitSetting, DeviceFunctionStatus disturb) {
        BloodPressure = bloodPressure;
        Rate = rate;
        ScreenOrientationSetting = screenOrientationSetting;
        Step = step;
        Sleep = sleep;
        WeRun = weRun;
        ScreenLightDurationSetting = screenLightDurationSetting;
        HourSystemSetting = hourSystemSetting;
        UnitSetting = unitSetting;
        Disturb = disturb;
    }

    public DeviceFunctionStatus getBloodPressure() {
        return BloodPressure;
    }

    public void setBloodPressure(DeviceFunctionStatus bloodPressure) {
        BloodPressure = bloodPressure;
    }

    public DeviceFunctionStatus getRate() {
        return Rate;
    }

    public void setRate(DeviceFunctionStatus rate) {
        Rate = rate;
    }

    public DeviceFunctionStatus getScreenOrientationSetting() {
        return ScreenOrientationSetting;
    }

    public void setScreenOrientationSetting(DeviceFunctionStatus screenOrientationSetting) {
        ScreenOrientationSetting = screenOrientationSetting;
    }

    public DeviceFunctionStatus getStep() {
        return Step;
    }

    public void setStep(DeviceFunctionStatus step) {
        Step = step;
    }

    public DeviceFunctionStatus getSleep() {
        return Sleep;
    }

    public void setSleep(DeviceFunctionStatus sleep) {
        Sleep = sleep;
    }

    public DeviceFunctionStatus getWeRun() {
        return WeRun;
    }

    public void setWeRun(DeviceFunctionStatus weRun) {
        WeRun = weRun;
    }

    public DeviceFunctionStatus getScreenLightDurationSetting() {
        return ScreenLightDurationSetting;
    }

    public void setScreenLightDurationSetting(DeviceFunctionStatus screenLightDurationSetting) {
        ScreenLightDurationSetting = screenLightDurationSetting;
    }

    public DeviceFunctionStatus getHourSystemSetting() {
        return HourSystemSetting;
    }

    public void setHourSystemSetting(DeviceFunctionStatus hourSystemSetting) {
        HourSystemSetting = hourSystemSetting;
    }

    public DeviceFunctionStatus getUnitSetting() {
        return UnitSetting;
    }

    public void setUnitSetting(DeviceFunctionStatus unitSetting) {
        UnitSetting = unitSetting;
    }

    public DeviceFunctionStatus getDisturb() {
        return Disturb;
    }

    public void setDisturb(DeviceFunctionStatus disturb) {
        Disturb = disturb;
    }

    public boolean parseData(byte[] data) {
        int bp = data[3] >> 1 & 0x01;
        int rate = data[3] >> 2 & 0x01;
        int screenOrientation = data[3] >> 3 & 0x01;
        int step = data[3] >> 4 & 0x01;
        int sleep = data[3] >> 5 & 0x01;
        int weRun = data[3] >> 6 & 0x01;
        int screenLightDuration = data[3] >> 7 & 0x01;
        int hourSystem = data[2] >> 1 & 0x01;
        int unit = data[2] >> 2 & 0x01;
        int disturb = data[2] >> 3 & 0x01;

        if (bp == 1) {
            this.BloodPressure = DeviceFunctionStatus.SUPPORT;
        } else {
            this.BloodPressure = DeviceFunctionStatus.UN_SUPPORT;
        }

        if (rate == 1) {
            this.Rate = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Rate = DeviceFunctionStatus.UN_SUPPORT;
        }

        if (screenOrientation == 1) {
            this.ScreenOrientationSetting = DeviceFunctionStatus.SUPPORT;
        } else {
            this.ScreenOrientationSetting = DeviceFunctionStatus.UN_SUPPORT;
        }

        if (step == 1) {
            this.Step = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Step = DeviceFunctionStatus.UN_SUPPORT;
        }

        if (sleep == 1) {
            this.Sleep = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Sleep = DeviceFunctionStatus.UN_SUPPORT;
        }

        if (weRun == 1) {
            this.WeRun = DeviceFunctionStatus.SUPPORT;
        } else {
            this.WeRun = DeviceFunctionStatus.UN_SUPPORT;
        }

        if (screenLightDuration == 1) {
            this.ScreenLightDurationSetting = DeviceFunctionStatus.SUPPORT;
        } else {
            this.ScreenLightDurationSetting = DeviceFunctionStatus.UN_SUPPORT;
        }

        if (hourSystem == 1) {
            this.HourSystemSetting = DeviceFunctionStatus.SUPPORT;
        } else {
            this.HourSystemSetting = DeviceFunctionStatus.UN_SUPPORT;
        }

        if (unit == 1) {
            this.UnitSetting = DeviceFunctionStatus.SUPPORT;
        } else {
            this.UnitSetting = DeviceFunctionStatus.UN_SUPPORT;
        }

        if (disturb == 1) {
            this.Disturb = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Disturb = DeviceFunctionStatus.UN_SUPPORT;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DeviceFunction{" +
                "Disturb=" + Disturb +
                ", UnitSetting=" + UnitSetting +
                ", HourSystemSetting=" + HourSystemSetting +
                ", ScreenLightDurationSetting=" + ScreenLightDurationSetting +
                ", WeRun=" + WeRun +
                ", Sleep=" + Sleep +
                ", Step=" + Step +
                ", ScreenOrientationSetting=" + ScreenOrientationSetting +
                ", Rate=" + Rate +
                ", BloodPressure=" + BloodPressure +
                '}';
    }


}
