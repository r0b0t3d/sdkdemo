package com.wosmart.ukprotocollibary.model.data;

import com.wosmart.ukprotocollibary.model.enums.DeviceFunctionStatus;

/**
 * device support function
 */
public class SportFunction {
    private DeviceFunctionStatus Run;
    private DeviceFunctionStatus Climb;
    private DeviceFunctionStatus Football;
    private DeviceFunctionStatus Cycle;
    private DeviceFunctionStatus Rope;
    private DeviceFunctionStatus Swim;
    private DeviceFunctionStatus Basketball;
    private DeviceFunctionStatus Badminton;
    private DeviceFunctionStatus Volleyball;
    private DeviceFunctionStatus Table_Tennis;
    private DeviceFunctionStatus Tennis;
    private DeviceFunctionStatus Bowling;
    private DeviceFunctionStatus Skiing;
    private DeviceFunctionStatus Skating;

    public SportFunction() {
    }

    public SportFunction(DeviceFunctionStatus run, DeviceFunctionStatus climb, DeviceFunctionStatus football, DeviceFunctionStatus cycle, DeviceFunctionStatus rope, DeviceFunctionStatus swim, DeviceFunctionStatus basketball, DeviceFunctionStatus badminton, DeviceFunctionStatus volleyball, DeviceFunctionStatus table_Tennis, DeviceFunctionStatus tennis, DeviceFunctionStatus bowling, DeviceFunctionStatus skiing, DeviceFunctionStatus skating) {
        this.Run = run;
        this.Climb = climb;
        this.Football = football;
        this.Cycle = cycle;
        this.Rope = rope;
        this.Swim = swim;
        this.Basketball = basketball;
        this.Badminton = badminton;
        this.Volleyball = volleyball;
        this.Table_Tennis = table_Tennis;
        this.Tennis = tennis;
        this.Bowling = bowling;
        this.Skiing = skiing;
        this.Skating = skating;
    }

    public DeviceFunctionStatus getRun() {
        return Run;
    }

    public void setRun(DeviceFunctionStatus run) {
        Run = run;
    }

    public DeviceFunctionStatus getClimb() {
        return Climb;
    }

    public void setClimb(DeviceFunctionStatus climb) {
        Climb = climb;
    }

    public DeviceFunctionStatus getFootball() {
        return Football;
    }

    public void setFootball(DeviceFunctionStatus football) {
        Football = football;
    }

    public DeviceFunctionStatus getCycle() {
        return Cycle;
    }

    public void setCycle(DeviceFunctionStatus cycle) {
        Cycle = cycle;
    }

    public DeviceFunctionStatus getRope() {
        return Rope;
    }

    public void setRope(DeviceFunctionStatus rope) {
        Rope = rope;
    }

    public DeviceFunctionStatus getSwim() {
        return Swim;
    }

    public void setSwim(DeviceFunctionStatus swim) {
        Swim = swim;
    }

    public DeviceFunctionStatus getBasketball() {
        return Basketball;
    }

    public void setBasketball(DeviceFunctionStatus basketball) {
        Basketball = basketball;
    }

    public DeviceFunctionStatus getBadminton() {
        return Badminton;
    }

    public void setBadminton(DeviceFunctionStatus badminton) {
        Badminton = badminton;
    }

    public DeviceFunctionStatus getVolleyball() {
        return Volleyball;
    }

    public void setVolleyball(DeviceFunctionStatus volleyball) {
        Volleyball = volleyball;
    }

    public DeviceFunctionStatus getTable_Tennis() {
        return Table_Tennis;
    }

    public void setTable_Tennis(DeviceFunctionStatus table_Tennis) {
        Table_Tennis = table_Tennis;
    }

    public DeviceFunctionStatus getTennis() {
        return Tennis;
    }

    public void setTennis(DeviceFunctionStatus tennis) {
        Tennis = tennis;
    }

    public DeviceFunctionStatus getBowling() {
        return Bowling;
    }

    public void setBowling(DeviceFunctionStatus bowling) {
        Bowling = bowling;
    }

    public DeviceFunctionStatus getSkiing() {
        return Skiing;
    }

    public void setSkiing(DeviceFunctionStatus skiing) {
        Skiing = skiing;
    }

    public DeviceFunctionStatus getSkating() {
        return Skating;
    }

    public void setSkating(DeviceFunctionStatus skating) {
        Skating = skating;
    }

    public boolean parseData(byte[] data) {
        int run = data[2] >> 5 & 0x01;
        int climb = data[2] >> 6 & 0x01;
        int footBall = data[2] >> 7 & 0x01;
        int cycle = data[1] >> 0 & 0x01;
        int rope = data[1] >> 1 & 0x01;
        int swim = data[1] >> 2 & 0x01;
        int basketBall = data[1] >> 3 & 0x01;
        int badminton = data[1] >> 4 & 0x01;
        int volleyBall = data[1] >> 5 & 0x01;
        int tableTennis = data[1] >> 6 & 0x01;
        int bowling = data[1] >> 7 & 0x01;
        int tennis = data[0] >> 0 & 0x01;
        int skiing = data[0] >> 1 & 0x01;
        int skating = data[0] >> 2 & 0x01;
        if (run == 1) {
            this.Run = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Run = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (climb == 1) {
            this.Climb = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Climb = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (footBall == 1) {
            this.Football = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Football = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (cycle == 1) {
            this.Cycle = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Cycle = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (rope == 1) {
            this.Rope = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Rope = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (swim == 1) {
            this.Swim = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Swim = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (basketBall == 1) {
            this.Basketball = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Basketball = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (badminton == 1) {
            this.Badminton = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Badminton = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (volleyBall == 1) {
            this.Volleyball = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Volleyball = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (tableTennis == 1) {
            this.Table_Tennis = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Table_Tennis = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (bowling == 1) {
            this.Bowling = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Bowling = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (tennis == 1) {
            this.Tennis = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Tennis = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (skiing == 1) {
            this.Skiing = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Skiing = DeviceFunctionStatus.UN_SUPPORT;
        }
        if (skating == 1) {
            this.Skating = DeviceFunctionStatus.SUPPORT;
        } else {
            this.Skating = DeviceFunctionStatus.UN_SUPPORT;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SportFunction{" +
                "Run=" + Run +
                ", Climb=" + Climb +
                ", Football=" + Football +
                ", Cycle=" + Cycle +
                ", Rope=" + Rope +
                ", Swim=" + Swim +
                ", Basketball=" + Basketball +
                ", Badminton=" + Badminton +
                ", Volleyball=" + Volleyball +
                ", Table_Tennis=" + Table_Tennis +
                ", Tennis=" + Tennis +
                ", Bowling=" + Bowling +
                ", Skiing=" + Skiing +
                ", Skating=" + Skating +
                '}';
    }
}
