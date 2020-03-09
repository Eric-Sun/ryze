package com.j13.ryze.fetcher;

import com.google.common.base.Splitter;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.FPostDAO;
import com.j13.ryze.daos.FReplyDAO;
import com.j13.ryze.daos.UserDAO;
import com.j13.ryze.services.ImgService;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.ReplyService;
import com.j13.ryze.utils.InternetUtil;
import com.j13.ryze.vos.ImgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Random;
import java.util.regex.Pattern;


@Service
public class TianyaFetcher {

    @Autowired
    FPostDAO fPostDAO;
    @Autowired
    FReplyDAO fReplyDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    ImgService imgService;
    private Random random = new Random();

    private Pattern p = Pattern.compile("\\s*|\t|\r");


    public void doFetch() {
        try {
            String rawString = InternetUtil.getContentForFetch("http://bbs.tianya.cn/list-feeling-1.shtml");

            Iterator<String> i1 = Splitter.on("<div id=\"shang_tab_list\"></div>").split(rawString).iterator();

            i1.next();
            String rawString1 = i1.next();

            Iterator<String> i2 = Splitter.on("<a href=\"/post-feeling-").split(rawString1).iterator();
            i2.next();
            int count = 0;
            while (i2.hasNext()) {
                String rawString3 = i2.next();
                int postIdIndex = rawString3.indexOf("-");
                String postIdStr = rawString3.substring(0, postIdIndex);

                // a
                int postTitleAStartIndex = rawString3.indexOf("target=\"_blank\">") + "target=\"_blank\">".length();
                int postTitleAEndIndex = rawString3.indexOf("</a>");

                String postTitleStr = rawString3.substring(postTitleAStartIndex, postTitleAEndIndex);
                if (postTitleStr.indexOf("span") > 0) {
                    int postTitleSpanIndex = postTitleStr.indexOf("<span");
                    postTitleStr = postTitleStr.substring(0, postTitleSpanIndex);
                }
                if (postTitleStr.indexOf("天涯") > 0)
                    continue;
                postTitleStr = postTitleStr.replaceAll("\\s*|\t|\n", "");
                Logger.FETCHER.info(postTitleStr);

                parsePostPage(new Integer(postIdStr), postTitleStr);
//                parsePostPage(4429545, "");
                count++;
                if(count==2)
                    break;
            }
            Logger.FETCHER.info("count={}", count);
        } catch (Exception e) {
            Logger.FETCHER.error("", e);
        }

    }


