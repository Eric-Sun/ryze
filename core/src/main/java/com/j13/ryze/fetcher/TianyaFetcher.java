package com.j13.ryze.fetcher;

import com.google.common.base.Splitter;
import com.j13.ryze.core.Constants;
import com.j13.ryze.core.Logger;
import com.j13.ryze.daos.FPostDAO;
import com.j13.ryze.daos.FReplyDAO;
import com.j13.ryze.services.PostService;
import com.j13.ryze.services.ReplyService;
import com.j13.ryze.utils.InternetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.regex.Pattern;

@Service
public class TianyaFetcher {

    @Autowired
    FPostDAO fPostDAO;
    @Autowired
    FReplyDAO fReplyDAO;

    private Pattern p = Pattern.compile("\\s*|\t|\r");


    public void doFetch() {
        try {
            String rawString = InternetUtil.get("http://bbs.tianya.cn/list-feeling-1.shtml");

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
            String rawString = InternetUtil.get("http://bbs.tianya.cn/post-feeling-" + postId + "-" + pageNum + ".shtml");
            if (rawString == null) {
                Logger.FETCHER.info("页面到底了 PageNum={}", pageNum);
                return;
            } else {
                Logger.FETCHER.info("开始抓取页面 pageNum={}", pageNum);
            }
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
                    }
                } catch (Exception e) {
                    Logger.FETCHER.error("", e);
                }
                Logger.FETCHER.info("content={}", content);
                i1 = Splitter.on("<div class=\"bbs-content\">").split(rawString2).iterator();

            } else {
                i1 = Splitter.on("<div class=\"bbs-content\">").split(rawString).iterator();

            }


            i1.next();
            while (i1.hasNext()) {
                String replyRawString = i1.next();
                int replyContentEndIndex = replyRawString.indexOf("</div>");
                String replyContent = replyRawString.substring(0, replyContentEndIndex).trim();

                // 找到一级评论的replyId
                int replyIdStartIndex = replyRawString.indexOf("<div class=\"atl-reply\" id=\"rid_") + "<div class=\"atl-reply\" id=\"rid_".length();
                int replyIdEndIndex = replyRawString.indexOf("\">", replyIdStartIndex);
                String replyId = replyRawString.substring(replyIdStartIndex, replyIdEndIndex);
//            replyContent = formatString(replyContent);
//            if (replyContent.indexOf("<img src=") > 0) {
//                Logger.FETCHER.info("reply have img ignore. content={}", replyContent);
//                continue;
//            }

                replyContent = replyContent.replaceAll("<br>", "\n").replaceAll("<img[^>]*>", "").replaceAll("评论.*：", "");

                if (replyContent.indexOf("<br>") >= 0) {
                    int prefixUserInfoIndex = replyContent.indexOf("<br>");
                    replyContent = replyContent.substring(prefixUserInfoIndex + "<br>".length()).replaceAll("<br>", "\n");
                }

                Logger.FETCHER.info("-LEVEL1-[{}]{}", replyId, replyContent);
                int fReplyId = 0;
                if (!fReplyDAO.checkExist(new Integer(replyId))) {
                    fReplyId = fReplyDAO.add(postId, 0, replyContent, new Integer(replyId));
                } else {
                    fReplyId = fReplyDAO.findFReplyId(new Integer(replyId));

                }


                Iterator<String> i3 = Splitter.on("<li id=\"itemreply-").split(replyRawString).iterator();
                i3.next();
                while (i3.hasNext()) {
                    String replyReplyRawString = i3.next();
                    int replyReplyIdEndIndex = replyReplyRawString.indexOf("\"");
                    String replyReplyId = replyReplyRawString.substring(0, replyReplyIdEndIndex);
                    int replyReplyContentStartIndex = replyReplyRawString.indexOf("<span class=\"ir-content\">") + "<span class=\"ir-content\">".length();
                    replyReplyRawString = replyReplyRawString.substring(replyReplyContentStartIndex);
                    int replyReplyContentEndIndex = replyReplyRawString.indexOf("</span>");
                    String replyReplyRawContent = replyReplyRawString.substring(0, replyReplyContentEndIndex);
                    String replyReplyContent = null;
                    if (replyReplyRawContent.indexOf("</a>") > 0) {
                        // 有评论两个字，需要去掉
                        int aEndIndex = replyReplyRawContent.indexOf("</a>：") + "</a>：".length();
                        replyReplyContent = replyReplyRawContent.substring(aEndIndex).
                                replaceAll("<img[^>]*>", "").replaceAll("<br>", "\n")
                                .replaceAll("评论.*：", "");
                        ;
                    } else {
                        replyReplyContent = replyReplyRawContent.replaceAll("<img[^>]*>", "").replaceAll("<br>", "\n")
                                .replaceAll("评论.*：", "");
                        ;
                    }

//                if (replyReplyContent.indexOf("<img src=") > 0) {
//                    Logger.FETCHER.info("reply have img ignore. content={}", replyReplyContent);
//                    continue;
//                }


                    if (replyReplyContent.indexOf("<br>") >= 0) {
                        int prefixUserInfoIndex2 = replyReplyContent.indexOf("<br>");
                        replyReplyContent = replyReplyContent.substring(prefixUserInfoIndex2 + "<br>".length()).replaceAll("<br>", "\n");
                    }
                    Logger.FETCHER.info("--LEVEL2--[{}]{}", replyReplyId, replyReplyContent);

                    try {
                        fReplyDAO.add(postId, new Integer(fReplyId), replyReplyContent, new Integer(replyReplyId));
                    } catch (Exception e) {
                        Logger.FETCHER.error("", e);
                    }

                }
            }
            pageNum++;
        }


    }


    public static void main(String[] args) {

//        TianyaFetcher f = new TianyaFetcher();
//        f.doFetch();

//        InternetUtil.get("http://bbs.tianya.cn/post-feeling-4446799-1.shtml");
//        f.parsePostPage(4384551);
    }


}
