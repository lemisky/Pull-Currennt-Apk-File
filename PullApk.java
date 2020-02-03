import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class PullApk {
    public static void main(String[] args) throws InterruptedException {
        List<String> activityTop = cmd("adb shell dumpsys activity top");
        for (int i = activityTop.size() - 1; i > 0; i--) {
            String s = activityTop.get(i).trim();
            if (s.contains("pid=")) {
                //当前应用包名
                String pName = s.substring(s.trim().indexOf(" ") + 1, s.indexOf("/"));
                System.out.println("当前应用包名：" + pName);
                //获取包名对应Apk文件路径
                List<String> pmList = cmd("adb shell pm list packages -f");
                for (String s1 :
                        pmList) {
                    if (s1.contains(pName)) {
                        s1 = s1.trim();
                        String pmPath = "adb pull "+s1.substring(s1.indexOf("/"), s1.lastIndexOf("=")) + " "+pName + ".apk";
                        System.out.println(cmd(pmPath));
                    }
                }
                //注释此行
                //提取所有打开apk
                break;
            }
        }
    }

    public static synchronized List<String> cmd(String command) {
        BufferedReader br = null;
        List<String> result = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec(command);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}