    public void parsePostPage(int postId, String title) {

        int pageNum = 1;

        while (true) {
            String rawString = InternetUtil.getContentForFetch("http://bbs.tianya.cn/post-feeling-" + postId + "-" + pageNum + ".shtml");


            if (rawString == null) {
                Logger.FETCHER.info("页面到底了 PageNum={}", pageNum);
                return;
            } else {
                Logger.FETCHER.info("开始抓取页面 pageNum={}", pageNum);
            }

            if (title == null) {
                // capture title
                int titleIndexStart = rawString.indexOf("<meta itemprop=\"name\" content=\"") + "<meta itemprop=\"name\" content=\"".length();
                int titleIndexEnd = rawString.indexOf("\">", titleIndexStart);
                title = rawString.substring(titleIndexStart, titleIndexEnd);
                Logger.FETCHER.info("title : {}", title);
            }

            // capture author
            int authorIndexStart = rawString.indexOf("<meta name=\"author\" content=\"") + "<meta name=\"author\" content=\"".length();
            int authorIndexEnd = rawString.indexOf("\">", authorIndexStart);
            String author = rawString.substring(authorIndexStart, authorIndexEnd);
            Logger.FETCHER.info("author : {}", author);


            Iterator<String> i1 = null;
            if (pageNum == 1) {
                int contentBeginIndex = rawString.indexOf("<div class=\"bbs-content clearfix\">") + "<div class=\"bbs-content clearfix\">".length();
                String rawString1 = rawString.substring(contentBeginIndex);
                int contentEndIndex = rawString1.indexOf("</div>");
                String content = rawString1.substring(0, contentEndIndex).trim().replaceAll("<br>", "\n").replaceAll("<img[^>]*>", "")
                        .replaceAll("评论.*：", "");
                // 内容有天涯两个字就略过
                if (content.indexOf("天涯") > 0 ||
                        content.indexOf("tianya") > 0) {
                    return;
                }

                String rawString2 = rawString1.substring(contentEndIndex);

                // save to db
                try {
                    // 判断是否已经插入过了，插入过了就放弃
                    if (!fPostDAO.checkExist(postId)) {
                        fPostDAO.add(Constants.Fetcher.SourceType.TIANYA, postId, title, content);
                        Logger.FETCHER.info("postId={},content={}", postId, content);
                    } else {
                        Logger.FETCHER.info("post existed postId={}", postId);
                    }
                } catch (Exception e) {
                    Logger.FETCHER.error("", e);
                }
                i1 = Splitter.on("<div class=\"atl-item\" _host=\"").split(rawString2).iterator();

            } else {
                i1 = Splitter.on("<div class=\"atl-item\" _host=\"").split(rawString).iterator();

            }


            i1.next();
            while (i1.hasNext()) {


                String replyRawString = i1.next();

                // 找到author
                int replyAuthorIndexEnd = replyRawString.indexOf("\" id=\"");
                String replyAuthor = replyRawString.substring(0, replyAuthorIndexEnd);

                // 找到authorId
                int replyAuthroIdIndexStart = replyRawString.indexOf("_hostid=\"") + "_hostid=\"".length();
                int replyAuthroIdIndexEnd = replyRawString.indexOf("\"", replyAuthroIdIndexStart);
                String authorId = replyRawString.substring(replyAuthroIdIndexStart, replyAuthroIdIndexEnd);


                // 截取到内容的位置
                int replyContentStartIndex = replyRawString.indexOf("<div class=\"bbs-content\">") + "<div class=\"bbs-content\">".length();
                replyRawString = replyRawString.substring(replyContentStartIndex);

                int replyContentEndIndex = replyRawString.indexOf("</div>");
                String replyContent = replyRawString.substring(0, replyContentEndIndex).trim();

                // 找到一级评论的replyId
                int replyIdStartIndex = replyRawString.indexOf("<div class=\"atl-reply\" id=\"rid_") + "<div class=\"atl-reply\" id=\"rid_".length();
                int replyIdEndIndex = replyRawString.indexOf("\">", replyIdStartIndex);
                String replyId = replyRawString.substring(replyIdStartIndex, replyIdEndIndex);

                // 如果有图片 continue
                if (replyContent.indexOf("<img src=") > 0) {
                    continue;
                }

                // 判断是否有tianya.cn的域名，如果有的话整体抛弃
                if (replyContent.indexOf("tianya.cn") > 0) {
                    continue;
                }
                // 天涯的字符串替换
                replyContent = replyContent.replaceAll("天涯", "豆豆");


                // 去掉类似于
                //                赶上直播了^_^
                // 　　-----------------------------
                //　　哈哈，晚上睡不着，码字的人都是夜猫子，等等马上更
                // ----之前的内容
                if (replyContent.indexOf("----------") > 0) {
                    int slashIndexEnd = replyContent.lastIndexOf("----------") + "----------".length();
                    replyContent = replyContent.substring(slashIndexEnd);
                }

                replyContent = replyContent.replaceAll("<img[^>]*>", "").replaceAll("评论.*：", "");

                if (replyContent.indexOf("<br>") >= 0) {
                    int prefixUserInfoIndex = replyContent.indexOf("<br>");
                    if (prefixUserInfoIndex == 0) {
                        replyContent = replyContent.substring(4);
                    }
                    replyContent = replyContent.replaceAll("<br>", "\n");
                }
                // 如果最后是\n替换掉
                if (replyContent.length() == 0) {
                    continue;
                } else {
                    int nIndexEnd = replyContent.lastIndexOf("\n") + "\n".length();
                    if (replyContent.length() == nIndexEnd) {
                        replyContent.substring(0, replyContent.lastIndexOf("\n"));
                    }
                }

                //丢弃掉发红包相关的评论
                if (replyContent.indexOf("<div class=\"red-pkt-v2 red-pkt-3\" title=") > 0)
                    continue;


                int fReplyId = 0;
                if (!fReplyDAO.checkExist(new Integer(replyId))) {
                    fReplyId = fReplyDAO.add(postId, 0, replyContent, new Integer(replyId),
                            replyAuthor.equals(author) ? 1 : 0);
                    Logger.FETCHER.info("-LEVEL1-- author:{} [{}]{}", replyAuthor.equals(author) ? 1 : 0, replyId, replyContent);
                    saveUser(replyAuthor, new Integer(authorId));
                } else {
                    fReplyId = fReplyDAO.findFReplyId(new Integer(replyId));
                    Logger.FETCHER.info("-LEVEL1-- replyId={} existed", replyId);
                }


                Iterator<String> i3 = Splitter.on("<li id=\"itemreply-").split(replyRawString).iterator();
                i3.next();
                while (i3.hasNext()) {
                    String replyReplyRawString = i3.next();
                    int replyReplyIdEndIndex = replyReplyRawString.indexOf("\"");
                    String replyReplyId = replyReplyRawString.substring(0, replyReplyIdEndIndex);

                    // 找到authorId
                    int replyReplyAuthorIdIndexStart = replyReplyRawString.indexOf("_userid=\"") + "_userid=\"".length();
                    int replyReplyAuthorIdIndexEnd = replyReplyRawString.indexOf("\" _username=\"");
                    int replyReplyAuthorId = new Integer(replyReplyRawString.substring(replyReplyAuthorIdIndexStart, replyReplyAuthorIdIndexEnd));

                    // 找到author
                    int replyReplyAuthorIndexStart = replyReplyRawString.indexOf("_username=\"") + "_username=\"".length();
                    int replyReplyAuthorIndexEnd = replyReplyRawString.indexOf("\"", replyReplyAuthorIndexStart);
                    String replyReplyAuthor = replyReplyRawString.substring(replyReplyAuthorIndexStart, replyReplyAuthorIndexEnd);


                    int replyReplyContentStartIndex = replyReplyRawString.indexOf("<span class=\"ir-content\">") + "<span class=\"ir-content\">".length();
                    replyReplyRawString = replyReplyRawString.substring(replyReplyContentStartIndex);
                    int replyReplyContentEndIndex = replyReplyRawString.indexOf("</span>");
                    String replyReplyRawContent = replyReplyRawString.substring(0, replyReplyContentEndIndex).replaceAll("评论.*：", "");
                    String replyReplyContent = null;

                    // 如果有图片 continue
                    if (replyReplyRawContent.indexOf("<img src=") > 0) {
                        continue;
                    }

                    if (replyReplyRawContent.indexOf("</a>") > 0) {
                        // 有评论两个字，需要去掉
                        int aEndIndex = replyReplyRawContent.indexOf("：") + "：".length();
                        replyReplyContent = replyReplyRawContent.substring(aEndIndex).
                                replaceAll("<img[^>]*>", "").replaceAll("<br>", "\n");
                    } else {
                        replyReplyContent = replyReplyRawContent.replaceAll("<img[^>]*>", "").replaceAll("<br>", "\n");
                    }

                    // 判断是否有tianya.cn的域名，如果有的话整体抛弃
                    if (replyReplyContent.indexOf("tianya.cn") > 0) {
                        continue;
                    }
                    // 天涯的字符串替换
                    replyReplyContent = replyReplyContent.replaceAll("天涯", "豆豆");

                    // 去掉类似于
                    //                赶上直播了^_^
                    // 　　-----------------------------
                    //　　哈哈，晚上睡不着，码字的人都是夜猫子，等等马上更
                    // ----之前的内容
                    if (replyReplyContent.indexOf("----------") > 0) {
                        int slashIndexEnd = replyReplyContent.lastIndexOf("----------") + "----------".length();
                        replyContent = replyReplyContent.substring(slashIndexEnd);
                    }


                    // 如果最后是\n替换掉
                    if (replyReplyContent.length() == 0) {
                        continue;
                    } else {
                        int nIndexEnd = replyReplyContent.lastIndexOf("\n") + "\n".length();
                        if (replyReplyContent.length() == nIndexEnd) {
                            replyReplyContent.substring(0, replyReplyContent.lastIndexOf("\n"));
                        }
                    }

                    //丢弃掉发红包相关的评论
                    if (replyReplyContent.indexOf("<div class=\"red-pkt-v2 red-pkt-3\" title=") > 0)
                        continue;

//                if (replyReplyContent.indexOf("<img src=") > 0) {
//                    Logger.FETCHER.info("reply have img ignore. content={}", replyReplyContent);
//                    continue;
//                }


                    if (replyReplyContent.indexOf("<br>") >= 0) {
                        int prefixUserInfoIndex2 = replyReplyContent.indexOf("<br>");
                        replyReplyContent = replyReplyContent.substring(prefixUserInfoIndex2 + "<br>".length()).replaceAll("<br>", "\n");
                    }


                    if (!fReplyDAO.checkExist(new Integer(replyReplyId))) {
                        fReplyDAO.add(postId, new Integer(fReplyId), replyReplyContent, new Integer(replyReplyId),
                                replyReplyAuthor.equals(author) ? 1 : 0);
                        Logger.FETCHER.info("--LEVEL2- author:{} -[{}]{}", replyReplyAuthor.equals(author) ? 1 : 0, replyReplyId, replyReplyContent);
                        saveUser(replyReplyAuthor, replyReplyAuthorId);

                    } else {
                        Logger.FETCHER.info("--LEVEL2- replyId={} existed", replyReplyId);
                    }

                }
            }
            pageNum++;
        }


    }

