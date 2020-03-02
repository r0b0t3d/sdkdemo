package com.wosmart.ukprotocollibary.model.data;

import com.wosmart.ukprotocollibary.model.enums.DeviceFunctionStatus;

/**
 * device support reminder function
 */
public class ReminderFunction {
    private DeviceFunctionStatus Call;
    private DeviceFunctionStatus Sms;
    private DeviceFunctionStatus QQ;
    private DeviceFunctionStatus WeChat;
    private DeviceFunctionStatus Line;
    private DeviceFunctionStatus Twitter;
    private DeviceFunctionStatus Facebook;
    private DeviceFunctionStatus Messenger;
    private DeviceFunctionStatus WhatsApp;
    private DeviceFunctionStatus LinkedIn;
    private DeviceFunctionStatus Instagram;
    private DeviceFunctionStatus Skype;
    private DeviceFunctionStatus Viber;
    private DeviceFunctionStatus KakaoTalk;
    private DeviceFunctionStatus VkonTakte;

    private final int REMINDER_HEAD_LENGTH = 4;

    public ReminderFunction() {
    }

    public ReminderFunction(DeviceFunctionStatus call, DeviceFunctionStatus sms, DeviceFunctionStatus QQ, DeviceFunctionStatus weChat) {
        this.Call = call;
        this.Sms = sms;
        this.QQ = QQ;
        this.WeChat = weChat;

        this.Line = DeviceFunctionStatus.UN_SUPPORT;
        this.Twitter = DeviceFunctionStatus.UN_SUPPORT;
        this.Facebook = DeviceFunctionStatus.UN_SUPPORT;
        this.Messenger = DeviceFunctionStatus.UN_SUPPORT;
        this.WhatsApp = DeviceFunctionStatus.UN_SUPPORT;
        this.LinkedIn = DeviceFunctionStatus.UN_SUPPORT;
        this.Instagram = DeviceFunctionStatus.UN_SUPPORT;
        this.Skype = DeviceFunctionStatus.UN_SUPPORT;
        this.Viber = DeviceFunctionStatus.UN_SUPPORT;
        this.KakaoTalk = DeviceFunctionStatus.UN_SUPPORT;
        this.VkonTakte = DeviceFunctionStatus.UN_SUPPORT;
    }

    public ReminderFunction(DeviceFunctionStatus call, DeviceFunctionStatus sms, DeviceFunctionStatus QQ, DeviceFunctionStatus weChat, DeviceFunctionStatus line, DeviceFunctionStatus twitter, DeviceFunctionStatus facebook, DeviceFunctionStatus messenger, DeviceFunctionStatus whatsApp, DeviceFunctionStatus linkedIn, DeviceFunctionStatus instagram, DeviceFunctionStatus skype, DeviceFunctionStatus viber, DeviceFunctionStatus kakaoTalk, DeviceFunctionStatus vkonTakte) {
        this.Call = call;
        this.Sms = sms;
        this.QQ = QQ;
        this.WeChat = weChat;
        this.Line = line;
        this.Twitter = twitter;
        this.Facebook = facebook;
        this.Messenger = messenger;
        this.WhatsApp = whatsApp;
        this.LinkedIn = linkedIn;
        this.Instagram = instagram;
        this.Skype = skype;
        this.Viber = viber;
        this.KakaoTalk = kakaoTalk;
        this.VkonTakte = vkonTakte;
    }

    public DeviceFunctionStatus getCall() {
        return Call;
    }

    public void setCall(DeviceFunctionStatus call) {
        Call = call;
    }

    public DeviceFunctionStatus getSms() {
        return Sms;
    }

    public void setSms(DeviceFunctionStatus sms) {
        Sms = sms;
    }

    public DeviceFunctionStatus getQQ() {
        return QQ;
    }

    public void setQQ(DeviceFunctionStatus QQ) {
        this.QQ = QQ;
    }

    public DeviceFunctionStatus getWeChat() {
        return WeChat;
    }

    public void setWeChat(DeviceFunctionStatus weChat) {
        WeChat = weChat;
    }

    public DeviceFunctionStatus getLine() {
        return Line;
    }

    public void setLine(DeviceFunctionStatus line) {
        this.Line = line;
    }

    public DeviceFunctionStatus getTwitter() {
        return Twitter;
    }

    public void setTwitter(DeviceFunctionStatus twitter) {
        Twitter = twitter;
    }

    public DeviceFunctionStatus getFacebook() {
        return Facebook;
    }

    public void setFacebook(DeviceFunctionStatus facebook) {
        Facebook = facebook;
    }

    public DeviceFunctionStatus getMessenger() {
        return Messenger;
    }

    public void setMessenger(DeviceFunctionStatus messenger) {
        Messenger = messenger;
    }

    public DeviceFunctionStatus getWhatsApp() {
        return WhatsApp;
    }

    public void setWhatsApp(DeviceFunctionStatus whatsApp) {
        WhatsApp = whatsApp;
    }

    public DeviceFunctionStatus getLinkedIn() {
        return LinkedIn;
    }

    public void setLinkedIn(DeviceFunctionStatus linkedIn) {
        LinkedIn = linkedIn;
    }

    public DeviceFunctionStatus getInstagram() {
        return Instagram;
    }

    public void setInstagram(DeviceFunctionStatus instagram) {
        Instagram = instagram;
    }

    public DeviceFunctionStatus getSkype() {
        return Skype;
    }

