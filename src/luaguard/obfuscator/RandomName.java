package luaguard.obfuscator;

import java.util.HashMap;
import java.util.Random;

public class RandomName {
    public RandomName() {
    }

    private String getRandomString(int length) {
        char[] names = new char[length];
        if(length == 0) {
            names = ("OTOSOTE" + (new Random()).nextInt(1000)).toCharArray();
        }

        if(length >= 1) {
            names[0] = (char)((new Random()).nextInt(26) + 97);
        }

        for(int i = 1; i < names.length; ++i) {
            int n = (new Random()).nextInt(3);
            switch(n) {
            case 0:
                names[i] = (char)((new Random()).nextInt(26) + 65);
                break;
            case 1:
                names[i] = (char)((new Random()).nextInt(26) + 97);
                break;
            case 2:
                names[i] = (char)((new Random()).nextInt(10) + 48);
                break;
            default:
                System.out.println("Random selection overflow");
            }
        }

        return new String(names);
    }

    public String generateVarName(HashMap<String, String> map) {
        String name;
        for(name = this.getRandomString(10); map.containsKey(name); name = this.getRandomString(10)) {
            ;
        }

        return name;
    }
}
