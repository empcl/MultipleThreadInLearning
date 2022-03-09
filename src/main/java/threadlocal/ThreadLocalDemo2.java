package threadlocal;

import java.util.HashMap;
import java.util.Map;

/**
 * 有一个数据表account，里面有两个用户Jack和Rose，
 * 用户Jack 给用户Rose 转账。
 */
public class ThreadLocalDemo2 {
    static Map<String, Integer> accountMap = new HashMap<>();


    public static void main(String[] args) {
        accountMap.put("Jack", 1000);
        accountMap.put("Rose", 0);
        String outUser = "Jack";
        String inUser = "Rose";

        int money = 100;
        final AccountService as = new AccountService();
        boolean result = as.transform(outUser, inUser, money);

        if (result) {
            System.out.println("转账成功～");
        } else {
            System.err.println("转账失败！");
        }
        System.out.println("当前情况：" + accountMap);
    }

    private static class AccountService {
        public boolean transform(String outUser, String inUser, int money) {
            final AccountDao ad = new AccountDao();

            try {
                ad.out(outUser, money);
                int i = 1 / 0;
                ad.in(inUser, money);
            } catch (Exception e) {
                ad.in(outUser, money);
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    private static class AccountDao {
        public void out(String outUser, int money) {
            accountMap.computeIfPresent(outUser, (k, v) -> v - money);
        }

        public void in(String inUser, int money) {
            accountMap.computeIfPresent(inUser, (k, v) -> v + money);
        }
    }
}