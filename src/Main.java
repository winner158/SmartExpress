import com.algorithm.LowestChargeAlgorithmDesign;
import com.algorithm.NearAlgorithmDesign;
import com.algorithm.NearNotCoAlgorithmDesign;
import com.config.Config;
import com.entity.ExpressS;
import com.entity.User;
import com.util.ImportAndExport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static List<User> getUserList(){
        //1.加载数据
        String userinfo = null;
        try {
            userinfo = ImportAndExport.importData("data/user.txt");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        //2.数据预处理
        String[] userSplit = userinfo.split("-");
        System.out.println(userSplit.length);
        List<User> userlist = new ArrayList<>();
        int count = 0;
        for (String str :
                userSplit) {
            User user = new User();
            String[] rowSplit = str.split(",");
            user.setId(count++);
            user.setJingdu(Double.parseDouble(rowSplit[0]));
            user.setWeidu(Double.parseDouble(rowSplit[1]));
            user.setWeight(Double.parseDouble(rowSplit[2]));
            user.setLengthP(Double.parseDouble(rowSplit[3]));
            user.setWidthP(Double.parseDouble(rowSplit[4]));
            user.setHeightP(Double.parseDouble(rowSplit[5]));
            user.setUnitCost(Config.unitCost);
            userlist.add(user);
        }
        return userlist;
    }

    public static List<ExpressS> getExpressSList(){
        //1.加载数据
        String Esinfo = null;
        try {
            Esinfo = ImportAndExport.importData("data/ES.txt");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        //2.数据预处理
        String[] EsinfoSplit = Esinfo.split("-");
        System.out.println(EsinfoSplit.length);
        List<ExpressS> EsinfoList = new ArrayList<>();
        int count = 0;
        for (String str :
                EsinfoSplit) {
            ExpressS es = new ExpressS();
            String[] rowSplit = str.split(",");
            es.setId(count++);
            es.setJingdu(Double.parseDouble(rowSplit[0]));
            es.setWeidu(Double.parseDouble(rowSplit[1]));
            es.setFirstPrice(Double.parseDouble(rowSplit[2]));
            es.setContinuePrice(Double.parseDouble(rowSplit[3]));
            es.setScale(Integer.parseInt(rowSplit[4]));
            EsinfoList.add(es);
        }
        return EsinfoList;
    }

    public static void main(String[] args) {

        //HashMap<Integer, Object> integerObjectHashMap = NearAlgorithmDesign.alocationMechnism(getExpressSList(), getUserList());
        NearAlgorithmDesign.calcluateCost(getExpressSList(), getUserList(),"result/NearAlgorithmDesign.txt");

        LowestChargeAlgorithmDesign.calcluateCost(getExpressSList(), getUserList(),"result/LowestChargeAlgorithmDesign.txt");

        NearNotCoAlgorithmDesign.calcluateCost(getExpressSList(), getUserList(),"result/NearNotCoAlgorithmDesign.txt");

       // System.out.println(LowestChargeAlgorithmDesign.alocationMechnism(getExpressSList(), getUserList()));


    }
}
