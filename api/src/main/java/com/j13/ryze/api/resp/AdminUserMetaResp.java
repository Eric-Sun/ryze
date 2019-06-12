package com.j13.ryze.api.resp;

import com.j13.poppy.anno.Parameter;

public class AdminUserMetaResp {
    @Parameter(desc = "")
    private int allUserCount;
    @Parameter(desc = "")
    private int machineUserCount;

    public int getAllUserCount() {
        return allUserCount;
    }

    public void setAllUserCount(int allUserCount) {
        this.allUserCount = allUserCount;
    }

    public int getMachineUserCount() {
        return machineUserCount;
    }

    public void setMachineUserCount(int machineUserCount) {
        this.machineUserCount = machineUserCount;
    }
}