    public void setSkype(DeviceFunctionStatus skype) {
        Skype = skype;
    }

    public DeviceFunctionStatus getViber() {
        return Viber;
    }

    public void setViber(DeviceFunctionStatus viber) {
        Viber = viber;
    }

    public DeviceFunctionStatus getKakaoTalk() {
        return KakaoTalk;
    }

    public void setKakaoTalk(DeviceFunctionStatus kakaoTalk) {
        KakaoTalk = kakaoTalk;
    }

    public DeviceFunctionStatus getVkonTakte() {
        return VkonTakte;
    }

    public void setVkonTakte(DeviceFunctionStatus vkonTakte) {
        VkonTakte = vkonTakte;
    }

    public boolean parseData(byte[] data) {
        int newReminder = data[2] >> 4 & 0x01;
        Call = DeviceFunctionStatus.SUPPORT;
        Sms = DeviceFunctionStatus.SUPPORT;
        QQ = DeviceFunctionStatus.SUPPORT;
        WeChat = DeviceFunctionStatus.SUPPORT;
        if (newReminder == 1) {
            Line = DeviceFunctionStatus.SUPPORT;
            Twitter = DeviceFunctionStatus.SUPPORT;
            Facebook = DeviceFunctionStatus.SUPPORT;
            Messenger = DeviceFunctionStatus.SUPPORT;
            WhatsApp = DeviceFunctionStatus.SUPPORT;
            LinkedIn = DeviceFunctionStatus.SUPPORT;
            Instagram = DeviceFunctionStatus.SUPPORT;
            Skype = DeviceFunctionStatus.SUPPORT;
            Viber = DeviceFunctionStatus.SUPPORT;
            KakaoTalk = DeviceFunctionStatus.SUPPORT;
            VkonTakte = DeviceFunctionStatus.SUPPORT;
        } else {
            Line = DeviceFunctionStatus.UN_SUPPORT;
            Twitter = DeviceFunctionStatus.UN_SUPPORT;
            Facebook = DeviceFunctionStatus.UN_SUPPORT;
            Messenger = DeviceFunctionStatus.UN_SUPPORT;
            WhatsApp = DeviceFunctionStatus.UN_SUPPORT;
            LinkedIn = DeviceFunctionStatus.UN_SUPPORT;
            Instagram = DeviceFunctionStatus.UN_SUPPORT;
            Skype = DeviceFunctionStatus.UN_SUPPORT;
            Viber = DeviceFunctionStatus.UN_SUPPORT;
            KakaoTalk = DeviceFunctionStatus.UN_SUPPORT;
            VkonTakte = DeviceFunctionStatus.UN_SUPPORT;
        }
        return true;
    }

    public boolean parseData2(byte[] data) {
        if (data.length >= REMINDER_HEAD_LENGTH) {
            int call = data[3] & 0x01;
            int qq = data[3] >> 1 & 0x01;
            int wechat = data[3] >> 2 & 0x01;
            int sms = data[3] >> 3 & 0x01;
            int line = data[3] >> 4 & 0x01;
            int twitter = data[3] >> 5 & 0x01;
            int facebook = data[3] >> 6 & 0x01;
            int messenger = data[3] >> 7 & 0x01;
            int whatsapp = data[2] & 0x01;
            int linkedin = data[2] >> 1 & 0x01;
            int instagram = data[2] >> 2 & 0x01;
            int skype = data[2] >> 3 & 0x01;
            int viber = data[2] >> 4 & 0x01;
            int kakaotalk = data[2] >> 5 & 0x01;
            int vkontakte = data[2] >> 6 & 0x01;
            if (call == 1) {
                Call = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                Call = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (qq == 1) {
                QQ = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                QQ = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (wechat == 1) {
                WeChat = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                WeChat = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (sms == 1) {
                Sms = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                Sms = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (line == 1) {
                Line = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                Line = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (twitter == 1) {
                Twitter = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                Twitter = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (facebook == 1) {
                Facebook = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                Facebook = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (messenger == 1) {
                Messenger = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                Messenger = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (whatsapp == 1) {
                WhatsApp = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                WhatsApp = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (linkedin == 1) {
                LinkedIn = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                LinkedIn = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (instagram == 1) {
                Instagram = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                Instagram = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (skype == 1) {
                Skype = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                Skype = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (viber == 1) {
                Viber = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                Viber = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (kakaotalk == 1) {
                KakaoTalk = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                KakaoTalk = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
            if (vkontakte == 1) {
                VkonTakte = DeviceFunctionStatus.SUPPORT_OPEN;
            } else {
                VkonTakte = DeviceFunctionStatus.SUPPORT_CLOSE;
            }
        }
        return true;
    }


    @Override
    public String toString() {
        return "ReminderFunction{" +
                "Call=" + Call +
                ", Sms=" + Sms +
                ", QQ=" + QQ +
                ", WeChat=" + WeChat +
                ", Line=" + Line +
                ", Twitter=" + Twitter +
                ", Facebook=" + Facebook +
                ", Messenger=" + Messenger +
                ", WhatsApp=" + WhatsApp +
                ", LinkedIn=" + LinkedIn +
                ", Instagram=" + Instagram +
                ", Skype=" + Skype +
                ", Viber=" + Viber +
                ", KakaoTalk=" + KakaoTalk +
                ", VkonTakte=" + VkonTakte +
                '}';
    }
}
