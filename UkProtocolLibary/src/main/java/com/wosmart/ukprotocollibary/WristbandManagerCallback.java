package com.wosmart.ukprotocollibary;


import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerAlarmsPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerBpPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerFacSensorPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerHrpPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerLogResponsePacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSleepPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerSportPacket;
import com.wosmart.ukprotocollibary.applicationlayer.ApplicationLayerStepPacket;
import com.wosmart.ukprotocollibary.model.data.DeviceFunction;
import com.wosmart.ukprotocollibary.model.data.ReminderFunction;
import com.wosmart.ukprotocollibary.model.data.SportFunction;
import com.wosmart.ukprotocollibary.model.enums.DeviceLanguage;

public class WristbandManagerCallback {
    /**
     * Callback indicating when gatt connected/disconnected to/from a remote device
     *
     * @param status status
     */
    public void onConnectionStateChange(final boolean status) {
    }

    /**
     * Callback indicating when login in to a wristband
     *
     * @param state state
     */
    public void onLoginStateChange(final int state) {
    }

    /**
     * Callback indicating something error
     *
     * @param error error code
     */
    public void onError(final int error) {
    }

    /**
     * Callback indicating a sport data receive.
     */
    public void onStepDataReceiveIndication(ApplicationLayerStepPacket packet) {
    }

    /**
     * Callback indicating a sport data receive.
     */
    public void onSportDataReceiveIndication(ApplicationLayerSportPacket packet) {
    }

    /**
     * Callback indicating a sleep data receive.
     */
    public void onSleepDataReceiveIndication(ApplicationLayerSleepPacket packet) {
    }

    /**
     * Callback indicating a hrp data receive.
     */
    public void onHrpDataReceiveIndication(ApplicationLayerHrpPacket packet) {
    }

    /**
     * Callback indicating a device cancel hrp read.
     */
    public void onDeviceCancelSingleHrpRead() {
    }

    /**
     * Callback indicating a bp data receive.
     */
    public void onBpDataReceiveIndication(ApplicationLayerBpPacket packet) {
    }

    /**
     * Callback indicating a device cancel bp read.
     */
    public void onDeviceCancelSingleBpRead() {
    }

    /**
     * Callback indicating a hrp continue param response.
     */
    public void onHrpContinueParamRsp(boolean enable, int interval) {
    }

    /**
     * Callback indicating alarm list data receive.
     *
     * @param data the receive alarm data packet
     */
    public void onAlarmsDataReceive(ApplicationLayerAlarmsPacket data) {
    }

    /**
     * Callback indicating notify mode setting receive.
     *
     * @param reminderFunction notify switch setting
     */
    public void onNotifyModeSettingReceive(ReminderFunction reminderFunction) {
    }

    /**
     * Callback indicating notify mode setting receive.
     *
     * @param isOpen the current long sit mode
     */
    public void onLongSitSettingReceive(boolean isOpen) {
    }

    /**
     * Callback indicate remote Turn Over Wrist setting.
     *
     * @param mode the long sit mode setting
     */
    public void onTurnOverWristSettingReceive(final boolean mode) {
    }

    /**
     * Callback indicate take a photo.
     */
    public void onTakePhotoRsp() {
    }

    /**
     * Callback indicating a fac sensor data receive.
     *
     * @param data the receive sensor data
     */
    public void onFacSensorDataReceive(ApplicationLayerFacSensorPacket data) {
    }

    /**
     * Callback indicating version read.
     *
     * @param appVersion   app Version value
     * @param patchVersion patch Version value
     */
    public void onVersionRead(int appVersion, int patchVersion) {
    }

    /**
     * Callback indicating name receive.
     *
     * @param data the receive data
     */
    public void onNameRead(final String data) {
    }

    /**
     * Callback indicating battery read.
     *
     * @param value battery level value
     */
    public void onBatteryRead(int value) {
    }

    /**
     * Callback indicating battery change.
     *
     * @param value battery level value
     */
    public void onBatteryChange(int value) {
    }

    /**
     * Callback indicate log sync start.
     *
     * @param logLength the log total length
     */
    public void onLogCmdStart(final long logLength) {
    }

    /**
     * Callback indicate log sync end.
     */
    public void onLogCmdEnd() {
    }

    /**
     * Callback indicate log data.
     *
     * @param packet receive log data
     */
    public void onLogCmdRsp(final ApplicationLayerLogResponsePacket packet) {
    }

    /**
     * callback indicate end call
     */
    public void onEndCall() {
    }

    /**
     * Callback indicating device support sport function receiver
     *
     * @param sportFunction
     */
    public void onSportFunction(final SportFunction sportFunction) {
    }

    /**
     * Callback indicating device support reminder function receiver
     *
     * @param reminderFunction
     */
    public void onReminderFunction(final ReminderFunction reminderFunction) {
    }

    /**
     * Callback indicating device support function receiver
     *
     * @param deviceFunction
     */
    public void onDeviceFunction(final DeviceFunction deviceFunction) {

    }

    /**
     * callback indicating request hour model
     *
     * @param is24Model is 24 model
     */
    public void onHour(boolean is24Model) {

    }

    /**
     * callabck indicating request unit model
     *
     * @param isMetricSystem is metric model
     */
    public void onUnit(boolean isMetricSystem) {

    }

    /**
     * callback indicating request disturb setting
     *
     * @param isOpen
     */
    public void onDisturb(boolean isOpen) {

    }


    /**
     * callback indicating request screen duration setting
     *
     * @param duration
     */
    public void onScreenLightDuration(int duration) {
    }

    /**
     * callback indicating request device language
     *
     * @param language
     */
    public void onLanguage(DeviceLanguage language) {
    }

    public void onClassAddress(String address) {

    }

    public void onClassicStatus(int status) {

    }
}
