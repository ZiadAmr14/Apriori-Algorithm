import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class Apriori {

    public File file = new File("retail_dataset.txt");
    private int minimumSupport;
    private float minimumConfidence;

    public int getMinimumSupport() {
        return minimumSupport;
    }

    public void setMinimumSupport(int minimumSupport) {
        this.minimumSupport = minimumSupport;
    }

    public float getMinimumConfidence() {
        return minimumConfidence;
    }

    public void setMinimumConfidence(float minimumConfidence) {
        this.minimumConfidence = minimumConfidence;
    }


    //Get Frequency of each item
    public Map<String,Integer> CreateCandidate1(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        Map<String,Integer> mp = new HashMap<>();
        while (scanner.hasNextLine())
        {
            String ln = scanner.nextLine();
            String[] str = ln.split(",");
            for(int i=1;i<str.length;i++)
            {
                mp.put(str[i],mp.getOrDefault(str[i],0) + 1);
            }
        }
        return mp;
    }

    //Remove Items that did not pass the threshold
    public Map<String,Integer> excludeUnderMinimumSupport(Map<String,Integer> mp)
    {
        Iterator<Map.Entry<String, Integer>> iterator = mp.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue() < minimumSupport)
                iterator.remove();
        }
        return mp;
    }


    //Get Support of an item set in candidate2
    public int getSupportOfSubset(String str1,String str2) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int support =0;
        while (scanner.hasNextLine())
        {

            String line = scanner.nextLine();
            String[] str = line.split(",");
            boolean flag1 = false;
            boolean flag2 = false;
            for(int i=0;i<str.length;i++)
            {
                if(str[i].equals(str1))
                    flag1 = true;
                if(str[i].equals(str2))
                    flag2 = true;
            }
            if(flag2 && flag1)
                support++;

        }
        return support;
    }

    //Create candidate2 with the items that passed the threshold
    public Map<String,Integer> createCandidate2(Map<String,Integer> mp) throws FileNotFoundException {
        Map<String,Integer> candidate2Map = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : mp.entrySet())
            list.add(entry.getKey());
        for(int i=0;i<list.size();i++)
        {
            for(int j=i+1;j<list.size();j++)
            {
                if(getSupportOfSubset(list.get(i),list.get(j)) > minimumSupport)
                {
                    int support = getSupportOfSubset(list.get(i),list.get(j));
                    String temp = list.get(i) + "," + list.get(j);
                    candidate2Map.put(temp, support);
                }
            }
        }
        return candidate2Map;
    }


    //To remove duplicates in case of {bread ,Milk} and {Milk,Diapers} for example
    public ArrayList<String> removeDuplicates(String S1, String S2)
    {
        StringBuilder str = new StringBuilder();
        str.append(S1);
        str.append(',');
        str.append(S2);
        String temp = str.toString();
        String[] tempStr = temp.split(",");
        ArrayList<String> tempArr = new ArrayList<>(Arrays.asList(tempStr));
        Set<String>set = new LinkedHashSet<>();
        set.addAll(tempArr);
        tempArr.clear();
        tempArr.addAll(set);

        return tempArr;
    }


    //Get support of an item set in candidate 3
    public int getSupportOfSubset(String str1,String str2,String str3) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int support =0;
        while (scanner.hasNextLine())
        {

            String line = scanner.nextLine();
            String[] str = line.split(",");
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            for(int i=0;i<str.length;i++)
            {
                if(str[i].equals(str1))
                    flag1 = true;
                if(str[i].equals(str2))
                    flag2 = true;
                if(str[i].equals(str3))
                    flag3 = true;
            }
            if(flag2 && flag1 && flag3)
                support++;

        }
        return support;
    }

    public Map<String,Integer> removeDuplicates(Map<String,Integer> mp)
    {
        ArrayList<String>list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : mp.entrySet())
            list.add(entry.getKey());

        for(int i=0;i<list.size();i++)
        {
            for(int j=i+1;j<list.size();j++)
            {
                String temp = list.get(j);
                String[]str1 = list.get(i).split(",");
                String[]str2 = list.get(j).split(",");
                //Frequency Array if the 3 items' freq =2 remove one
                Arrays.sort(str1);
                Arrays.sort(str2);
                if(str1[0].equals(str2[0]) && str1[1].equals(str2[1]) && str1[2].equals(str2[2]))
                    mp.remove(temp);
            }
        }
        return mp;
    }

    //Create candidate3 Item sets
    public Map<String,Integer> createCandidate3(Map<String,Integer>mp) throws FileNotFoundException {
        Map<String,Integer>candidate3Map = new HashMap<>();
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : mp.entrySet())
            list.add(entry.getKey());

        for(int i=0;i<list.size();i++)
        {
            for(int j=i+1;j<list.size();j++)
            {
                ArrayList<String> noDuplicatesArray = removeDuplicates(list.get(i),list.get(j));
                if(noDuplicatesArray.size()>3)
                    continue;
                if(getSupportOfSubset(noDuplicatesArray.get(0),noDuplicatesArray.get(1),noDuplicatesArray.get(2))>minimumSupport)
                {
                    int support = getSupportOfSubset(noDuplicatesArray.get(0),noDuplicatesArray.get(1),noDuplicatesArray.get(2));
                    String temp = noDuplicatesArray.get(0) + "," + noDuplicatesArray.get(1) + ',' + noDuplicatesArray.get(2);
                    candidate3Map.put(temp,support);
                }
            }
        }
        return candidate3Map;


    }

    public float getConfidence1(String s1,String s2,String s3,int support) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int cnt=0;
        while (scanner.hasNextLine())
        {

            String line = scanner.nextLine();
            String str[] = line.split(",");
            for(int i=0;i < str.length;i++)
            {
                if(str[i].equals(s1))
                    cnt++;
            }
        }
        return (float)support/cnt;
    }

    public float getConfidence2(String s1,String s2,String s3,int support) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int cnt=0;
        while (scanner.hasNextLine())
        {
            boolean s1flag = false;
            boolean s2flag = false;
            String line = scanner.nextLine();
            String str[] = line.split(",");
            for(int i=0;i < str.length;i++)
            {
                if(str[i].equals(s1))
                    s1flag = true;
                if(str[i].equals(s2))
                    s2flag = true;
                if(s1flag && s2flag)
                    cnt++;
            }
        }
        return (float)support/cnt;
    }

    public Map<String,Float> getAllConfidence(Map<String, Integer> mp) throws FileNotFoundException {
        Map<String,Float> outMap = new HashMap<>();
        for(Map.Entry<String,Integer> entry : mp.entrySet())
        {
            String[] temp = entry.getKey().split(",");
            if(getConfidence1(temp[0],temp[1],temp[2],entry.getValue()) > minimumConfidence)
            {
                outMap.put("{" + temp[0] + "}"+ "--> {" + temp[1] + "," + temp[2] + "}",getConfidence1(temp[0],temp[1],temp[2],entry.getValue()));
            }
            if(getConfidence1(temp[1],temp[0],temp[2],entry.getValue()) > minimumConfidence)
            {
                outMap.put("{" + temp[1] + "}"+ "--> {" + temp[0] + "," + temp[2] + "}",getConfidence1(temp[1],temp[0],temp[2],entry.getValue()));
            }
            if(getConfidence1(temp[2],temp[0],temp[1],entry.getValue()) > minimumConfidence)
            {
                outMap.put("{" + temp[2] + "}"+ "--> {" + temp[0] + "," + temp[1] + "}",getConfidence1(temp[2],temp[0],temp[1],entry.getValue()));
            }
            if(getConfidence2(temp[0],temp[1],temp[2],entry.getValue()) > minimumConfidence)
            {
                outMap.put("{" + temp[0] + "}"+ ", {" + temp[1] + "}" +"----->" + temp[2] + "}",getConfidence2(temp[0],temp[1],temp[2],entry.getValue()));
            }
            if(getConfidence2(temp[1],temp[2],temp[0],entry.getValue()) > minimumConfidence)
            {
                outMap.put("{" + temp[1] + "}"+ ", {" + temp[2] + "}" +"----->" + temp[0] + "}",getConfidence2(temp[1],temp[2],temp[0],entry.getValue()));
            }
            if(getConfidence2(temp[0],temp[2],temp[1],entry.getValue()) > minimumConfidence)
            {
                outMap.put("{" + temp[0] + "}"+ ", {" + temp[2] + "}" +"----->" + temp[1] + "}",getConfidence2(temp[0],temp[2],temp[1],entry.getValue()));
            }
        }
        return outMap;
    }

    public void printFrequencySets(Map<String,Integer>mp)
    {
        for (Map.Entry<String, Integer> entry : mp.entrySet())
            System.out.println(entry.getKey() + "------->>" + entry.getValue());
    }

    public void printConfidenceRules(Map<String,Float> mp)
    {
        for(Map.Entry<String,Float> entry : mp.entrySet())
        {
            System.out.println(entry.getKey() + "==================>>>>>" + entry
            .getValue());
            System.out.println("--------------------------------------------------------------------------------------");
        }
    }




}

