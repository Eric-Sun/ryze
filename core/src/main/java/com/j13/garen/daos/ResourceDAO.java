package com.j13.garen.daos;

import com.j13.garen.core.Constants;
import com.j13.garen.vos.AuthorityVO;
import com.j13.garen.vos.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 14-10-9
 * Time: 下午3:33
 * To change this template use File | Settings | File Templates.
 */
@Repository("resourceDAO")
public class ResourceDAO {

    @Autowired
    JdbcTemplate j;

    public List<AuthorityVO> getAuthorityListByResourceName(String name) {
        String sql = "select a.id,a.name from authority a, authority_resource ar, resource r " +
                "where a.id=ar.authority_id and r.id = ar.resource_id and r.name=? and a.deleted=? and ar.deleted=? and r.deleted=?";
        return j.query(sql, new Object[]{name, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED, Constants.DB.NOT_DELETED}, new BeanPropertyRowMapper<AuthorityVO>(AuthorityVO.class));
    }

    public List<ResourceVO> getResourceList() {
        String sql = "select * from resource where deleted=?";
        return j.query(sql, new Object[]{Constants.DB.NOT_DELETED}, new BeanPropertyRowMapper<ResourceVO>(ResourceVO.class));
    }


    public int add(final String name) {
        KeyHolder holder = new GeneratedKeyHolder();
        final String sql = "insert into resource (name,createtime,updatetime) values (?,now(),now())";
        j.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, name);
                return pstmt;
            }
        }, holder);
        return holder.getKey().intValue();
    }


    public ResourceVO get(int id) {
        String sql = "select * from resource where id=? and deleted=?";
        return j.queryForObject(sql, new Object[]{id, Constants.DB.NOT_DELETED}, new BeanPropertyRowMapper<ResourceVO>(ResourceVO.class));
    }

    public void update(int id, String name) {
        String sql = "update resource set name=?,updatetime=now() where id=? and deleted=?";
        j.update(sql, new Object[]{name, id, Constants.DB.NOT_DELETED});
    }

    public void delete(int id) {
        String sql = "update resource set deleted=?,updatetime=now() where  id=?";
        j.update(sql, new Object[]{Constants.DB.DELETED, id});
    }

    public List<Integer> getResourceListByAuthorityId(int id) {
        String sql = "select resource_id from authority_resource where authority_id=? and deleted=?";
        return j.queryForList(sql, new Object[]{id, Constants.DB.NOT_DELETED}, Integer.class);
    }
}
