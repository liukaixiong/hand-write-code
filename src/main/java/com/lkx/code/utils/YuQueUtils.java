package com.lkx.code.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;

/**
 * @author liukaixiong
 * @Email liukx@elab-plus.com
 * @date 2021/11/25 - 17:10
 */
public class YuQueUtils {

    // https://www.yuque.com/kaixiong820/yxbn6y/vuvtb6/markdown?attachment=true&latexcode=true&anchor=true&linebreak=true

    private static final String TYPE_TITLE = "TITLE";
    private static final String TYPE_DOC = "DOC";
    public static final String indexUrl =
        "https://www.yuque.com/kaixiong820/yxbn6y/%s/markdown?attachment=true&latexcode=false&anchor=false&linebreak=false";

    private static final String filterString = "&name=image.png";

    public static void download(String bookList, String fileDir) throws Exception {
        List<YuQue> yuQueList = JSON.parseArray(bookList, YuQue.class);
        // 构建缓存
        Map<String, YuQue> dataCache =
            yuQueList.stream().collect(Collectors.toMap(YuQue::getUuid, Function.identity()));
        for (int i = 0; i < yuQueList.size(); i++) {
            YuQue yuQue = yuQueList.get(i);
            String title = yuQue.getTitle();
            // 先构建目录
            if (TYPE_TITLE.equals(yuQue.getType())) { // 如果是目录
                String rootTitle = getTitle(dataCache, yuQue, title);
                String fileDirPath = fileDir + "/" + rootTitle;
                File file = new File(fileDirPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
            } else {
                String url = yuQue.getUrl();
                String rootTitle = getTitle(dataCache, yuQue, "");
                String fileDirPath = fileDir + "/" + rootTitle;

                File file = new File(fileDirPath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String requestUrl = String.format(indexUrl, url);
                System.out.println(requestUrl + "\t" + JSON.toJSONString(yuQue));
                try {
                    String content =
                        HttpUtil.downloadString(requestUrl, Charset.defaultCharset()).replaceAll(filterString, "");
                    FileUtil.writeString(content, fileDirPath + "/" + yuQue.getTitle() + ".md", "utf-8");
                    // HttpUtil.downloadFile(requestUrl, fileDirPath + "/" + yuQue.getTitle() + ".md");
                } catch (Exception e) {
                    e.getMessage();
                }
                Thread.sleep(500);
            }
        }
        // 然后下载文件

        // 最后迁移文件到目录
    }

    private static String getTitle(Map<String, YuQue> dataCache, YuQue yuQue, String title) {
        String parentUuid = yuQue.getParentUuid();
        if (parentUuid == null || "".equals(parentUuid)) {
            return title;
        } else {
            YuQue parentYuQue = dataCache.get(parentUuid);
            return getTitle(dataCache, parentYuQue, parentYuQue.getTitle() + "/" + title);
        }
    }

    public static void main(String[] args) throws Exception {
        // 第一步: 打开语雀，来到你要导出的知识库。
        // 第二步: 打开F12 在console输入 window.appData.book.toc 得到结果
        String bookData =
            "[{\"type\":\"TITLE\",\"title\":\"设计模式实战\",\"uuid\":\"zNL8tABCjSMpP_ff\",\"url\":\"\",\"parent_uuid\":\"\",\"doc_id\":\"\",\"level\":0,\"id\":\"\",\"open_window\":1,\"visible\":1,\"child_uuid\":\"sQ4nsjRUUrTLFXIs\",\"sibling_uuid\":\"HKgnzUp1KxE3IAvy\"},{\"type\":\"DOC\",\"title\":\"观察者模式\",\"uuid\":\"sQ4nsjRUUrTLFXIs\",\"url\":\"vuvtb6\",\"parent_uuid\":\"zNL8tABCjSMpP_ff\",\"doc_id\":56446545,\"level\":1,\"id\":56446545,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"zNL8tABCjSMpP_ff\",\"sibling_uuid\":\"jRehXJPo5ehYL2dg\"},{\"type\":\"DOC\",\"title\":\"模版方法\",\"uuid\":\"jRehXJPo5ehYL2dg\",\"url\":\"qx1oyw\",\"parent_uuid\":\"zNL8tABCjSMpP_ff\",\"doc_id\":56445101,\"level\":1,\"id\":56445101,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"sQ4nsjRUUrTLFXIs\",\"sibling_uuid\":\"UYkv5H1Ao_zMkp8Z\"},{\"type\":\"DOC\",\"title\":\"责任链模式\",\"uuid\":\"UYkv5H1Ao_zMkp8Z\",\"url\":\"cpe5o7\",\"parent_uuid\":\"zNL8tABCjSMpP_ff\",\"doc_id\":56432001,\"level\":1,\"id\":56432001,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"jRehXJPo5ehYL2dg\",\"sibling_uuid\":\"GuAD4iZ4171GRYmA\"},{\"type\":\"DOC\",\"title\":\"策略模式\",\"uuid\":\"GuAD4iZ4171GRYmA\",\"url\":\"xdl934\",\"parent_uuid\":\"zNL8tABCjSMpP_ff\",\"doc_id\":56430687,\"level\":1,\"id\":56430687,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"UYkv5H1Ao_zMkp8Z\"},{\"type\":\"TITLE\",\"title\":\"监控实战\",\"uuid\":\"HKgnzUp1KxE3IAvy\",\"url\":\"\",\"parent_uuid\":\"\",\"doc_id\":\"\",\"level\":0,\"id\":\"\",\"open_window\":1,\"visible\":1,\"prev_uuid\":\"zNL8tABCjSMpP_ff\",\"child_uuid\":\"S2NzepqJO54hEx9k\",\"sibling_uuid\":\"UXoHw6QqjqoCTft3\"},{\"type\":\"TITLE\",\"title\":\"arthas 实战\",\"uuid\":\"S2NzepqJO54hEx9k\",\"url\":\"\",\"parent_uuid\":\"HKgnzUp1KxE3IAvy\",\"doc_id\":\"\",\"level\":1,\"id\":\"\",\"open_window\":1,\"visible\":1,\"prev_uuid\":\"HKgnzUp1KxE3IAvy\",\"child_uuid\":\"I-RG4-0xMHCHEMwQ\",\"sibling_uuid\":\"-LLwSjI68SSvEJAi\"},{\"type\":\"DOC\",\"title\":\"1. arthas 调试实战\",\"uuid\":\"I-RG4-0xMHCHEMwQ\",\"url\":\"pcdi87\",\"parent_uuid\":\"S2NzepqJO54hEx9k\",\"doc_id\":60818009,\"level\":2,\"id\":60818009,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"S2NzepqJO54hEx9k\"},{\"type\":\"TITLE\",\"title\":\"流量回放\",\"uuid\":\"-LLwSjI68SSvEJAi\",\"url\":\"\",\"parent_uuid\":\"HKgnzUp1KxE3IAvy\",\"doc_id\":\"\",\"level\":1,\"id\":\"\",\"open_window\":1,\"visible\":1,\"prev_uuid\":\"S2NzepqJO54hEx9k\",\"child_uuid\":\"r9oWgfWLcT5ZTc1N\",\"sibling_uuid\":\"tNPd_iwF2OYXDC-J\"},{\"type\":\"DOC\",\"title\":\"相关资料\",\"uuid\":\"r9oWgfWLcT5ZTc1N\",\"url\":\"gg0gk1\",\"parent_uuid\":\"-LLwSjI68SSvEJAi\",\"doc_id\":56889802,\"level\":2,\"id\":56889802,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"-LLwSjI68SSvEJAi\"},{\"type\":\"TITLE\",\"title\":\"jvm-sandbox-repeater : 基于沙箱的流量录制回放应用\",\"uuid\":\"tNPd_iwF2OYXDC-J\",\"url\":\"\",\"parent_uuid\":\"HKgnzUp1KxE3IAvy\",\"doc_id\":\"\",\"level\":1,\"id\":\"\",\"open_window\":1,\"visible\":1,\"prev_uuid\":\"-LLwSjI68SSvEJAi\",\"child_uuid\":\"XtGB1meFscxRZ1RJ\",\"sibling_uuid\":\"H1FjGfyE9paZmYIQ\"},{\"type\":\"DOC\",\"title\":\"环境搭建\",\"uuid\":\"XtGB1meFscxRZ1RJ\",\"url\":\"pu0d2w\",\"parent_uuid\":\"tNPd_iwF2OYXDC-J\",\"doc_id\":56866837,\"level\":2,\"id\":56866837,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"tNPd_iwF2OYXDC-J\",\"sibling_uuid\":\"rdURA8K5CwqcIqFw\"},{\"type\":\"DOC\",\"title\":\"源码走读\",\"uuid\":\"rdURA8K5CwqcIqFw\",\"url\":\"tsw8cp\",\"parent_uuid\":\"tNPd_iwF2OYXDC-J\",\"doc_id\":56855767,\"level\":2,\"id\":56855767,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"XtGB1meFscxRZ1RJ\"},{\"type\":\"TITLE\",\"title\":\"jvm-sandbox : JVM沙箱容器，一种JVM的非侵入式运行期AOP解决方案\",\"uuid\":\"H1FjGfyE9paZmYIQ\",\"url\":\"\",\"parent_uuid\":\"HKgnzUp1KxE3IAvy\",\"doc_id\":\"\",\"level\":1,\"id\":\"\",\"open_window\":1,\"visible\":1,\"prev_uuid\":\"tNPd_iwF2OYXDC-J\",\"child_uuid\":\"y10HUScy8UTFfIIU\"},{\"type\":\"DOC\",\"title\":\"功能报表\",\"uuid\":\"y10HUScy8UTFfIIU\",\"url\":\"wmc7q9\",\"parent_uuid\":\"H1FjGfyE9paZmYIQ\",\"doc_id\":57151747,\"level\":2,\"id\":57151747,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"H1FjGfyE9paZmYIQ\",\"sibling_uuid\":\"ljXqTc0kJ5heXAiM\"},{\"type\":\"DOC\",\"title\":\"jvm-sandbox实战之windows调试\",\"uuid\":\"ljXqTc0kJ5heXAiM\",\"url\":\"nnadhr\",\"parent_uuid\":\"H1FjGfyE9paZmYIQ\",\"doc_id\":57105723,\"level\":2,\"id\":57105723,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"y10HUScy8UTFfIIU\",\"sibling_uuid\":\"tmXuNA-s7Jh-W78G\"},{\"type\":\"DOC\",\"title\":\"1. 环境搭建\",\"uuid\":\"tmXuNA-s7Jh-W78G\",\"url\":\"tlnl6n\",\"parent_uuid\":\"H1FjGfyE9paZmYIQ\",\"doc_id\":56375147,\"level\":2,\"id\":56375147,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"ljXqTc0kJ5heXAiM\",\"sibling_uuid\":\"I2S_b7wDn5zUkelo\"},{\"type\":\"DOC\",\"title\":\"2. sandbox IDEA Debug 调试\",\"uuid\":\"I2S_b7wDn5zUkelo\",\"url\":\"hm9axc\",\"parent_uuid\":\"H1FjGfyE9paZmYIQ\",\"doc_id\":56551717,\"level\":2,\"id\":56551717,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"tmXuNA-s7Jh-W78G\",\"sibling_uuid\":\"6VXd9S43c7AeVMSG\"},{\"type\":\"DOC\",\"title\":\"3. 各个模块的介绍和使用\",\"uuid\":\"6VXd9S43c7AeVMSG\",\"url\":\"xzx5gb\",\"parent_uuid\":\"H1FjGfyE9paZmYIQ\",\"doc_id\":56797025,\"level\":2,\"id\":56797025,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"I2S_b7wDn5zUkelo\",\"sibling_uuid\":\"u-IGKfKpxqiDr_45\"},{\"type\":\"DOC\",\"title\":\"4. 编写一个案例\",\"uuid\":\"u-IGKfKpxqiDr_45\",\"url\":\"vshcb3\",\"parent_uuid\":\"H1FjGfyE9paZmYIQ\",\"doc_id\":56790556,\"level\":2,\"id\":56790556,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"6VXd9S43c7AeVMSG\",\"sibling_uuid\":\"jcxt-Bwt6RXbcY6x\"},{\"type\":\"DOC\",\"title\":\"5. 源码流程梳理\",\"uuid\":\"jcxt-Bwt6RXbcY6x\",\"url\":\"ggdew3\",\"parent_uuid\":\"H1FjGfyE9paZmYIQ\",\"doc_id\":56824442,\"level\":2,\"id\":56824442,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"u-IGKfKpxqiDr_45\"},{\"type\":\"TITLE\",\"title\":\"SpringCloud实战\",\"uuid\":\"UXoHw6QqjqoCTft3\",\"url\":\"\",\"parent_uuid\":\"\",\"doc_id\":\"\",\"level\":0,\"id\":\"\",\"open_window\":1,\"visible\":1,\"prev_uuid\":\"HKgnzUp1KxE3IAvy\",\"child_uuid\":\"rxM_7SI6nkHH7AnT\",\"sibling_uuid\":\"XPnoOpq4tRX9Vd-h\"},{\"type\":\"DOC\",\"title\":\"1. SpringCloud 搭建实战\",\"uuid\":\"rxM_7SI6nkHH7AnT\",\"url\":\"eupcvf\",\"parent_uuid\":\"UXoHw6QqjqoCTft3\",\"doc_id\":56007282,\"level\":1,\"id\":56007282,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"UXoHw6QqjqoCTft3\"},{\"type\":\"TITLE\",\"title\":\"设计模式实战\",\"uuid\":\"XPnoOpq4tRX9Vd-h\",\"url\":\"\",\"parent_uuid\":\"\",\"doc_id\":\"\",\"level\":0,\"id\":\"\",\"open_window\":1,\"visible\":1,\"prev_uuid\":\"UXoHw6QqjqoCTft3\",\"child_uuid\":\"_JVELT6Vhnpdihrz\",\"sibling_uuid\":\"yX0KgBDCJOlqfyd0\"},{\"type\":\"DOC\",\"title\":\"解释器模式\",\"uuid\":\"_JVELT6Vhnpdihrz\",\"url\":\"uwrppp\",\"parent_uuid\":\"XPnoOpq4tRX9Vd-h\",\"doc_id\":53315333,\"level\":1,\"id\":53315333,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"XPnoOpq4tRX9Vd-h\"},{\"type\":\"TITLE\",\"title\":\"Spring实战\",\"uuid\":\"yX0KgBDCJOlqfyd0\",\"url\":\"\",\"parent_uuid\":\"\",\"doc_id\":\"\",\"level\":0,\"id\":\"\",\"open_window\":1,\"visible\":1,\"prev_uuid\":\"XPnoOpq4tRX9Vd-h\",\"child_uuid\":\"TELfQqT0n2wRDAS8\",\"sibling_uuid\":\"DhU-sVB-6Q7SPHtJ\"},{\"type\":\"DOC\",\"title\":\"Bean的生命周期\",\"uuid\":\"TELfQqT0n2wRDAS8\",\"url\":\"tk1me8\",\"parent_uuid\":\"yX0KgBDCJOlqfyd0\",\"doc_id\":54314386,\"level\":1,\"id\":54314386,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"yX0KgBDCJOlqfyd0\",\"sibling_uuid\":\"jA5EM1UznU5ZRlwJ\"},{\"type\":\"DOC\",\"title\":\"Bean的阶段拓展点\",\"uuid\":\"jA5EM1UznU5ZRlwJ\",\"url\":\"wd5w7v\",\"parent_uuid\":\"yX0KgBDCJOlqfyd0\",\"doc_id\":54314272,\"level\":1,\"id\":54314272,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"TELfQqT0n2wRDAS8\",\"sibling_uuid\":\"zNpHMhGaXH62eI_o\"},{\"type\":\"DOC\",\"title\":\"功能处理类列表\",\"uuid\":\"zNpHMhGaXH62eI_o\",\"url\":\"kgasd1\",\"parent_uuid\":\"yX0KgBDCJOlqfyd0\",\"doc_id\":54313730,\"level\":1,\"id\":54313730,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"jA5EM1UznU5ZRlwJ\",\"sibling_uuid\":\"WNtRwDt00SifoBby\"},{\"type\":\"DOC\",\"title\":\"注解处理器\",\"uuid\":\"WNtRwDt00SifoBby\",\"url\":\"bo7ohl\",\"parent_uuid\":\"yX0KgBDCJOlqfyd0\",\"doc_id\":54313372,\"level\":1,\"id\":54313372,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"zNpHMhGaXH62eI_o\",\"sibling_uuid\":\"XYw2d-JPosqnABcB\"},{\"type\":\"DOC\",\"title\":\"Expression表达式\",\"uuid\":\"XYw2d-JPosqnABcB\",\"url\":\"expression\",\"parent_uuid\":\"yX0KgBDCJOlqfyd0\",\"doc_id\":53275053,\"level\":1,\"id\":53275053,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"WNtRwDt00SifoBby\"},{\"type\":\"TITLE\",\"title\":\"编码设计心得\",\"uuid\":\"DhU-sVB-6Q7SPHtJ\",\"url\":\"\",\"parent_uuid\":\"\",\"doc_id\":\"\",\"level\":0,\"id\":\"\",\"open_window\":1,\"visible\":1,\"prev_uuid\":\"yX0KgBDCJOlqfyd0\",\"child_uuid\":\"dsdSsjt6NWxe8NZB\"},{\"type\":\"DOC\",\"title\":\"Java必会工具库，代码量减少90%\",\"uuid\":\"dsdSsjt6NWxe8NZB\",\"url\":\"qgv1mw\",\"parent_uuid\":\"DhU-sVB-6Q7SPHtJ\",\"doc_id\":60696614,\"level\":1,\"id\":60696614,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"DhU-sVB-6Q7SPHtJ\",\"sibling_uuid\":\"E35dR4yXGJqQqMLj\"},{\"type\":\"DOC\",\"title\":\"基于模版方法模式优化if、else\",\"uuid\":\"E35dR4yXGJqQqMLj\",\"url\":\"yr6qrq\",\"parent_uuid\":\"DhU-sVB-6Q7SPHtJ\",\"doc_id\":56453751,\"level\":1,\"id\":56453751,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"dsdSsjt6NWxe8NZB\",\"sibling_uuid\":\"-6GZ4vFazASuIuj4\"},{\"type\":\"DOC\",\"title\":\"收藏这36个正则表达式，开发效率提高80%\",\"uuid\":\"-6GZ4vFazASuIuj4\",\"url\":\"fn9bgn\",\"parent_uuid\":\"DhU-sVB-6Q7SPHtJ\",\"doc_id\":55919462,\"level\":1,\"id\":55919462,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"E35dR4yXGJqQqMLj\",\"sibling_uuid\":\"POFrepuJgRHkzMqA\"},{\"type\":\"DOC\",\"title\":\"任务线程统一管理\",\"uuid\":\"POFrepuJgRHkzMqA\",\"url\":\"fzh4lz\",\"parent_uuid\":\"DhU-sVB-6Q7SPHtJ\",\"doc_id\":53147084,\"level\":1,\"id\":53147084,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"-6GZ4vFazASuIuj4\",\"sibling_uuid\":\"tqk06y2vanwjltHu\"},{\"type\":\"DOC\",\"title\":\"容器的注册回调\",\"uuid\":\"tqk06y2vanwjltHu\",\"url\":\"keaxep\",\"parent_uuid\":\"DhU-sVB-6Q7SPHtJ\",\"doc_id\":53145865,\"level\":1,\"id\":53145865,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"POFrepuJgRHkzMqA\",\"sibling_uuid\":\"_pqjqPKp3vHVmkcI\"},{\"type\":\"DOC\",\"title\":\"集合索引分组\",\"uuid\":\"_pqjqPKp3vHVmkcI\",\"url\":\"tbvzff\",\"parent_uuid\":\"DhU-sVB-6Q7SPHtJ\",\"doc_id\":53148129,\"level\":1,\"id\":53148129,\"open_window\":1,\"visible\":1,\"prev_uuid\":\"tqk06y2vanwjltHu\"}]";
        download(bookData, "E:\\tmp1\\yuque");
        // 这里需要注意的是如果有图片，图片的路径参数?后面如果有小数点可能会显示不了图片
    }

}
