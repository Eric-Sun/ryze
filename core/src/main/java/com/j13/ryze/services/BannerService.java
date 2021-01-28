package com.j13.ryze.services;

import com.j13.ryze.daos.BannerBannerPlanDAO;
import com.j13.ryze.daos.BannerDAO;
import com.j13.ryze.daos.BannerPlanDAO;
import com.j13.ryze.vos.BannerBannerPlanVO;
import com.j13.ryze.vos.BannerPlanVO;
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
        List<BannerVO> voList = bannerDAO.list(size, pageNum);
        for (BannerVO vo : voList) {
            ImgVO imgVO = new ImgVO();
            imgVO.setId(vo.getUrlImgId());

            String url = imgService.getFileUrl(vo.getUrlImgId());
            imgVO.setUrl(url);
            vo.setImg(imgVO);
        }
        return voList;
    }

    public void addBannerPlan(String name, int type) {
        bannerPlanDAO.add(name, type);
    }

    public void deleteBannerPlan(int bannerPlanId) {
        bannerPlanDAO.delete(bannerPlanId);
    }

    public void updateBannerPlan(int bannerPlanId, String name) {
        bannerPlanDAO.update(bannerPlanId, name);
    }

    /**
     * 获取单个bannner的信息，并且填充img信息
     * @param bannerId
     * @return
     */
    public BannerVO getBanner(int bannerId) {
        BannerVO bannerVO = bannerDAO.get(bannerId);
        String url = imgService.getFileUrl(bannerVO.getUrlImgId());
        ImgVO imgVO = new ImgVO();
        imgVO.setUrl(url);
        bannerVO.setImg(imgVO);
        return bannerVO;
    }

    public List<BannerPlanVO> bannerPlanList(int size, int pageNum) {
        List<BannerPlanVO> bannerPlanList = bannerPlanDAO.list(size, pageNum);
        for (BannerPlanVO planVO : bannerPlanList) {
            List<BannerBannerPlanVO> bbpVOList = bannerPlanDAO.listBanners(planVO.getId(), size, pageNum);
            for (BannerBannerPlanVO bbpVO : bbpVOList) {
                BannerVO bannerVO = getBanner(bbpVO.getBannerId());
                planVO.getBannerVOList().add(bannerVO);
                planVO.getBannerIdList().add(bannerVO.getId());
            }
            bannerPlanList.add(planVO);
        }
        return bannerPlanList;
    }

    public void addBannerToBannerPlan(int bannerPlanId, int bannerId) {
        bannerBannerPlanDAO.add(bannerId, bannerPlanId);
    }

    public void deleteBannerFromBannerPlan(int bannerBannerPlanId){
        bannerBannerPlanDAO.delete(bannerBannerPlanId);
    }

}
