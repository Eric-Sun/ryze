package com.j13.garen.api.req.roomChat;

import com.google.common.collect.Lists;
import com.j13.poppy.anno.Parameter;

import java.util.List;

public class RoomChatLoadContentResp {
    @Parameter(desc = "")
    private List<RoomChatContentResp> data = Lists.newLinkedList();

    public List<RoomChatContentResp> getData() {
        return data;
    }

    public void setData(List<RoomChatContentResp> data) {
        this.data = data;
    }
}
