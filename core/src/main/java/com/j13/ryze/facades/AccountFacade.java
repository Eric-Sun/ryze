package com.j13.ryze.facades;

import com.j13.poppy.anno.Action;
import com.j13.poppy.core.CommandContext;
import com.j13.poppy.core.CommonResultResp;
import com.j13.poppy.exceptions.CommonException;
import com.j13.poppy.util.BeanUtils;
import com.j13.ryze.api.req.*;
import com.j13.ryze.api.resp.*;
import com.j13.ryze.core.ErrorCode;
import com.j13.ryze.daos.AccountDAO;
import com.j13.ryze.daos.ResourceDAO;
import com.j13.ryze.utils.MD5Util;
import com.j13.ryze.vos.AccountVO;
import com.j13.ryze.vos.AuthorityVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * facade for the account service.
 * some of interfaces only called by admin server.
 * TODO: will add the Admin annotation to handle it
 */
@Component
public class AccountFacade {
    private static Logger LOG = LoggerFactory.getLogger(AccountFacade.class);

    @Autowired
    AccountDAO accountDAO;
    @Autowired
    ResourceDAO resourceDAO;

    @Action(name = "account.getAuthorityByName",
            desc = "Get all resource id configured by target accountName. Called by admin")
    public AccountGetAuthorityByNameResp getAuthorityByName(CommandContext ctxt, AccountGetAuthorityByNameReq req) {
        AccountGetAuthorityByNameResp resp = new AccountGetAuthorityByNameResp();
        String accountName = req.getName();
        List<AuthorityVO> list = accountDAO.getAuthorityListByAccountName(accountName);
//        List<Integer> resourceIdList = resourceDAO.getResourceListByAuthorityId(authorityId);
        for (AuthorityVO vo : list) {
            AuthorityGetResp r = new AuthorityGetResp();
            BeanUtils.copyProperties(r, vo);
            resp.getData().add(r);
        }
        return resp;
    }


    @Action(name = "account.getAccountIdByName", desc = "Get Account Id by name. called by admin")
    public AccountGetAccountIdByNameResp getAccountIdByName(CommandContext ctxt, AccountGetAccountIdByNameReq req) {
        AccountGetAccountIdByNameResp resp = new AccountGetAccountIdByNameResp();
        String name = req.getName();
        int accountId = accountDAO.getAccountIdByAccountName(name);
        resp.setAccountId(accountId);
        return resp;
    }

    @Action(name = "account.checkExisted", desc = "check account existed?")
    public AccountCheckExistedResp checkExisted(CommandContext ctxt, AccountCheckExistedReq req) {
        AccountCheckExistedResp resp = new AccountCheckExistedResp();
        String name = req.getName();
        String password = req.getPassword();
        boolean b = accountDAO.checkExisted(name, password);
        resp.setExisted(b);
        return resp;
    }


    @Action(name = "account.list", desc = "list")
    public AccountListResp list(CommandContext ctxt, AccountListReq req) {
        AccountListResp resp = new AccountListResp();
        List<AccountVO> list = accountDAO.list();
        for (AccountVO vo : list) {
            AccountGetResp r = new AccountGetResp();
            BeanUtils.copyProperties(r, vo);
            resp.getList().add(r);
        }
        return resp;
    }

    @Action(name = "account.get", desc = "get")
    public AccountGetResp get(CommandContext ctxt, AccountGetReq req) {
        AccountGetResp resp = new AccountGetResp();
        int id = req.getId();
        AccountVO vo = accountDAO.get(id);
        BeanUtils.copyProperties(resp, vo);
        return resp;
    }


    @Action(name = "account.delete", desc = "delete")
    public CommonResultResp delete(CommandContext ctxt, AccountDeleteReq req) {
        CommonResultResp resp = new CommonResultResp();
        accountDAO.delete(req.getId());
        LOG.info("delete account info suc. id={}", req.getId());
        return resp;
    }

    @Action(name = "account.update", desc = "update")
    public CommonResultResp update(CommandContext ctxt, AccountUpdateReq req) {
        CommonResultResp resp = new CommonResultResp();
        accountDAO.update(req.getId(), req.getName(), req.getPasswordAfterMD5(), req.getAuthorityId());
        LOG.info("update account info suc. id={}", req.getId());
        return resp;
    }

    @Action(name = "account.add", desc = "add")
    public AccountAddResp add(CommandContext ctxt, AccountAddReq req) {
        AccountAddResp resp = new AccountAddResp();
        // check existed or not
        if (accountDAO.checkExisted(req.getName())) {
            LOG.info("account name existed. name={}", req.getName());
            throw new CommonException(ErrorCode.Account.NAME_EXISTED);
        }

        int id = accountDAO.add(req.getName(), req.getPasswordAfterMD5(), req.getAuthorityId());
        LOG.info("add account info successfully. id={}", id);
        resp.setId(id);
        return resp;
    }

    @Action(name="account.login")
    public AccountLoginResp add(CommandContext ctxt, AccountLoginReq req) {
        AccountLoginResp resp = new AccountLoginResp();

        // 如果id为0，则为登陆失败
        int id = accountDAO.login(req.getUserName(), MD5Util.getMD5String(req.getUserPassword()));
        if (id == 0)
            throw new CommonException(ErrorCode.User.PASSWORD_NOT_RIGHT);

        resp.setId(id);
        return resp;
    }

}
