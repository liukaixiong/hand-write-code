package com.lkx.code.utils;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * 批量上传到maven私服
 */
public class Deploy {

    // 你要上传到的maven私服地址
    private  static String repositoryURL = "http://192.168.4.222:8081/repository/maven-releases/";
    // 注意 : 这里是setting文件中的server定义的帐号和密码
    private  static String repositoryId = "releases";

    /**
     * mvn -s F:\.m2\settings.xml
     * org.apache.maven.plugins:maven-deploy-plugin:2.8.2:deploy-file
     * -Durl=http://IP:PORT/nexus/content/repositories/thirdpart
     * -DrepositoryId=thirdpart
     * -Dfile=antlr-2.7.2.jar
     * -DpomFile=antlr-2.7.2.pom
     * -Dpackaging=jar
     * -DgeneratePom=false
     * -Dsources=./path/to/artifact-name-1.0-sources.jar
     * -Djavadoc=./path/to/artifact-name-1.0-javadoc.jar
     */
    public static final String BASE_CMD = "cmd /c mvn " +
            "deploy:deploy-file " +
            "-Durl=" + repositoryURL +
            " -DrepositoryId=" + repositoryId +
            " -DgeneratePom=false";

    public static final Pattern DATE_PATTERN = Pattern.compile("-[\\d]{8}\\.[\\d]{6}-");

    public static final Runtime CMD = Runtime.getRuntime();

    public static final Writer ERROR;

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(10);

    static {
        Writer err = null;
        try {
            err = new OutputStreamWriter(new FileOutputStream("deploy-error.log"), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        ERROR = err;
    }


    public static void error(String error) {
        try {
            System.err.println(error);
            ERROR.write(error + "\n");
            ERROR.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkArgs(String[] args) {
        if (args.length != 1) {
            System.out.println("用法如： java -jar Deploy D:\\some\\path\\");
            return false;
        }
        File file = new File(args[0]);
        if (!file.exists()) {
            System.out.println(args[0] + " 目录不存在!");
            return false;
        }
        if (!file.isDirectory()) {
            System.out.println("必须指定为目录!");
            return false;
        }
        return true;
    }

    public static void deploy(File[] files) {
        if (files.length == 0) {
            //ignore
        } else if (files[0].isDirectory()) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deploy(file.listFiles());
                }
            }
        } else if (files[0].isFile()) {
            File pom = null;
            File jar = null;
            File source = null;
            File javadoc = null;
            //忽略日期快照版本，如 xxx-mySql-2.2.6-20170714.095105-1.jar
            for (File file : files) {
                String name = file.getName();
                if (DATE_PATTERN.matcher(name).find()) {
                    //skip
                } else if (name.endsWith(".pom")) {
                    pom = file;
                } else if (name.endsWith("-javadoc.jar")) {
                    javadoc = file;
                } else if (name.endsWith("-sources.jar")) {
                    source = file;
                } else if (name.endsWith(".jar")) {
                    jar = file;
                }
            }
            if (pom != null) {
                if (jar != null) {
                    deploy(pom, jar, source, javadoc);
                } else if (packingIsPom(pom)) {
                    deployPom(pom);
                }
            }
        }
    }

    public static boolean packingIsPom(File pom) {
        BufferedReader reader = null;
        try {
            reader =
                    new BufferedReader(new InputStreamReader(new FileInputStream(pom)));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().indexOf("<packaging>pom</packaging>") != -1) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return false;
    }

    public static void deployPom(final File pom) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                StringBuffer cmd = new StringBuffer(BASE_CMD);
                cmd.append(" -DpomFile=").append(pom.getName());
                cmd.append(" -Dfile=").append(pom.getName());
                try {
                    final Process proc = CMD.exec(cmd.toString(), null, pom.getParentFile());
                    InputStream inputStream = proc.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    String line;
                    StringBuffer logBuffer = new StringBuffer();
                    logBuffer.append("\n\n\n==================================\n");
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("[INFO]") || line.startsWith("Upload")) {
                            logBuffer.append(Thread.currentThread().getName() + " : " + line + "\n");
                        }
                    }
                    System.out.println(logBuffer);
                    int result = proc.waitFor();
                    if (result != 0) {
                        error("上传失败1：" + pom.getAbsolutePath());
                    }
                } catch (IOException e) {
                    error("上传失败2：" + pom.getAbsolutePath());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    error("上传失败3：" + pom.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        });
    }

    public static void deploy(final File pom, final File jar, final File source, final File javadoc) {
        EXECUTOR_SERVICE.execute(new Runnable() {
            @Override
            public void run() {
                StringBuffer cmd = new StringBuffer(BASE_CMD);
                cmd.append(" -DpomFile=").append(pom.toString());
                if (jar != null) {
                    //当有bundle类型时，下面的配置可以保证上传的jar包后缀为.jar
                    cmd.append(" -Dpackaging=jar -Dfile=").append(jar.toString());
                } else {
                    cmd.append(" -Dfile=").append(pom.getName());
                }
                if (source != null) {
                    cmd.append(" -Dsources=").append(source.toString());
                }
                if (javadoc != null) {
                    cmd.append(" -Djavadoc=").append(javadoc.toString());
                }


                System.out.println(cmd.toString());
                try {
                    final Process proc = CMD.exec(cmd.toString(), null, pom.getParentFile());
                    InputStream inputStream = proc.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    String line;
                    StringBuffer logBuffer = new StringBuffer();
                    logBuffer.append("\n\n\n=======================================\n");
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("[INFO]") || line.startsWith("Upload")) {
                            logBuffer.append(Thread.currentThread().getName() + " : " + line + "\n");
                        }
                    }
                    System.out.println(logBuffer);
                    int result = proc.waitFor();
                    if (result != 0) {
                        error("上传失败4：" + pom.getAbsolutePath());
                    }
                } catch (IOException e) {
                    error("上传失败5：" + pom.getAbsolutePath());
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    error("上传失败6：" + pom.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) {
        // 注意 如果不成功,请把打印的cmd命令复制到命令窗口去执行.
        // 还需要注意一点，就是不要把路径指向到maven的本地仓库中。可以先把需要拷贝的包复制出来到一个文件夹统一上传
        String pathDir = "E:\\tmp1";
        deploy(new File(pathDir).listFiles());

        //  这是很早之前写的,代码写的有点烂了，就先不吐槽了。
        /**
         *
         * 先说明一下注意的点:
         * 1. 首先核对repositoryURL属性的URL是否匹配上.
         * 2. 再次核对资源仓库的编号repositoryId是否匹配上
         * 2.1 这个东西是和你配置的maven环境的setting.xml中的server的属性servers.server.id是否匹配上，参考
         * <p>
         *      <server>
         *       <id>releases</id>
         *       <username>admin</username>
         *       <password>12312312</password>
         *     </server>
         * </p>
         * 2.2 还有一个地方在idea会生效的就是C:\Users\用户目录\.m2下面也有一个setting.xml，这两个最好保持一致
         * 3. main方法中的pathDir只要是你想上传的jar包目录地址就行，不必非得和是com/aaa/***.xxx.jar,可以目录下面直接是***.xxx.jar但是最好是包含***-pom.xml文件
         * 4. 如果执行main方法报错了，就将打印出来的命令在CMD里面执行一遍，一般会告诉你出现了哪些问题，一般401就是排查第二步。
         *
         * 该类一般用于第三方包导入maven私服库，如果包过多麻烦的问题，通过该类将文件夹跑到对应的maven库中
         */

        EXECUTOR_SERVICE.shutdown();
        try {
            ERROR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}