    public void saveUser(String nickName, int userId) {
//        boolean exist = userDAO.checkNickNameExisted(nickName);
//        if (exist) {
//            Logger.FETCHER.info("userName={},existed. so continue. currentId={}", nickName, userId);
//            try {
//                Thread.sleep(1000L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return;
//        }
//
//        String userImgUrl = "http://tx.tianyaui.com/logo/" + userId;
//
//        ImgVO imgVO = imgService.saveFile(userImgUrl, Constants.IMG_TYPE.AVATAR);
//
//        String anonNickName = "匿名侠" + random.nextInt(1000000);
//        // 插入到user_info表中
//        int savedUserId = userDAO.register(nickName, anonNickName, imgVO.getId(), Constants.USER_SOURCE_TYPE.MACHINE);
//
//        userDAO.registerUserInfoFromWechat(savedUserId, "Chaoyang", "China", "Beijing", Constants.User.Gender.NO, "zh_CN");
//        Logger.FETCHER.info("get userName={}  currentId={}", nickName, userId);
    }


    public static void main(String[] args) {

//        TianyaFetcher f = new TianyaFetcher();
//        f.doFetch();

//        InternetUtil.get("http://bbs.tianya.cn/post-feeling-4446799-1.shtml");
//        f.parsePostPage(4384551);
    }


}
