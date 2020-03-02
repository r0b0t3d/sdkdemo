package com.wosmart.ukprotocollibary.applicationlayer;

import com.wosmart.ukprotocollibary.model.data.DeviceFunction;
import com.wosmart.ukprotocollibary.model.data.ReminderFunction;
import com.wosmart.ukprotocollibary.model.data.SportFunction;
import com.wosmart.ukprotocollibary.model.enums.DeviceLanguage;

public class ApplicationLayerCallback {
    /**
     * Callback indicate when gatt connected/disconnected to/from a remote device
     *
     * @param status   status
     * @param newState the new connection State
     */
    public void onConnectionStateChange(final boolean status, final boolean newState) {
    }

    /**
     * Callback indicate remote enter OTA mode ok or not.
     *
     * @param status
     * @param errorcode the error code
     */
    public void onUpdateCmdRequestEnterOtaMode(final byte status, final byte errorcode) {
    }

    /**
     * Callback indicate remote alarm list.
     *
     * @param alarms
     */
    public void onSettingCmdRequestAlarmList(final ApplicationLayerAlarmsPacket alarms) {
    }

    /**
     * Callback indicate remote other notify switch setting.
     *
     * @param reminderFunction the other notify switch setting
     */
    public void onSettingCmdRequestNotifySwitch(ReminderFunction reminderFunction) {
    }

    /**
     * Callback indicate remote long sit mode setting.
     *
     * @param isOpen the long sit status
     */
    public void onSettingCmdRequestLongSit(boolean isOpen) {
    }

    /**
     * Callback indicate remote Turn Over Wristsetting.
     *
     * @param mode the long sit mode setting
     */
    public void onTurnOverWristSettingReceive(final boolean mode) {
    }

    /**
     * Callback indicate remote bond response.
     *
     * @param status
     */
    public void onBondCmdRequestBond(final byte status) {
    }

    /**
     * Callback indicate remote login response.
     *
     * @param status
     */
    public void onBondCmdRequestLogin(final byte status) {
    }

    /**
     * Callback indicate end call.
     */
    public void onEndCallReceived() {
    }

    /**
     * Callback indicate remote send sport data.
     *
     * @param step
     */
    public void onSportDataCmdStepData(final ApplicationLayerStepPacket step) {
    }


    /**
     * Callback indicate remote send sleep data.
     *
     * @param sleep sleep data packet
     */
    public void onSportDataCmdSleepData(final ApplicationLayerSleepPacket sleep) {
    }

    /**
     * Callback indicate remote send sport data.
     *
     * @param sport
     */
    public void onSportDataCmdSportData(final ApplicationLayerSportPacket sport) {
    }

    /**
     * Callback indicate remote have more data to send.
     */
    public void onSportDataCmdMoreData() {
    }

    /**
     * Callback indicate remote send sleep set data.
     *
     * @param sleep sleep data packet
     */
    public void onSportDataCmdSleepSetData(final ApplicationLayerSleepPacket sleep) {
    }

    /**
     * Callback indicate history sync begin.
     */
    public void onSportDataCmdHistorySyncBegin() {
    }

    /**
     * Callback indicate history sync end.
     */
    public void onSportDataCmdHistorySyncEnd(ApplicationLayerTodaySumSportPacket packet) {
    }

    /**
     * Callback indicate hrp data received.
     */
    public void onSportDataCmdHrpData(ApplicationLayerHrpPacket packet) {
    }

    /**
     * Callback indicate hrp data received.
     */
    public void onSportDataCmdBpData(ApplicationLayerBpPacket packet) {
    }

    /**
     * Callback indicate device cancel single hrp read.
     */
    public void onSportDataCmdDeviceCancelSingleHrpRead() {
    }

    /**
     * Callback indicate device cancel single bp read.
     */
    public void onSportDataCmdDeviceCancelSingleBpRead() {
    }

    /**
     * Callback indicate device current continue param.
     */
    public void onSportDataCmdHrpContinueParamRsp(boolean enable, int interval) {
    }

    /**
     * Callback indicate take a photo.
     */
    public void onTakePhotoRsp() {
    }

    /**
     * Callback indicate history sync end.
     *
     * @param sensor sensor data packet
     */
    public void onFACCmdSensorData(final ApplicationLayerFacSensorPacket sensor) {
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
     * Callback indicate a command send.
     *
     * @param status  the send result.
     * @param command the send data's command id
     * @param key     the send data's key id
     */
    public void onCommandSend(final boolean status, byte command, byte key) {
    }

    /**
     * Callback indicating name receive.
     *
     * @param data the receive data
     */
    public void onNameReceive(final String data) {
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
