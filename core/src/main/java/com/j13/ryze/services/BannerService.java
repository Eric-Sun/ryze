package com.j13.ryze.services;

import com.j13.ryze.daos.BannerBannerPlanDAO;
import com.j13.ryze.daos.BannerDAO;
import com.j13.ryze.daos.BannerPlanDAO;
import com.j13.ryze.vos.BannerVO;
import com.j13.ryze.vos.ImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {

    @Autowired
    BannerDAO bannerDAO;
    BannerPlanDAO bannerPlanDAO;
    BannerBannerPlanDAO bannerBannerPlanDAO;
    @Autowired
    ImgService imgService;


    public void addBanner(String name, int urlImgId) {
        bannerDAO.add(name, urlImgId);
    }

    public void deleteBanner(int bannerId) {
        bannerDAO.delete(bannerId);
    }

    public void updateBanner(int bannerId, String name, int urlImgId) {
        bannerDAO.update(bannerId, name, urlImgId);
    }

    public List<BannerVO> listBanner(int size, int pageNum) {
        List<BannerVO> voList =  bannerDAO.list(size, pageNum);
        for(BannerVO vo : voList){
            ImgVO imgVO = new ImgVO();
            imgVO.setId(vo.getUrlImgId());

            String url = imgService.getFileUrl(vo.getUrlImgId());
            imgVO.setUrl(url);
            vo.setImg(imgVO);
        }
        return voList;
    }
}
