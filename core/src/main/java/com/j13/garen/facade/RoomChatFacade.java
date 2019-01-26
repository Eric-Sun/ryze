package com.j13.garen.facade;

import com.j13.garen.api.req.roomChat.*;
import com.j13.garen.daos.RoomChatDAO;
import com.j13.garen.utils.MD5Util;
import com.j13.garen.vos.RoomChatContentVO;
import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomChatFacade {

    @Autowired
    RoomChatDAO roomChatDAO;

    @Action(name = "roomChat.createRoom", desc = "创建聊天房间")
    public RoomChatCreateRoomResp createRoom(CommandContext ctxt, RoomChatCreateRoomReq req) {
        int userId = req.getUserId();
        String passwordAfterMD5 = MD5Util.getMD5String(req.getPassword());

        int crId = roomChatDAO.createRoom(userId, passwordAfterMD5);
        roomChatDAO.addMember(crId, userId);

        RoomChatCreateRoomResp resp = new RoomChatCreateRoomResp();
        resp.setCrId(crId);
        return resp;
    }

    @Action(name = "roomChat.joinRoom", desc = "加入聊天室")
    public RoomChatJoinRoomResp joinRoom(CommandContext ctxt, RoomChatJoinRoomReq req) {

        RoomChatJoinRoomResp resp = new RoomChatJoinRoomResp();
        int userId = req.getUserId();
        String passwordAfterMD5 = MD5Util.getMD5String(req.getPassword());

        int crId = roomChatDAO.getRoomByPassword(passwordAfterMD5);
        if (crId == 0) {
            // 密码错误
            resp.setCrId(-1);
            return resp;
        }
        resp.setCrId(crId);
        boolean in = roomChatDAO.checkMember(crId, userId);
        if (in) {
            // 已经在房间里面直接进入
            return resp;
        } else {
            roomChatDAO.addMember(crId, userId);
            return resp;
        }

    }


    @Action(name = "roomChat.initRoom", desc = "双方进入之后初始化聊天室的聊天内容等")
    public RoomChatInitRoomResp initRoom(CommandContext ctxt, RoomChatInitRoomReq req) {
        RoomChatInitRoomResp resp = new RoomChatInitRoomResp();
        int userId = req.getUserId();
        int crId = req.getCrId();

        // 检查这个userId是否在cr中
        boolean in = roomChatDAO.checkMember(crId, userId);
        if (in) {
            List<RoomChatContentVO> list = roomChatDAO.loadContent(crId, 10);
            for (RoomChatContentVO vo : list) {
                RoomChatContentResp innerResp = new RoomChatContentResp();
                BeanUtils.copyProperties(innerResp, vo);
                resp.getData().add(innerResp);
            }
        }

        return resp;
    }


    @Action(name = "roomChat.loadContent", desc = "在当前位置往上和往下查找记录")
    public RoomChatLoadContentResp loadContent(CommandContext ctxt, RoomChatLoadContentReq req) {
        RoomChatLoadContentResp resp = new RoomChatLoadContentResp();
        List<RoomChatContentVO> list = null;
        if (req.getType() == 1) {
            // 往上查找
            list = roomChatDAO.loadContentByIndexToUp(req.getCrId(), req.getIndex(), 10);
        } else {
            // 往下查找
            list = roomChatDAO.loadContentByIndexToDown(req.getCrId(), req.getIndex());
        }
        for (RoomChatContentVO vo : list) {
            RoomChatContentResp innerResp = new RoomChatContentResp();
            BeanUtils.copyProperties(innerResp, vo);
            resp.getData().add(innerResp);
        }
        return resp;
    }


    @Action(name = "roomChat.sendContent", desc = "发送消息")
    public RoomChatSendContentResp sendContent(CommandContext ctxt, RoomChatSendContentReq req) {
        RoomChatSendContentResp resp = new RoomChatSendContentResp();
        int id = roomChatDAO.sendContent(req.getUserId(), req.getCrId(), req.getContent());
        resp.setId(id);
        return resp;
    }


}
